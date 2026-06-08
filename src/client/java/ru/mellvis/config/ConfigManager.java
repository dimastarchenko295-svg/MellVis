package ru.mellvis.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import ru.mellvis.module.Module;
import ru.mellvis.module.ModuleManager;
import ru.mellvis.module.settings.Setting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final ModuleManager modules;
    private final Path dir;

    public ConfigManager(ModuleManager modules) {
        this.modules = modules;
        this.dir = FabricLoader.getInstance().getConfigDir().resolve("mellvis");
    }

    public void loadDefault() { if (!load("default")) save("default"); }
    public void saveDefault() { save("default"); }

    public void save(String name) {
        try {
            Files.createDirectories(dir);
            JsonObject root = new JsonObject();
            for (Module module : modules.all()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("enabled", module.enabled());
                for (Setting<?> setting : module.settings()) obj.addProperty(setting.getName(), setting.asString());
                root.add(module.name(), obj);
            }
            Files.writeString(path(name), GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException e) { throw new IllegalStateException("Unable to save MellVis config", e); }
    }

    public boolean load(String name) {
        Path p = path(name);
        if (!Files.exists(p)) return false;
        try {
            JsonObject root = GSON.fromJson(Files.readString(p, StandardCharsets.UTF_8), JsonObject.class);
            for (Module module : modules.all()) {
                if (!root.has(module.name())) continue;
                JsonObject obj = root.getAsJsonObject(module.name());
                if (obj.has("enabled")) module.setEnabled(obj.get("enabled").getAsBoolean());
                for (Setting<?> setting : module.settings()) if (obj.has(setting.getName())) setting.fromString(obj.get(setting.getName()).getAsString());
            }
            return true;
        } catch (Exception e) { return false; }
    }

    public List<String> list() {
        try {
            Files.createDirectories(dir);
            List<String> result = new ArrayList<>();
            try (var stream = Files.list(dir)) {
                stream.filter(p -> p.getFileName().toString().endsWith(".cfg"))
                    .forEach(p -> result.add(p.getFileName().toString().replaceFirst("\\.cfg$", "")));
            }
            return result;
        } catch (IOException e) { return List.of(); }
    }

    private Path path(String name) { return dir.resolve(name.replaceAll("[^A-Za-z0-9_.-]", "_") + ".cfg"); }
}
