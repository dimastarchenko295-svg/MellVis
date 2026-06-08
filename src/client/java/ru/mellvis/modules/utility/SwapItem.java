package ru.mellvis.modules.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public enum SwapItem {
    TOTEM("Тотем"), ENCHANTED_TOTEM("Зач.Тотем"), ENDER_PEARL("Шар"), SHIELD("Щит"), GOLDEN_APPLE("Золотое яблоко");
    public final String title;
    SwapItem(String title) { this.title = title; }
    public boolean matches(ItemStack stack) {
        return switch (this) {
            case TOTEM -> stack.isOf(Items.TOTEM_OF_UNDYING);
            case ENCHANTED_TOTEM -> stack.isOf(Items.TOTEM_OF_UNDYING) && stack.hasGlint();
            case ENDER_PEARL -> stack.isOf(Items.ENDER_PEARL);
            case SHIELD -> stack.isOf(Items.SHIELD);
            case GOLDEN_APPLE -> stack.isOf(Items.GOLDEN_APPLE) || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE);
        };
    }
    public static String[] titles() { SwapItem[] v = values(); String[] s = new String[v.length]; for (int i=0;i<v.length;i++) s[i]=v[i].title; return s; }
    public static SwapItem byTitle(String title) { for (SwapItem i : values()) if (i.title.equals(title)) return i; return TOTEM; }
}
