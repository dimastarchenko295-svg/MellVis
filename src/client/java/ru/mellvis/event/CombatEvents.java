package ru.mellvis.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import ru.mellvis.MellVisClient;

public final class CombatEvents {
    private CombatEvents() {}
    private static LivingEntity lastTarget;
    private static long lastHitMs;

    public static void onAttack(PlayerEntity attacker, Entity target) {
        if (target instanceof LivingEntity living) {
            lastTarget = living;
            lastHitMs = System.currentTimeMillis();
            MellVisClient.particles.critical(living.getPos().add(0, living.getHeight() * 0.65, 0));
        }
    }

    public static LivingEntity targetForHud() {
        if (lastTarget == null || !lastTarget.isAlive() || System.currentTimeMillis() - lastHitMs > 6000) return null;
        return lastTarget;
    }

    public static void onTotem(LivingEntity entity) {
        Vec3d pos = entity.getPos().add(0, entity.getHeight() * 0.5, 0);
        MellVisClient.particles.totem(pos);
        MellVisClient.angels.spawn(entity.getUuid(), entity.getPos(), entity.getHeight());
    }

    public static int durabilityPct(ItemStack stack) {
        if (stack.isEmpty() || !stack.isDamageable()) return -1;
        return (int)Math.round((1.0 - (double)stack.getDamage() / Math.max(1, stack.getMaxDamage())) * 100.0);
    }
}
