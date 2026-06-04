package io.github.foecollab.mixin;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.LoadingHandler;
import io.github.foecollab.util.SimpleTagFont;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/// Global catch-all that makes the simplified FishOnMC tag squares ({@code \\uF0xx}) render with
/// the mod's {@code foecollab:tags} font <i>everywhere</i> text is drawn — chat, tab list, tooltips,
/// scoreboard, boss bar, titles, nametags, GUIs — without a per-surface hook for each.
///
/// Both {@code draw(Text)} and {@code draw(OrderedText)} funnel their layout through
/// {@code prepare(OrderedText, …)}, and outlined text (some nametags) through
/// {@code drawWithOutline(OrderedText, …)}; wrapping the {@link OrderedText} argument of those two
/// is enough to cover the whole render pipeline. The surfaces that also apply the swap at the
/// {@code Text} level (chat / tab / tooltips) keep doing so for exact width-based centering — the
/// extra swap here is idempotent.
@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @ModifyVariable(
            method = "prepare(Lnet/minecraft/text/OrderedText;FFIZZI)Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;",
            at = @At("HEAD"), argsOnly = true)
    private OrderedText foeSwapTagFontPrepare(OrderedText text) {
        return foeSwapTagFont(text);
    }

    @ModifyVariable(
            method = "drawWithOutline(Lnet/minecraft/text/OrderedText;FFIILorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"), argsOnly = true)
    private OrderedText foeSwapTagFontOutline(OrderedText text) {
        return foeSwapTagFont(text);
    }

    private static OrderedText foeSwapTagFont(OrderedText text) {
        if (text == null
                || !FOEConfig.getConfig().cleanerDisplay.simpleTags
                || !LoadingHandler.instance().isOnServer) {
            return text;
        }
        // Re-emit the same glyph stream, swapping only the font of tag codepoints.
        return visitor -> text.accept((index, style, codePoint) ->
                visitor.accept(index,
                        SimpleTagFont.isTagChar(codePoint) ? style.withFont(SimpleTagFont.FONT_SOURCE) : style,
                        codePoint));
    }
}
