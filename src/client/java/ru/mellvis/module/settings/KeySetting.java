package ru.mellvis.module.settings;

public class KeySetting extends Setting<Integer> {
    public KeySetting(String name, int key) { super(name, key); }
    @Override public void fromString(String value) { set(Integer.parseInt(value)); }
}
