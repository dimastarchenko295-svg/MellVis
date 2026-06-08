package ru.mellvis.modules.movement;

import org.lwjgl.glfw.GLFW;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;

public class AutoSprintModule extends Module {
    public AutoSprintModule() { super("AutoSprint", "Automatically holds sprint while moving forward.", Category.MOVEMENT, GLFW.GLFW_KEY_UNKNOWN, false); }
    @Override public void onTick() {
        if (mc.player == null || mc.options == null) return;
        boolean canSprint = mc.player.input.movementForward > 0.0f && !mc.player.isSneaking() && !mc.player.horizontalCollision;
        mc.options.sprintKey.setPressed(canSprint);
    }
    @Override public void onDisable() { if (mc.options != null) mc.options.sprintKey.setPressed(false); }
}
