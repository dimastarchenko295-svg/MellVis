package ru.mellvis.modules.hud;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.MellVisClient;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;
import ru.mellvis.module.settings.NumberSetting;
import ru.mellvis.util.ColorUtil;
import ru.mellvis.util.Render2D;

public class WatermarkModule extends Module {
    private final NumberSetting x = add(new NumberSetting("X", 8, 0, 500, 1));
    private final NumberSetting y = add(new NumberSetting("Y", 8, 0, 300, 1));

    public WatermarkModule() { super("Watermark", "Gradient client name, nickname and FPS.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN, true); }

    @Override public void onRender(DrawContext context, float tickDelta) {
        if (mc.player == null) return;
        String text = MellVisClient.NAME + "  |  " + mc.player.getGameProfile().getName() + "  |  " + mc.getCurrentFps() + " FPS";
        int px = x.get().intValue(), py = y.get().intValue();
        int w = Render2D.textWidth(text) + 14;
        Render2D.rect(context, px, py, w, 20, ColorUtil.argb(150, 12, 12, 18));
        Render2D.border(context, px, py, w, 20, ColorUtil.gradient(0));
        Render2D.gradientText(context, text, px + 7, py + 6);
    }
}
