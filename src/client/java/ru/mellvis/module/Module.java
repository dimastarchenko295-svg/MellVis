package ru.mellvis.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import ru.mellvis.module.settings.KeySetting;
import ru.mellvis.module.settings.Setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;
    private final KeySetting key;

    protected Module(String name, String description, Category category, int defaultKey, boolean enabledByDefault) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.key = add(new KeySetting("Bind", defaultKey));
        this.enabled = enabledByDefault;
    }

    protected <T extends Setting<?>> T add(T setting) { settings.add(setting); return setting; }

    public void toggle() { setEnabled(!enabled); }
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }

    public String name() { return name; }
    public String description() { return description; }
    public Category category() { return category; }
    public boolean enabled() { return enabled; }
    public KeySetting key() { return key; }
    public List<Setting<?>> settings() { return Collections.unmodifiableList(settings); }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
    public void onRender(DrawContext context, float tickDelta) {}
}
