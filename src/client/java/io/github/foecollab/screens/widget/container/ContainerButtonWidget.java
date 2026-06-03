package io.github.foecollab.screens.widget.container;

import io.github.foecollab.FOECollab;
import io.github.foecollab.common.ButtonColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ContainerButtonWidget extends ClickableWidget {
    private final Identifier buttonTexture = Identifier.of(FOECollab.MOD_ID, "containers/button");
    private final Identifier buttonHoverTexture = Identifier.of(FOECollab.MOD_ID, "containers/button_hover");
    private final TextRenderer textRenderer;
    private final ClickCallback clickCallback;


    public ContainerButtonWidget(int x, int y, Text message, @Nullable Tooltip tooltip, ClickCallback clickCallback) {
        super(x, y, 22, 22, message);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.setTooltip(tooltip);
        this.clickCallback = clickCallback;
    }

    public ContainerButtonWidget(int x, int y, Text message, @Nullable Tooltip tooltip) {
        super(x, y, 22, 22, message);
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.setTooltip(tooltip);
        this.clickCallback = null;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().pushMatrix();
        try {
            int tint = ButtonColor.tint();
            if(this.isHovered() || this.isFocused()) context.drawGuiTexture( RenderPipelines.GUI_TEXTURED, buttonHoverTexture, this.getX(), this.getY(), this.width, this.height, tint);
            else context.drawGuiTexture( RenderPipelines.GUI_TEXTURED, buttonTexture, this.getX(), this.getY(), this.width, this.height, tint);

            context.drawText(textRenderer, this.getMessage(), this.getX() + this.width / 2 - this.textRenderer.getWidth(this.getMessage())/ 2, this.getY() + this.height / 2 - this.textRenderer.fontHeight / 2, 0xFFFFFFFF, true);
        } finally {
            context.getMatrices().popMatrix();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void onClick(Click click, boolean doubled) {
        super.onClick(click, doubled);
        if(clickCallback != null) {
            this.clickCallback.onClick(this);
            this.setMessage(Text.literal("...").formatted(Formatting.GRAY));
        }
    }

    public interface ClickCallback {
        void onClick(ContainerButtonWidget iconButtonWidget);
    }
}
