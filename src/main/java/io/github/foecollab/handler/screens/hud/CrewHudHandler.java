package io.github.foecollab.handler.screens.hud;

import io.github.foecollab.handler.CrewHandler;
import io.github.foecollab.util.TextHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CrewHudHandler {
    private static CrewHudHandler INSTANCE = new CrewHudHandler();

    public static CrewHudHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new CrewHudHandler();
        }
        return INSTANCE;
    }

    public Text assembleCrewNearbyText() {
        return TextHelper.concat(
                Text.literal("ᴄʀᴇᴡ ɴᴇᴀʀʙʏ: ").formatted(Formatting.GRAY),
                CrewHandler.instance().isCrewNearby ? Text.literal("✔").formatted(Formatting.DARK_GREEN) : Text.literal("✘").formatted(Formatting.DARK_RED)
        );
    }
}
