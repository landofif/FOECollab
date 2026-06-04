package io.github.foecollab.handler;

import io.github.foecollab.FOMC.Types.FOMCItem;
import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.util.ItemStackHelper;
import io.github.foecollab.util.LocationNameHelper;
import io.github.foecollab.util.SimpleTagFont;
import io.github.foecollab.util.TextHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Trims and tidies FishOnMC item tooltips. Runs LAST in the tooltip callback so
 * it never shifts the fixed line indices the other tooltip handlers
 * ({@link PetTooltipHandler}, {@link FishingStatsHandler}, {@link ArmorHandler})
 * depend on.
 */
public class TooltipShortenerHandler {
    private static TooltipShortenerHandler INSTANCE = new TooltipShortenerHandler();
    private final FOEConfig config = FOEConfig.getConfig();

    private static final String ECOSYSTEM = TextHelper.smallCaps("ecosystem");
    private static final String LUCK = TextHelper.smallCaps("luck");
    private static final String SCALE = TextHelper.smallCaps("scale");

    // A "(...)" group that contains both a weight (kg) and a length (cm) unit, in any order.
    private static final Pattern WEIGHT_LENGTH = Pattern.compile(
            "\\s*\\((?=[^)]*(?:kg|ᴋɢ))(?=[^)]*(?:cm|ᴄᴍ))[^)]*\\)",
            Pattern.CASE_INSENSITIVE);
    // A "(max ...)" group on a pet stat line.
    private static final Pattern PET_MAX = Pattern.compile("\\s*\\(\\s*" + TextHelper.smallCaps("max") + "[^)]*\\)");

    public static TooltipShortenerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new TooltipShortenerHandler();
        }
        return INSTANCE;
    }

    public void cleanTooltip(List<Text> textList, ItemStack itemStack) {
        if (textList.isEmpty()) {
            return;
        }

        if (config.cleanerDisplay.shortenTooltips) {
            NbtCompound nbt = ItemStackHelper.getNbtView(itemStack);
            if (nbt != null) {
                // Fish have no "type" NBT key (detected by item id + lore); pets do.
                if (FOMCItem.isFish(itemStack)) {
                    shortenFish(textList, nbt);
                } else if (nbt.getString("type").orElse("").equals("pet")) {
                    shortenPet(textList, nbt.getInt("level").orElse(0));
                }
            }
        }

        if (config.cleanerDisplay.shortenLocationNames) {
            for (int i = 0; i < textList.size(); i++) {
                textList.set(i, LocationNameHelper.shorten(textList.get(i)));
            }
        }

        if (config.cleanerDisplay.simpleTags) {
            for (int i = 0; i < textList.size(); i++) {
                textList.set(i, SimpleTagFont.apply(textList.get(i)));
            }
        }
    }

    private void shortenFish(List<Text> textList, NbtCompound nbt) {
        // Scientific (latin) name — its own italic line; match by the NBT value.
        String scientific = nbt.getString("scientific").orElse("").trim();
        if (!scientific.isEmpty()) {
            String sci = scientific.toLowerCase();
            removeFirstLine(textList, line -> line.getString().toLowerCase().contains(sci));
        }

        // Ecosystem / native line.
        removeFirstLine(textList, line -> {
            String s = line.getString();
            return s.contains(ECOSYSTEM) || s.contains("Ecosystem");
        });

        // Weight (kg) / length (cm) bracket — strip just the parenthetical, keep the rest of the line.
        for (int i = 0; i < textList.size(); i++) {
            if (WEIGHT_LENGTH.matcher(textList.get(i).getString()).find()) {
                Text stripped = TextHelper.deleteFirstMatch(textList.get(i), WEIGHT_LENGTH);
                if (stripped.getString().isBlank()) {
                    textList.remove(i);
                } else {
                    textList.set(i, stripped);
                }
                break;
            }
        }
    }

    private void shortenPet(List<Text> textList, int level) {
        if (level < 100) {
            return;
        }
        // On a maxed pet the "max" potential equals the current value, so drop the
        // redundant "(max ...)" part from the luck / scale lines. Only ever strips a
        // parenthetical, never a whole line, so it is safe if the format differs.
        for (int i = 0; i < textList.size(); i++) {
            String s = textList.get(i).getString();
            if ((s.contains(LUCK) || s.contains(SCALE)) && PET_MAX.matcher(s).find()) {
                textList.set(i, TextHelper.deleteFirstMatch(textList.get(i), PET_MAX));
            }
        }
    }

    private static void removeFirstLine(List<Text> textList, Predicate<Text> predicate) {
        for (int i = 0; i < textList.size(); i++) {
            if (predicate.test(textList.get(i))) {
                textList.remove(i);
                return;
            }
        }
    }
}
