package io.github.foecollab.mixin;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.LoadingHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {
    @Unique
    private final FOEConfig config = FOEConfig.getConfig();

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void injectRender(DrawContext context, CallbackInfo ci) {
        if(config.bossBarTracker.hideBossBar && LoadingHandler.instance().isOnServer) {
            ci.cancel();
        }
    }
}
