package ru.mellvis.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.mellvis.event.CombatEvents;

@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {
    @Inject(method = "attack", at = @At("HEAD"))
    private void mellvis$attack(Entity target, CallbackInfo ci) {
        CombatEvents.onAttack((ClientPlayerEntity)(Object)this, target);
    }
}
