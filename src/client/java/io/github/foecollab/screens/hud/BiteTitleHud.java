package io.github.foecollab.screens.hud;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.config.HudAlignment;
import io.github.foecollab.handler.BiteTitleHandler;
import io.github.foecollab.handler.FishCatchHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/// Renders the user-configurable "bite" title (custom text + color + position). It shows the
/// instant a fish bites and disappears the instant the fish is hooked (the rod is reeled in) —
/// no fade either way — falling back to a short timeout if the bite is never acted on.
public class BiteTitleHud {
    /// How long the title lingers past its configured stay time when the fish is never hooked (ms).
    private static final long EXTRA_TIME = 750L;

    public void render(DrawContext drawContext, MinecraftClient client) {
        FOEConfig config = FOEConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        long showedAt = BiteTitleHandler.instance().showedAt;
        if (showedAt == 0L) {
            return;
        }

        // Vanish instantly the moment the fish is hooked: lastTimeUsedRod is stamped when the
        // bobber is reeled in, so if that happened at/after this bite the alert is no longer needed.
        if (FishCatchHandler.instance().lastTimeUsedRod >= showedAt) {
            return;
        }

        long elapsed = System.currentTimeMillis() - showedAt;
        long total = BiteTitleHandler.instance().time + EXTRA_TIME;
        if (elapsed < 0L || elapsed > total) {
            return;
        }

        String textString = config.biteTitle.text;
        if (textString == null || textString.isEmpty()) {
            return;
        }

        int color = 0xFF000000 | (config.biteTitle.textColor & 0xFFFFFF);
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
