package io.github.foecollab.screens.widget.container;

import io.github.foecollab.FOECollab;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ContainerHeaderWidget extends ClickableWidget {
    private final Identifier headerTexture = Identifier.of(FOECollab.MOD_ID, "containers/panel_header");

    public ContainerHeaderWidget(int x, int y, Text message) {
        super(x, y, 174, 28, message);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawGuiTexture( RenderPipelines.GUI_TEXTURED, headerTexture, this.getX(), this.getY(), this.width, this.height);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    // Purely decorative background panel — kept out of ParentElement#hoveredElement so clicks pass
    // through to the widgets drawn on top of it (1.21.11 dispatches a click to only one element).
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }
}
