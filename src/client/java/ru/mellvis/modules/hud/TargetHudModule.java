package ru.mellvis.modules.hud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.event.CombatEvents;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;
import ru.mellvis.module.settings.NumberSetting;
import ru.mellvis.util.ColorUtil;
import ru.mellvis.util.Render2D;

public class TargetHudModule extends Module {
    private final NumberSetting x = add(new NumberSetting("X", 260, 0, 900, 1));
    private final NumberSetting y = add(new NumberSetting("Y", 185, 0, 600, 1));
    private float animatedHealth = -1f;

    public TargetHudModule() { super("TargetHUD", "Shows the last attacked target with lerped HP and armor.", Category.HUD, GLFW.GLFW_KEY_UNKNOWN, true); }

    @Override public void onRender(DrawContext context, float tickDelta) {
        LivingEntity target = CombatEvents.targetForHud();
        if (target == null) { animatedHealth = -1f; return; }
        int px = x.get().intValue(), py = y.get().intValue();
        float max = Math.max(1f, target.getMaxHealth());
        float hp = Math.max(0, target.getHealth() + target.getAbsorptionAmount());
        if (animatedHealth < 0) animatedHealth = hp;
        animatedHealth += (hp - animatedHealth) * 0.12f;
        float hpPct = Math.min(1f, hp / max);
        float animPct = Math.min(1f, animatedHealth / max);

        Render2D.rect(context, px, py, 168, 72, ColorUtil.argb(170, 14, 14, 22));
        Render2D.border(context, px, py, 168, 72, ColorUtil.gradient(0));
        drawHead(context, target, px + 8, py + 9, 32);
        Render2D.text(context, target.getName().getString(), px + 48, py + 10, 0xFFFFFFFF);
        Render2D.text(context, String.format("%.1f / %.1f HP", hp, max), px + 48, py + 24, 0xFFEFEFFF);
        Render2D.rect(context, px + 48, py + 41, 108, 8, ColorUtil.argb(160, 35, 35, 42));
        Render2D.rect(context, px + 48, py + 41, (int)(108 * animPct), 8, ColorUtil.argb(210, 160, 45, 45));
        Render2D.rect(context, px + 48, py + 41, (int)(108 * hpPct), 8, ColorUtil.argb(230, 65, 235, 130));
        int ax = px + 48;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = target.getEquippedStack(slot);
            context.drawItem(stack, ax, py + 52);
            context.drawItemInSlot(mc.textRenderer, stack, ax, py + 52);
            int pct = CombatEvents.durabilityPct(stack);
            if (pct >= 0) Render2D.text(context, pct + "%", ax, py + 66, pct > 40 ? 0xFFAAFFAA : 0xFFFF7777);
            ax += 24;
        }
    }

    private void drawHead(DrawContext context, LivingEntity target, int x, int y, int size) {
        ItemStack head = target.getEquippedStack(EquipmentSlot.HEAD);
        if (!head.isEmpty()) {
            context.drawItem(head, x + 8, y + 8);
            context.drawItemInSlot(mc.textRenderer, head, x + 8, y + 8);
        } else {
            Render2D.rect(context, x, y, size, size, ColorUtil.argb(180, 35, 35, 48));
            Render2D.gradientText(context, target.getName().getString().substring(0, 1).toUpperCase(), x + 11, y + 11);
        }
    }
}
