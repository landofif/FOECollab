package io.github.foecollab.screens.hud;

import io.github.foecollab.common.FlairDecor;
import io.github.foecollab.common.HudFont;
import io.github.foecollab.common.Theming;
import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.DailyQuestHandler;
import io.github.foecollab.handler.ThemingHandler;
import io.github.foecollab.handler.screens.hud.DailyQuestTrackerHudHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// "Borrowed" from QuestHud.java
public class DailyQuestHud {
    public void render(DrawContext drawContext, MinecraftClient client) {

        FOEConfig config = FOEConfig.getConfig();
        TextRenderer textRenderer = client.textRenderer;

        // Assemble all text lines
        List<Text> textList = HudFont.recolorAll(DailyQuestTrackerHudHandler.instance().assembleQuestText());

        drawContext.getMatrices().pushMatrix();
        try {
            if(!DailyQuestHandler.instance().quests.isEmpty()) {
                // Get screen size
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();

                boolean rightAlignment = config.dailyQuestTracker.rightAlignment;

                // Convert percentage config values to screen coordinates
                float xPercent = rightAlignment ?  1f - (config.dailyQuestTracker.hudX / 100f) : config.dailyQuestTracker.hudX / 100f;
                float yPercent = config.dailyQuestTracker.hudY / 100f;
                // Calculate base positions relative to screen size
                int baseX = (int) (screenWidth * xPercent);
                int baseY = (int) (screenHeight * yPercent);

                // Scaling setup
                int fontSize = config.dailyQuestTracker.fontSize;
                float scale = fontSize / 10.0f;
                drawContext.getMatrices().scale(scale, scale);

                // Alpha
                int alphaInt = (int) ((config.dailyQuestTracker.backgroundOpacity / 100f) * 255f) << 24;

                int lineSpacing = 2;
                int lineHeight = (int) (textRenderer.fontHeight + (lineSpacing / scale));
                int scaledX = (int) (baseX / scale);
                int scaledY = (int) (baseY / scale);
                int padding = 8;
                AtomicInteger count = new AtomicInteger(0);

                int maxLength = textList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
                int heightClampTranslation = (int) ((padding * 2 + textList.size() * lineHeight) * yPercent);
                heightClampTranslation -= (int) ((padding * 3) * (1 - yPercent));

                if (rightAlignment) {
                    drawContext.fill(scaledX, scaledY - heightClampTranslation, scaledX - maxLength - padding * 2, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);
                } else {
                    drawContext.fill(scaledX, scaledY - heightClampTranslation, scaledX + maxLength + padding * 2, scaledY + ((textList.size() - 1) * lineHeight) + padding * 3 - heightClampTranslation, alphaInt);
                }

                // Theming
                FlairDecor flairDecor = ThemingHandler.instance().flairDecorQuest;
                int rightAlignmentOffset = (rightAlignment ? padding * 2 + maxLength : 0);

                if(config.theme.themeType != Theming.ThemeType.OFF) {
                    Theming theme = ThemingHandler.instance().currentTheme;
                    int colorOverlay = config.theme.colorOverlay;
                    int themeTextColor = ThemingHandler.instance().currentThemeType.TEXT_COLOR;
                    int alphaOverlay = (int) ((config.theme.opacity / 100f) * 255f) << 24;

                    // Corners
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_TOP_LEFT, scaledX - padding - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_TOP_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_BOTTOM_LEFT, scaledX - padding - rightAlignmentOffset, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_BOTTOM_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);

                    // Sides
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_LEFT, scaledX - padding - rightAlignmentOffset, scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset, scaledY + padding - heightClampTranslation, 16, ((textList.size() - 1) * lineHeight) + padding, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_TOP, scaledX + padding - rightAlignmentOffset, scaledY - padding - heightClampTranslation, maxLength, 16, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_BOTTOM, scaledX + padding - rightAlignmentOffset, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation, maxLength, 16, alphaOverlay | colorOverlay);

                    // Title
                    Text title = DailyQuestTrackerHudHandler.instance().getTitle().copy().withColor(ThemingHandler.instance().currentThemeType.TEXT_COLOR);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_TEXT_LEFT, scaledX + (maxLength + padding * 2) / 2 - textRenderer.getWidth(title) / 2 - 16 - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_TEXT_MIDDLE, scaledX  + (maxLength + padding * 2) / 2 - textRenderer.getWidth(title) / 2 - rightAlignmentOffset, scaledY - padding - heightClampTranslation, textRenderer.getWidth(title), 16, alphaOverlay | colorOverlay);
                    drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   theme.GUI_TEXT_RIGHT, scaledX + (maxLength + padding * 2) / 2 + textRenderer.getWidth(title) / 2 - rightAlignmentOffset, scaledY - padding - heightClampTranslation, 16, 16, alphaOverlay | colorOverlay);
                    drawContext.drawText(textRenderer, title, scaledX + (maxLength + padding * 2) / 2 - textRenderer.getWidth(title) / 2 - rightAlignmentOffset, scaledY - textRenderer.fontHeight / 2 - heightClampTranslation - 1, themeTextColor, false);
                }

                // Flair
                drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   flairDecor.GUI_FLAIR_TOP_LEFT, scaledX - padding - rightAlignmentOffset - 24, scaledY - padding - heightClampTranslation - 24, 64, 64);
                drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   flairDecor.GUI_FLAIR_TOP_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset - 24, scaledY - padding - heightClampTranslation - 24, 64, 64);
                drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   flairDecor.GUI_FLAIR_BOTTOM_LEFT, scaledX - padding - rightAlignmentOffset - 24, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);
                drawContext.drawGuiTexture(RenderPipelines.GUI_TEXTURED,   flairDecor.GUI_FLAIR_BOTTOM_RIGHT, scaledX + padding + maxLength - rightAlignmentOffset - 24, scaledY + padding * 2 + ((textList.size() - 1) * lineHeight) - heightClampTranslation - 24, 64, 64);

                int finalHeightClampTranslation = heightClampTranslation;
                textList.forEach(text -> drawContext.drawText(textRenderer, text, rightAlignment ? scaledX - textRenderer.getWidth(text) - padding: scaledX + padding, scaledY + (count.getAndIncrement() * lineHeight) + padding - finalHeightClampTranslation, 0xFFFFFFFF, true));
            }
        } finally {
            drawContext.getMatrices().popMatrix();
        }
    }
}

