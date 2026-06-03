package io.github.foecollab.screens.widget.movablebox;

import io.github.foecollab.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MovableBoxWidget extends ClickableWidget {
    public enum Alignment {
        LEFT,
        RIGHT,
        CENTER
    }

    private final int padding = 8;
    private final TextRenderer textRenderer;
    private final CallbackCoordinates callbackCoordinates;
    private final CallbackFontSize callbackFontSize;
    private final int lines;
    private final Text text;
    private final Alignment alignment;
    private final int maxLength;
    private final int minFontSize;
    private final int maxFontSize;

    private float scale;
    private int fontSize;
    private int minWidth;
    private int maxWidth;
    private int minHeight;
    private int maxHeight;

    private double deltaX = 0;
    private double deltaY = 0;
    private int originalX;
    private int originalY;
    private int xPercent;
    private int yPercent;

    public MovableBoxWidget(TextRenderer textRenderer, int xPercent, int yPercent, Alignment alignment, Text text, int fontSize, int minFontSize, int maxFontSize, int lines, int maxLength, CallbackCoordinates callbackCoordinates, CallbackFontSize callbackFontSize) {
        super(1, 1, 1, 1, null);

        this.text = text;
        this.alignment = alignment;
        this.textRenderer = textRenderer;
        this.callbackCoordinates = callbackCoordinates;
        this.callbackFontSize = callbackFontSize;
        this.fontSize = fontSize;
        this.minFontSize = minFontSize;
        this.maxFontSize = maxFontSize;
        this.lines = lines;
        this.maxLength = maxLength;
        this.xPercent = xPercent;
        this.yPercent = yPercent;

        recomputeGeometry();
    }

    /// Recompute the widget's scale, size and (from the stored percentages) its on-screen
    /// position. Called once on construction and again whenever the font size is scrolled.
    private void recomputeGeometry() {
        this.scale = fontSize / 10f;

        int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
        int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();

        this.setWidth((int) ((padding * 2 + this.maxLength) * this.scale));
        this.setHeight((int) ((padding * 2 + (textRenderer.fontHeight + 2f) * this.lines) * this.scale));

        this.minWidth = 0;
        this.maxWidth = Math.max(this.minWidth, screenWidth - this.width);
        this.minHeight = 24;
        this.maxHeight = Math.max(this.minHeight, screenHeight - this.height);

        float xCoord;
        float yCoord;
        if (alignment == Alignment.CENTER) {
            xCoord = xPercent / 100f * screenWidth - this.width / 2f;
            yCoord = yPercent / 100f * screenHeight - this.height / 2f;
        } else {
            boolean rightAlignment = alignment == Alignment.RIGHT;
            xCoord = ((rightAlignment ? 100 - xPercent : xPercent) / 100f * (this.maxWidth - this.minWidth + this.width)) - (rightAlignment ? this.width : 0);
            yCoord = yPercent / 100f * (this.maxHeight - this.minHeight) + this.minHeight;
        }

        this.setX(Math.clamp((long) xCoord, this.minWidth, this.maxWidth));
        this.setY(Math.clamp((long) yCoord, this.minHeight, this.maxHeight));
        this.originalX = getX();
        this.originalY = getY();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int alphaInt = (int) ((40 / 100f) * 255f) << 24;
        boolean rightAlignment = alignment == Alignment.RIGHT;

        context.fill(getX(), getY(), getX() + width, getY() + height, this.isHovered() ? alphaInt | 0xFFFFFF : alphaInt);

        if (alignment == Alignment.CENTER) {
            // Anchor line through the centre so it's clear where the element is pinned.
            context.fill(getX() + width / 2, getY(), getX() + width / 2 + 1, getY() + height, 0xFFFFFFFF);
        } else {
            context.fill(rightAlignment ? getX() + width - 1 : getX(), getY(), rightAlignment ? getX() + width : getX() + 1, getY() + height, 0xFFFFFFFF);
        }

        context.drawText(textRenderer, text, getX() + width / 2 - textRenderer.getWidth(text) / 2, getY() + height / 2 - textRenderer.fontHeight, 0xFFFFFFFF, true);
        context.drawText(textRenderer,
                TextHelper.concat(
                        Text.literal("X: ").formatted(Formatting.GRAY),
                        Text.literal(xPercent + "% ").formatted(Formatting.YELLOW),
                        Text.literal("Y: ").formatted(Formatting.GRAY),
                        Text.literal(yPercent + "%").formatted(Formatting.YELLOW)

                ),
                getX() + width / 2 - textRenderer.getWidth(text) / 2, getY() + height / 2 + 1, 0xFFFFFFFF, true);

        if (alignment == Alignment.CENTER) {
            return;
        }

        context.getMatrices().pushMatrix();
        try {
            context.getMatrices().translate(rightAlignment ? getX() + width : getX(), getY());
            context.getMatrices().rotate((float) Math.toRadians(rightAlignment ? 90.0 : -90.0));
            Text alignmentText = Text.literal(rightAlignment ? "ʀɪɢʜᴛ" : "ʟᴇꜰᴛ").formatted(Formatting.GRAY, Formatting.ITALIC);
            context.drawText(textRenderer, alignmentText, rightAlignment ? height / 2 - textRenderer.getWidth(alignmentText) / 2 : - height / 2 - textRenderer.getWidth(alignmentText) / 2, 0, 0xFFFFFFFF, true);

        } finally {
            context.getMatrices().popMatrix();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount == 0) {
            return false;
        }

        // Scroll up -> bigger font, scroll down -> smaller font.
        int newFontSize = (int) Math.clamp((long) (fontSize + (verticalAmount > 0 ? 1 : -1)), this.minFontSize, this.maxFontSize);
        if (newFontSize == fontSize) {
            return true;
        }

        this.fontSize = newFontSize;
        recomputeGeometry();
        callbackFontSize.onScroll(this.fontSize);
        return true;
    }

    @Override
    protected void onDrag(Click click, double deltaX, double deltaY) {
        super.onDrag(click, deltaX, deltaY);

        this.deltaX += deltaX;
        this.deltaY += deltaY;

        this.setX(Math.clamp(originalX + (int) this.deltaX, this.minWidth, this.maxWidth));
        this.setY(Math.clamp(originalY + (int) this.deltaY, this.minHeight, this.maxHeight));
    }

    @Override
    public void onRelease(Click click) {
        super.onRelease(click);
        this.originalX = getX();
        this.originalY = getY();
        this.deltaX = 0;
        this.deltaY = 0;

        float percentageX;
        float percentageY;
        if (alignment == Alignment.CENTER) {
            int screenWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();
            int screenHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
            percentageX = (getX() + width / 2f) / screenWidth * 100;
            percentageY = (getY() + height / 2f) / screenHeight * 100;
        } else {
            boolean rightAlignment = alignment == Alignment.RIGHT;
            percentageX = rightAlignment ? 100 - ((float) (getX() - minWidth + width) / (maxWidth + width - minWidth) * 100) : (float) (getX() - minWidth) / (maxWidth + width - minWidth) * 100;
            percentageY = (float) (getY() - minHeight) / (maxHeight - minHeight) * 100;
        }

        this.xPercent = (int) Math.clamp((long) percentageX, 0, 100);
        this.yPercent = (int) Math.clamp((long) percentageY, 0, 100);

        // Send percentages back
        callbackCoordinates.onRelease(this.xPercent, this.yPercent);
    }
}
