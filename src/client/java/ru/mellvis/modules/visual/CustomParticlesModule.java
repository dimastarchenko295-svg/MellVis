package ru.mellvis.modules.visual;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.MellVisClient;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;

public class CustomParticlesModule extends Module {
    public CustomParticlesModule() { super("CustomParticles", "Bright star/line particles for crits and totems.", Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, true); }
    @Override public void onRender(DrawContext context, float tickDelta) { MellVisClient.particles.render2d(context); }
}
