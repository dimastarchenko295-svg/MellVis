package ru.mellvis.module;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import ru.mellvis.modules.hud.ActivePotionsModule;
import ru.mellvis.modules.hud.TargetHudModule;
import ru.mellvis.modules.hud.WatermarkModule;
import ru.mellvis.modules.movement.AutoSprintModule;
import ru.mellvis.modules.utility.AutoSwapModule;
import ru.mellvis.modules.utility.ElytraSwapModule;
import ru.mellvis.modules.visual.CustomParticlesModule;
import ru.mellvis.modules.visual.TotemAngelModule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        add(new WatermarkModule());
        add(new ActivePotionsModule());
        add(new TargetHudModule());
        add(new AutoSprintModule());
        add(new CustomParticlesModule());
        add(new TotemAngelModule());
        add(new AutoSwapModule());
        add(new ElytraSwapModule());
        modules.sort(Comparator.comparing(Module::name));
    }

    private void add(Module module) { modules.add(module); }
    public List<Module> all() { return modules; }
    public List<Module> byCategory(Category category) { return modules.stream().filter(m -> m.category() == category).toList(); }
    public Optional<Module> get(String name) { return modules.stream().filter(m -> m.name().equalsIgnoreCase(name)).findFirst(); }

    public void tick() { modules.stream().filter(Module::enabled).forEach(Module::onTick); }
    public void render(DrawContext context, float tickDelta) { modules.stream().filter(Module::enabled).forEach(m -> m.onRender(context, tickDelta)); }

    public void onKey(int key, int action) {
        if (action != GLFW.GLFW_PRESS) return;
        for (Module module : modules) {
            if (module instanceof KeyPressListener listener && listener.onKeyPressed(key)) continue;
            if (module.key().get() == key) module.toggle();
        }
    }
}
