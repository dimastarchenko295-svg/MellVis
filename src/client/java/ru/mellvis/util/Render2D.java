package ru.mellvis.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class Render2D {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private Render2D() {}

    public static void rect(DrawContext ctx, int x, int y, int w, int h, int color) { ctx.fill(x, y, x + w, y + h, color); }
    public static void border(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + 1, color); ctx.fill(x, y + h - 1, x + w, y + h, color);
        ctx.fill(x, y, x + 1, y + h, color); ctx.fill(x + w - 1, y, x + w, y + h, color);
    }
    public static void text(DrawContext ctx, String s, int x, int y, int color) { ctx.drawText(mc.textRenderer, Text.literal(s), x, y, color, true); }
    public static void gradientText(DrawContext ctx, String s, int x, int y) {
        int ox = 0;
        for (int i = 0; i < s.length(); i++) {
            String ch = String.valueOf(s.charAt(i));
            ctx.drawText(mc.textRenderer, Text.literal(ch), x + ox, y, ColorUtil.gradient(i * 95L), true);
            ox += mc.textRenderer.getWidth(ch);
        }
    }
    public static int textWidth(String s) { return mc.textRenderer.getWidth(s); }
}
