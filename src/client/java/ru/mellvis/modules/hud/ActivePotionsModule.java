package ru.mellvis.modules.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;
import ru.mellvis.module.settings.NumberSetting;
import ru.mellvis.util.ColorUtil;
import ru.mellvis.util.Render2D;

public class ActivePotionsModule extends Module {
    private final NumberSetting x = add(new NumberSetting("X", 8, 0, 700, 1));
    private final NumberSetting y = add(new NumberSetting("Y", 34, 0, 500, 1));

    public ActivePotionsModule() { super("ActivePotions", "Effect list with icons and duration.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN, true); }

    @Override public void onRender(DrawContext context, float tickDelta) {
        if (mc.player == null) return;
        int px = x.get().intValue(), py = y.get().intValue();
        int row = 0;
        for (StatusEffectInstance effect : mc.player.getStatusEffects()) {
            String name = effect.getEffectType().value().getName().getString();
            String line = name + " " + (effect.getAmplifier() + 1) + "  " + format(effect.getDuration());
            int yy = py + row * 23;
            Render2D.rect(context, px, yy, Math.max(118, Render2D.textWidth(line) + 30), 20, ColorUtil.argb(135, 18, 18, 26));
            Render2D.border(context, px, yy, Math.max(118, Render2D.textWidth(line) + 30), 20, ColorUtil.gradient(row * 180L));
            Identifier icon = texture(effect.getEffectType());
            context.drawTexture(icon, px + 3, yy + 2, 0, 0, 18, 18, 18, 18);
            Render2D.text(context, line, px + 25, yy + 6, 0xFFFFFFFF);
            row++;
        }
    }

    private Identifier texture(RegistryEntry<?> effect) {
        Identifier id = effect.getKey().map(k -> k.getValue()).orElse(Identifier.ofVanilla("speed"));
        return Identifier.ofVanilla("textures/mob_effect/" + id.getPath() + ".png");
    }

    private String format(int ticks) {
        int seconds = ticks / 20;
        return (seconds / 60) + ":" + String.format("%02d", seconds % 60);
    }
}
