package io.github.foecollab.mixin;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.LoadingHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.bar.ExperienceBar;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 1.21.11: the vanilla experience bar graphic moved out of InGameHud into this Bar implementation.
@Mixin(ExperienceBar.class)
public class ExperienceBarMixin {
    @Unique
    private final FOEConfig config = FOEConfig.getConfig();

    @Inject(method = "renderBar", at = @At("HEAD"), cancellable = true)
    private void hideExperienceBar(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if(config.barHUD.showBar && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }
}
