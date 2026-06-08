package ru.mellvis.util;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.MellVisClient;
import ru.mellvis.module.Module;
import ru.mellvis.module.settings.Setting;

import java.util.HashMap;
import java.util.Map;

public class InputTracker {
    private final Map<Integer, Boolean> previous = new HashMap<>();

    public void tick(MinecraftClient mc) {
        if (mc.getWindow() == null) return;
        long handle = mc.getWindow().getHandle();
        press(handle, GLFW.GLFW_KEY_RIGHT_SHIFT, () -> MellVisClient.openGui());
        for (Module module : MellVisClient.modules.all()) {
            poll(handle, module.key().get());
            for (Setting<?> setting : module.settings()) if (setting.get() instanceof Integer key) poll(handle, key);
        }
    }

    private void poll(long handle, int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN || key < 0) return;
        press(handle, key, () -> MellVisClient.modules.onKey(key, GLFW.GLFW_PRESS));
    }

    private void press(long handle, int key, Runnable action) {
        boolean down = GLFW.glfwGetKey(handle, key) == GLFW.GLFW_PRESS;
        boolean was = previous.getOrDefault(key, false);
        if (down && !was) action.run();
        previous.put(key, down);
    }
}
