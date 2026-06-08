package ru.mellvis.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.mellvis.MellVisClient;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(method = "render", at = @At("TAIL"))
    private void mellvis$render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (MellVisClient.modules != null) MellVisClient.modules.render(context, tickCounter.getTickDelta(false));
    }
}
