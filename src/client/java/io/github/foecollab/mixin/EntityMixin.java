package io.github.foecollab.mixin;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.HiderHandler;
import io.github.foecollab.handler.LoadingHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin<T extends Entity, S extends EntityRenderState> {
    @Shadow public abstract Text getName();

    @Unique
    private final FOEConfig config = FOEConfig.getConfig();

    @Inject(method = "isCustomNameVisible", at = @At("RETURN"), cancellable = true)
    private void injectShouldRenderName(CallbackInfoReturnable<Boolean> cir) {
        if(LoadingHandler.instance().isOnServer
                && this.getName().getString().contains("'s")
                && this.getName().getString().contains("Pet")
        ) {
            if(MinecraftClient.getInstance().player != null
                    && this.getName().getString().contains(MinecraftClient.getInstance().player.getName().getString())
            ) {
                if(config.petFollower.ownPet != HiderHandler.FollowingPetState.OFF) {
                    cir.setReturnValue(false);
                }
            } else if(config.petFollower.otherPets != HiderHandler.FollowingPetState.OFF) {
                cir.setReturnValue(false);
            } else {
                cir.setReturnValue(cir.getReturnValue());
            }
        } else {
            cir.setReturnValue(cir.getReturnValue());
        }
    }
}
