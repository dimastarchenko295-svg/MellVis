package ru.mellvis.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.function.Predicate;

public final class InventoryUtil {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private InventoryUtil() {}

    public static int find(Predicate<ItemStack> predicate) {
        if (mc.player == null) return -1;
        for (int i = 0; i < mc.player.getInventory().size(); i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (predicate.test(stack)) return i;
        }
        return -1;
    }

    public static int find(Item item) { return find(stack -> stack.isOf(item)); }

    public static int invToScreenSlot(int invSlot) {
        if (invSlot >= 0 && invSlot <= 8) return 36 + invSlot;
        if (invSlot >= 9 && invSlot <= 35) return invSlot;
        if (invSlot == 40) return 45;
        return invSlot;
    }

    public static void swapWithOffhand(int invSlot) {
        if (mc.player == null || mc.interactionManager == null || invSlot < 0) return;
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, invToScreenSlot(invSlot), 40, SlotActionType.SWAP, mc.player);
    }

    public static void pickupSwap(int screenSlotA, int screenSlotB) {
        if (mc.player == null || mc.interactionManager == null) return;
        int sync = mc.player.currentScreenHandler.syncId;
        mc.interactionManager.clickSlot(sync, screenSlotA, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(sync, screenSlotB, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(sync, screenSlotA, 0, SlotActionType.PICKUP, mc.player);
    }

    public static int selectedHotbarSlot() { return mc.player == null ? 0 : mc.player.getInventory().selectedSlot; }
    public static void selectHotbar(int slot) { if (mc.player != null && slot >= 0 && slot <= 8) mc.player.getInventory().selectedSlot = slot; }
}
