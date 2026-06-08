package ru.mellvis.modules.visual;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.MellVisClient;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;

public class TotemAngelModule extends Module {
    public TotemAngelModule() { super("TotemAngel", "Renders a fading angel silhouette when an enemy pops a totem.", Category.VISUAL, GLFW.GLFW_KEY_UNKNOWN, true); }
    @Override public void onRender(DrawContext context, float tickDelta) { MellVisClient.angels.render2d(context); }
}
