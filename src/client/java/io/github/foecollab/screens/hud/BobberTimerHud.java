package io.github.foecollab.screens.hud;

import io.github.foecollab.common.HudFont;
import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.FishingRodHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/// Draws the bobber waiting-time as a fixed HUD element instead of floating over the bobber.
/// Active only while the player's own bobber is out (see {@link FishingRodHandler}); its color
/// follows the cosmetic HUD font color chosen in the config.
public class BobberTimerHud {
    public void render(DrawContext drawContext, MinecraftClient client) {
        if (!FishingRodHandler.instance().showTimerHud) {
            return;
        }

        // Disappear the instant the fish is hooked / the bobber is reeled in, without waiting for
        // the next tick to clear showTimerHud (render runs many times between ticks).
        if (client.player == null || client.player.fishHook == null) {
            return;
        }

        FOEConfig config = FOEConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;
        Text text = Text.literal(String.format("%.1fs", FishingRodHandler.instance().timerSeconds));
        int color = 0xFF000000 | (HudFont.baseColor() & 0xFFFFFF);

        drawContext.getMatrices().pushMatrix();
        try {
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            // Calculate base positions relative to screen size (movable via the HUD editor)
            int baseX = (int) (screenWidth * (config.bobberTracker.timerHudX / 100f));
            int baseY = (int) (screenHeight * (config.bobberTracker.timerHudY / 100f));

            float scale = config.bobberTracker.timerHudFontSize / 10.0f;
            drawContext.getMatrices().scale(scale, scale);

            int scaledX = (int) (baseX / scale);
            int scaledY = (int) (baseY / scale);

            drawContext.drawText(textRenderer, text, scaledX - textRenderer.getWidth(text) / 2, scaledY, color, true);
        } finally {
            drawContext.getMatrices().popMatrix();
        }
    }
}
