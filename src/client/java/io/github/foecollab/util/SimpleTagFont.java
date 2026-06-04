package io.github.foecollab.util;

import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Set;

/**
 * Re-styles the FishOnMC rank / rarity / variant tag glyphs (the {@code \\uF0xx}
 * private-use codepoints) to use the mod's own {@code foecollab:tags} font, which
 * draws them as compact coloured squares with a letter.
 *
 * <p>Why a font swap instead of a resource pack: those glyphs are supplied by the
 * FishOnMC <i>server</i> resource pack, which loads at the highest priority and so
 * overrides both the mod's assets and any pack in the {@code resourcepacks} folder.
 * A separate font id that only the mod defines is the one thing the server pack
 * can't shadow, so we leave the codepoint in place and just change which font
 * renders it.
 */
public class SimpleTagFont {
    public static final Identifier FONT = Identifier.of("foecollab", "tags");
    /** The {@code foecollab:tags} font as a style source, reused by the global render hook. */
    public static final StyleSpriteSource FONT_SOURCE = new StyleSpriteSource.Font(FONT);

    // Every codepoint that has a glyph in assets/foecollab/font/tags.json.
    private static final Set<Integer> CHARS = Set.of(
            // Ranks
            0xF021, // owner
            0xF022, // admin
            0xF023, // manager
            0xF024, // staff
            0xF026, // designer
            0xF027, // builder
            0xF028, // admiral
            0xF029, // captain
            0xF030, // mariner
            0xF031, // sailor
            0xF032, // angler
            0xF088, // community manager
            // Rarities
            0xF033, // common
            0xF034, // rare
            0xF035, // epic
            0xF036, // legendary
            0xF037, // mythical
            0xF092, // special
            // Variants
            0xF041, // albino
            0xF042, // melanistic
            0xF043, // trophy
            0xF044, // fabled
            0xF098, // alternate
            0xF102, // spooky
            0xF179  // frozen
    );

    public static boolean isTagChar(int codepoint) {
        return CHARS.contains(codepoint);
    }

    public static Text apply(Text text) {
        if (text == null) {
            return null;
        }
        return TextHelper.setFontForChars(text, c -> CHARS.contains(c), FONT);
    }
}
