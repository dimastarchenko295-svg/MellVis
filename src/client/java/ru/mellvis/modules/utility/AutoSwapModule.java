package ru.mellvis.modules.utility;

import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;
import ru.mellvis.module.KeyPressListener;
import ru.mellvis.module.settings.ModeSetting;
import ru.mellvis.util.InventoryUtil;

public class AutoSwapModule extends Module implements KeyPressListener {
    private final ModeSetting first = add(new ModeSetting("Первый", SwapItem.TOTEM.title, SwapItem.titles()));
    private final ModeSetting second = add(new ModeSetting("Второй", SwapItem.ENDER_PEARL.title, SwapItem.titles()));

    public AutoSwapModule() { super("AutoSwap", "Manual offhand swap between two selected item types; no anticheat bypass logic.", Category.UTILITY, GLFW.GLFW_KEY_G, false); }

    @Override public boolean onKeyPressed(int key) {
        if (key == key().get()) { performSwap(); return true; }
        return false;
    }

    private void performSwap() {
        if (mc.player == null) return;
        ItemStack offhand = mc.player.getOffHandStack();
        SwapItem a = SwapItem.byTitle(first.get());
        SwapItem b = SwapItem.byTitle(second.get());
        SwapItem desired = a.matches(offhand) ? b : a;
        int slot = InventoryUtil.find(desired::matches);
        if (slot >= 0) InventoryUtil.swapWithOffhand(slot);
    }
}
