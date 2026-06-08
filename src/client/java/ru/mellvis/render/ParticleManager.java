package ru.mellvis.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.mellvis.util.ColorUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleManager {
    private final List<Spark> sparks = new ArrayList<>();
    private final Random random = new Random();

    public void critical(Vec3d pos) {
        for (int i = 0; i < 18; i++) add(pos, true);
    }

    public void totem(Vec3d pos) {
        for (int i = 0; i < 36; i++) add(pos, false);
    }

    private void add(Vec3d pos, boolean crit) {
        double speed = crit ? 0.065 : 0.095;
        Vec3d vel = new Vec3d((random.nextDouble() - .5) * speed, random.nextDouble() * speed, (random.nextDouble() - .5) * speed);
        sparks.add(new Spark(pos, vel, System.currentTimeMillis(), crit ? 650 : 1100, ColorUtil.gradient(random.nextInt(2000))));
    }

    public void tick() {
        long now = System.currentTimeMillis();
        Iterator<Spark> it = sparks.iterator();
        while (it.hasNext()) {
            Spark s = it.next();
            s.pos = s.pos.add(s.vel);
            s.vel = s.vel.multiply(0.96).add(0, -0.0015, 0);
            if (now - s.created > s.life) it.remove();
        }
    }

    public void render2d(DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.gameRenderer == null || mc.player == null) return;
        int sw = context.getScaledWindowWidth(), sh = context.getScaledWindowHeight();
        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        for (Spark s : sparks) {
            Vec3d d = s.pos.subtract(cam);
            if (d.length() > 32) continue;
            float alpha = 1f - (System.currentTimeMillis() - s.created) / (float)s.life;
            int x = (int)(sw / 2.0 + d.x * 18.0);
            int y = (int)(sh / 2.0 - d.y * 18.0);
            int color = (s.color & 0x00FFFFFF) | ((int)(MathHelper.clamp(alpha, 0, 1) * 255) << 24);
            context.fill(x - 2, y, x + 3, y + 1, color);
            context.fill(x, y - 2, x + 1, y + 3, color);
        }
    }

    private static class Spark {
        Vec3d pos; Vec3d vel; long created; int life; int color;
        Spark(Vec3d pos, Vec3d vel, long created, int life, int color) { this.pos = pos; this.vel = vel; this.created = created; this.life = life; this.color = color; }
    }
}
