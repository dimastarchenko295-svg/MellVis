package ru.mellvis.module.settings;

public abstract class Setting<T> {
    private final String name;
    private T value;

    protected Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public T get() { return value; }
    public void set(T value) { this.value = value; }
    public String asString() { return String.valueOf(value); }
    public abstract void fromString(String value);
}
