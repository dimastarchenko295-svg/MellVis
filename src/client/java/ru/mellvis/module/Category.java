package ru.mellvis.module;

public enum Category {
    HUD("HUD"), VISUAL("Visual"), MOVEMENT("Movement"), UTILITY("Utility");

    private final String title;

    Category(String title) { this.title = title; }
    public String title() { return title; }
}
