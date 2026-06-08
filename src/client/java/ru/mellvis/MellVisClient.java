package ru.mellvis;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import ru.mellvis.config.ConfigManager;
import ru.mellvis.gui.ClickGuiScreen;
import ru.mellvis.module.ModuleManager;
import ru.mellvis.render.ParticleManager;
import ru.mellvis.render.TotemAngelManager;
import ru.mellvis.util.InputTracker;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class MellVisClient implements ClientModInitializer {
    public static final String MOD_ID = "mellvis";
    public static final String NAME = "MellVis";
    public static ModuleManager modules;
    public static ConfigManager configs;
    public static ParticleManager particles;
    public static TotemAngelManager angels;
    private static InputTracker input;

    @Override
    public void onInitializeClient() {
        modules = new ModuleManager();
        particles = new ParticleManager();
        angels = new TotemAngelManager();
        configs = new ConfigManager(modules);
        input = new InputTracker();
        configs.loadDefault();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            input.tick(client);
            modules.tick();
            particles.tick();
            angels.tick();
        });
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("cfg")
            .then(literal("save").then(argument("name", word()).executes(ctx -> save(ctx.getSource(), getString(ctx, "name")))))
            .then(literal("load").then(argument("name", word()).executes(ctx -> load(ctx.getSource(), getString(ctx, "name")))))
            .then(literal("list").executes(ctx -> list(ctx.getSource())))));
    }

    private int save(FabricClientCommandSource source, String name) {
        configs.save(name);
        source.sendFeedback(Text.literal("MellVis: saved config " + name));
        return 1;
    }

    private int load(FabricClientCommandSource source, String name) {
        if (configs.load(name)) source.sendFeedback(Text.literal("MellVis: loaded config " + name));
        else source.sendError(Text.literal("MellVis: config not found: " + name));
        return 1;
    }

    private int list(FabricClientCommandSource source) {
        source.sendFeedback(Text.literal("MellVis configs: " + String.join(", ", configs.list())));
        return 1;
    }

    public static void openGui() {
        MinecraftClient.getInstance().setScreen(new ClickGuiScreen());
    }
}
