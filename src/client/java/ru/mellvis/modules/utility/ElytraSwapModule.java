package ru.mellvis.modules.utility;

import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.module.Category;
import ru.mellvis.module.Module;
import ru.mellvis.module.KeyPressListener;
import ru.mellvis.module.settings.KeySetting;
import ru.mellvis.util.InventoryUtil;

public class ElytraSwapModule extends Module implements KeyPressListener {
    private final KeySetting fireworkKey = add(new KeySetting("FireworkBind", GLFW.GLFW_KEY_V));
    private long restoreAt = 0;
    private int restoreSlot = -1;

    public ElytraSwapModule() { super("ElytraSwap", "Manual elytra/chestplate swap and 500ms firework use on a separate bind.", Category.UTILITY, GLFW.GLFW_KEY_H, false); }

    @Override public boolean onKeyPressed(int key) {
        if (key == key().get()) { swapChest(); return true; }
        if (key == fireworkKey.get()) { useFireworkLegit(); return true; }
        return false;
    }

    @Override public void onTick() {
        if (mc.player == null || mc.interactionManager == null) return;
        long now = System.currentTimeMillis();
        if (restoreAt > 0 && now >= restoreAt) {
            InventoryUtil.selectHotbar(restoreSlot);
            restoreAt = 0; restoreSlot = -1;
        }
    }

    private void swapChest() {
        if (mc.player == null) return;
        int elytra = InventoryUtil.find(Items.ELYTRA);
        if (elytra < 0) return;
        InventoryUtil.pickupSwap(6, InventoryUtil.invToScreenSlot(elytra));
    }

    private void useFireworkLegit() {
        int firework = InventoryUtil.find(Items.FIREWORK_ROCKET);
        if (firework < 0 || mc.interactionManager == null || mc.player == null) return;
        int prev = InventoryUtil.selectedHotbarSlot();
        int hotbar = firework >= 0 && firework <= 8 ? firework : firstFreeHotbar();
        if (hotbar < 0) return;
        if (firework > 8) mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, InventoryUtil.invToScreenSlot(firework), hotbar, SlotActionType.SWAP, mc.player);
        InventoryUtil.selectHotbar(hotbar);
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        restoreSlot = prev;
        restoreAt = System.currentTimeMillis() + 500;
    }

    private int firstFreeHotbar() {
        for (int i = 0; i < 9; i++) if (mc.player.getInventory().getStack(i).isEmpty()) return i;
        return mc.player.getInventory().selectedSlot;
    }
}
