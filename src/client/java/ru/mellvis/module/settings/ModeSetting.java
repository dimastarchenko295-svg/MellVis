package ru.mellvis.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String value, String... modes) {
        super(name, value);
        this.modes = Arrays.asList(modes);
        if (!this.modes.contains(value) && !this.modes.isEmpty()) set(this.modes.getFirst());
    }

    public List<String> modes() { return modes; }
    public void next() { set(modes.get((modes.indexOf(get()) + 1) % modes.size())); }
    @Override public void set(String value) { if (modes.contains(value)) super.set(value); }
    @Override public void fromString(String value) { set(value); }
}
