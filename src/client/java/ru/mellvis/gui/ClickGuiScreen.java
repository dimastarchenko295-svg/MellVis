package ru.mellvis.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.MellVisClient;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;
import ru.mellvis.module.settings.BooleanSetting;
import ru.mellvis.module.settings.KeySetting;
import ru.mellvis.module.settings.ModeSetting;
import ru.mellvis.module.settings.NumberSetting;
import ru.mellvis.module.settings.Setting;
import ru.mellvis.util.ColorUtil;
import ru.mellvis.util.Render2D;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClickGuiScreen extends Screen {
    private final Map<Category, int[]> positions = new EnumMap<>(Category.class);
    private final Set<Module> open = new HashSet<>();
    private Setting<?> binding;

    public ClickGuiScreen() { super(Text.literal("MellVis ClickGUI")); int x = 24; for (Category c : Category.values()) { positions.put(c, new int[]{x, 32}); x += 142; } }

    @Override public boolean shouldPause() { return false; }

    @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        for (Category category : Category.values()) drawCategory(context, category, mouseX, mouseY);
        if (binding != null) {
            String text = "Нажми клавишу для " + binding.getName() + " (ESC = снять)";
            Render2D.rect(context, width / 2 - 125, height - 35, 250, 22, ColorUtil.argb(210, 10, 10, 15));
            Render2D.gradientText(context, text, width / 2 - Render2D.textWidth(text) / 2, height - 28);
        }
    }

    private void drawCategory(DrawContext ctx, Category category, int mouseX, int mouseY) {
        int[] pos = positions.get(category);
        int x = pos[0], y = pos[1], w = 132;
        Render2D.rect(ctx, x, y, w, 18, ColorUtil.argb(210, 18, 18, 28));
        Render2D.border(ctx, x, y, w, 18, ColorUtil.gradient(category.ordinal() * 220L));
        Render2D.gradientText(ctx, category.title(), x + 7, y + 5);
        int cy = y + 22;
        for (Module module : MellVisClient.modules.byCategory(category)) {
            Render2D.rect(ctx, x, cy, w, 18, module.enabled() ? ColorUtil.argb(210, 38, 55, 72) : ColorUtil.argb(175, 22, 22, 31));
            Render2D.text(ctx, module.name(), x + 6, cy + 5, module.enabled() ? 0xFFFFFFFF : 0xFFB8B8C8);
            Render2D.text(ctx, open.contains(module) ? "-" : "+", x + w - 12, cy + 5, 0xFFFFFFFF);
            cy += 20;
            if (open.contains(module)) {
                for (Setting<?> setting : module.settings()) {
                    cy = drawSetting(ctx, setting, x + 4, cy, w - 8, mouseX, mouseY);
                }
            }
        }
    }

    private int drawSetting(DrawContext ctx, Setting<?> setting, int x, int y, int w, int mouseX, int mouseY) {
        Render2D.rect(ctx, x, y, w, 18, ColorUtil.argb(150, 12, 12, 20));
        if (setting instanceof BooleanSetting b) {
            Render2D.text(ctx, setting.getName() + ": " + (b.get() ? "ON" : "OFF"), x + 5, y + 5, b.get() ? 0xFF88FFAA : 0xFFFF8888);
        } else if (setting instanceof ModeSetting m) {
            Render2D.text(ctx, setting.getName() + ": " + m.get(), x + 5, y + 5, 0xFFFFFFFF);
        } else if (setting instanceof NumberSetting n) {
            double pct = (n.get() - n.min()) / (n.max() - n.min());
            Render2D.text(ctx, setting.getName() + ": " + String.format("%.1f", n.get()), x + 5, y + 4, 0xFFFFFFFF);
            Render2D.rect(ctx, x + 5, y + 15, (int)((w - 10) * pct), 2, ColorUtil.gradient(0));
        } else if (setting instanceof KeySetting k) {
            Render2D.text(ctx, setting.getName() + ": " + GLFW.glfwGetKeyName(k.get(), 0), x + 5, y + 5, binding == setting ? 0xFFFFFF88 : 0xFFFFFFFF);
        }
        return y + 20;
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Category category : Category.values()) {
            int[] pos = positions.get(category); int x = pos[0], y = pos[1], w = 132, cy = y + 22;
            for (Module module : MellVisClient.modules.byCategory(category)) {
                if (inside(mouseX, mouseY, x, cy, w, 18)) { if (button == 0) module.toggle(); else if (button == 1) toggleOpen(module); return true; }
                cy += 20;
                if (open.contains(module)) for (Setting<?> setting : module.settings()) {
                    if (inside(mouseX, mouseY, x + 4, cy, w - 8, 18)) { clickSetting(setting, mouseX, x + 4, w - 8, button); return true; }
                    cy += 20;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void clickSetting(Setting<?> setting, double mouseX, int x, int w, int button) {
        if (setting instanceof BooleanSetting b) b.toggle();
        else if (setting instanceof ModeSetting m) m.next();
        else if (setting instanceof NumberSetting n) n.set(n.min() + ((mouseX - x) / w) * (n.max() - n.min()));
        else if (setting instanceof KeySetting) binding = setting;
    }

    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (binding instanceof KeySetting k) { k.set(keyCode == GLFW.GLFW_KEY_ESCAPE ? GLFW.GLFW_KEY_UNKNOWN : keyCode); binding = null; return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override public void close() { MellVisClient.configs.saveDefault(); super.close(); }
    private void toggleOpen(Module module) { if (!open.add(module)) open.remove(module); }
    private boolean inside(double mx, double my, int x, int y, int w, int h) { return mx >= x && mx <= x + w && my >= y && my <= y + h; }
}
