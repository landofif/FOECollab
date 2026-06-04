package io.github.foecollab.screens.hud;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.config.HudAlignment;
import io.github.foecollab.handler.BiteTitleHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/// Renders the user-configurable "bite" title (custom text + color + position), faded in and out
/// independently of the caught-fish popup drawn by {@link TitleHud}.
public class BiteTitleHud {
    private static final long FADE_IN = 250L;
    private static final long FADE_OUT = 500L;

    public void render(DrawContext drawContext, MinecraftClient client) {
        FOEConfig config = FOEConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        long showedAt = BiteTitleHandler.instance().showedAt;
        if (showedAt == 0L) {
            return;
        }

        long elapsed = System.currentTimeMillis() - showedAt;
        long stay = BiteTitleHandler.instance().time;
        long total = FADE_IN + stay + FADE_OUT;
        if (elapsed < 0L || elapsed > total) {
            return;
        }

        String textString = config.biteTitle.text;
        if (textString == null || textString.isEmpty()) {
            return;
        }

        float alpha;
        if (elapsed < FADE_IN) {
            alpha = elapsed / (float) FADE_IN;
        } else if (elapsed < FADE_IN + stay) {
            alpha = 1.0f;
        } else {
            alpha = 1.0f - (elapsed - FADE_IN - stay) / (float) FADE_OUT;
        }
        alpha = Math.clamp(alpha, 0f, 1f);

        int color = ((int) (alpha * 255f) << 24) | (config.biteTitle.textColor & 0xFFFFFF);
        Text text = Text.literal(textString);

        drawContext.getMatrices().pushMatrix();
        try {
            int screenWidth = client.getWindow().getScaledWidth();
            int screenHeight = client.getWindow().getScaledHeight();

            // Calculate base positions relative to screen size (movable via the HUD editor)
            int baseX = (int) (screenWidth * (config.biteTitle.hudX / 100f));
            int baseY = (int) (screenHeight * (config.biteTitle.hudY / 100f));

            // Scaling setup (matches the caught-fish title popup)
            float fontSize = config.biteTitle.scale * 2f;
            float scale = fontSize / 10.0f;
            drawContext.getMatrices().scale(scale, scale);

            int scaledX = (int) (baseX / scale);
            int scaledY = (int) (baseY / scale);

            drawContext.drawText(textRenderer, text, alignX(config.biteTitle.alignment, scaledX, textRenderer.getWidth(text)), scaledY, color, true);
        } finally {
            drawContext.getMatrices().popMatrix();
        }
    }

    /// X coordinate for a line of the given width drawn from anchor {@code anchorX}, per alignment.
    private static int alignX(HudAlignment alignment, int anchorX, int width) {
        return switch (alignment) {
            case LEFT -> anchorX;
            case RIGHT -> anchorX - width;
            case CENTER -> anchorX - width / 2;
        };
    }
}
