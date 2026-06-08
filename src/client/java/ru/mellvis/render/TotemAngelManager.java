package ru.mellvis.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class TotemAngelManager {
    private final List<Angel> angels = new ArrayList<>();

    public void spawn(UUID uuid, Vec3d pos, float entityHeight) {
        angels.add(new Angel(uuid, pos, entityHeight, System.currentTimeMillis()));
    }

    public void tick() {
        long now = System.currentTimeMillis();
        angels.removeIf(a -> now - a.created > 1900);
    }

    public boolean has(UUID uuid) { return angels.stream().anyMatch(a -> a.uuid.equals(uuid)); }

    public void render(MatrixStack matrices, VertexConsumerProvider vertices, UUID uuid) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        long now = System.currentTimeMillis();
        for (Angel angel : angels) {
            if (!angel.uuid.equals(uuid)) continue;
            float age = (now - angel.created) / 1900f;
            float alpha = Math.max(0, 1f - age);
            matrices.push();
            matrices.translate(0, angel.height * 0.65 + age * 1.4, 0);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
            matrices.scale(0.9f + age * 0.35f, 0.9f + age * 0.35f, 0.9f + age * 0.35f);
            drawSilhouette(matrices, vertices, alpha);
            matrices.pop();
        }
    }

    private void drawSilhouette(MatrixStack matrices, VertexConsumerProvider vertices, float alpha) {
        VertexConsumer line = vertices.getBuffer(RenderLayer.getLines());
        int a = (int)(alpha * 210);
        segment(line, matrices, 0, -0.45f, 0, 0, 0.55f, 0, a);
        segment(line, matrices, -0.16f, 0.18f, 0, -0.42f, -0.12f, 0, a);
        segment(line, matrices, 0.16f, 0.18f, 0, 0.42f, -0.12f, 0, a);
        segment(line, matrices, -0.95f, 0.25f, 0, -0.22f, 0.04f, 0, a);
        segment(line, matrices, 0.22f, 0.04f, 0, 0.95f, 0.25f, 0, a);
        for (int i = 0; i < 18; i++) {
            double a0 = Math.PI * 2 * i / 18.0;
            double a1 = Math.PI * 2 * (i + 1) / 18.0;
            segment(line, matrices, (float)Math.cos(a0) * .25f, .78f, (float)Math.sin(a0) * .08f, (float)Math.cos(a1) * .25f, .78f, (float)Math.sin(a1) * .08f, a);
        }
    }

    private void segment(VertexConsumer line, MatrixStack matrices, float x1, float y1, float z1, float x2, float y2, float z2, int alpha) {
        MatrixStack.Entry e = matrices.peek();
        line.vertex(e, x1, y1, z1).color(255, 240, 180, alpha).normal(e, 0, 1, 0);
        line.vertex(e, x2, y2, z2).color(255, 240, 180, alpha).normal(e, 0, 1, 0);
    }

    public void render2d(net.minecraft.client.gui.DrawContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        int sw = context.getScaledWindowWidth(), sh = context.getScaledWindowHeight();
        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        for (Angel a : angels) {
            float age = (System.currentTimeMillis() - a.created) / 1900f;
            Vec3d d = a.pos.add(0, a.height + age * 1.4, 0).subtract(cam);
            if (d.length() > 40) continue;
            int x = (int)(sw / 2.0 + d.x * 18.0);
            int y = (int)(sh / 2.0 - d.y * 18.0);
            int alpha = (int)((1f - age) * 180);
            int c = (alpha << 24) | 0xFFE8A8;
            context.fill(x - 10, y - 2, x + 11, y + 1, c);
            context.fill(x - 2, y - 14, x + 3, y + 16, c);
            context.drawBorder(x - 6, y - 20, 12, 6, c);
            context.fill(x - 28, y - 7, x - 8, y - 4, c);
            context.fill(x + 8, y - 7, x + 28, y - 4, c);
        }
    }

    private record Angel(UUID uuid, Vec3d pos, float height, long created) {}
}
