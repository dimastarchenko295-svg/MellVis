package ru.mellvis.module.settings;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, boolean value) { super(name, value); }
    public void toggle() { set(!get()); }
    @Override public void fromString(String value) { set(Boolean.parseBoolean(value)); }
}
