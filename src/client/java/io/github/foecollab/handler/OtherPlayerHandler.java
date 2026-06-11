package io.github.foecollab.handler;

import io.github.foecollab.FOMC.Types.Defaults;
import io.github.foecollab.util.TextHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;

import java.util.ArrayList;
import java.util.List;

public class OtherPlayerHandler {
    private static OtherPlayerHandler INSTANCE = new OtherPlayerHandler();

    // Private-use glyph the server stamps onto FoE-relevant text-display nameplates.
    private static final String NAMEPLATE_MARKER = "";

    public PlayerListEntry highlightedPlayer = null;
    public long highlightStartTime = 0L;
    public boolean isHighlighted = false;

    private final List<Integer> hiddenNamePlates = new ArrayList<>();

    public static OtherPlayerHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new OtherPlayerHandler();
        }
        return INSTANCE;
    }

    public void tick(MinecraftClient minecraftClient) {
        if(System.currentTimeMillis() - highlightStartTime <= 300000L && highlightedPlayer != null) {
            this.isHighlighted = true;
        } else if (this.isHighlighted && highlightedPlayer != null) {
            highlightedPlayer = null;
            this.isHighlighted = false;
        }
    }

    public void tickEntities(Entity entity, MinecraftClient minecraftClient) {
        // Restore previously dimmed nameplates once the HUD is shown again.
        if (!minecraftClient.options.hudHidden && !hiddenNamePlates.isEmpty() && minecraftClient.world != null) {
            hiddenNamePlates.forEach(id -> {
                Entity namePlate = minecraftClient.world.getEntityById(id);
                if (namePlate != null) {
                    ((DisplayEntity.TextDisplayEntity) namePlate).setTextOpacity((byte) -1);
                }
            });
            hiddenNamePlates.clear();
        }

        if (!(entity instanceof DisplayEntity.TextDisplayEntity textDisplayEntity)) {
            return;
        }

        // Resolve the nameplate text once. getText().getString() walks the text tree and
        // allocates, and a crowd has many nameplates; the old code rebuilt this several
        // times per entity per tick (once per check plus once per dev in the stream below).
        String nameplateText = textDisplayEntity.getText().getString();
        if (!nameplateText.contains(NAMEPLATE_MARKER)) {
            return;
        }

        // Dim FoE nameplates while the HUD is hidden (e.g. for screenshots).
        if (minecraftClient.options.hudHidden) {
            textDisplayEntity.setTextOpacity((byte) 24);
            if (!hiddenNamePlates.contains(entity.getId())) {
                hiddenNamePlates.add(entity.getId());
            }
        }

        // Re-tag FoE dev nameplates.
        Defaults.FoEDevType senderDev = Defaults.foeDevs.values().stream()
                .filter(foEDevType -> nameplateText.contains(foEDevType.name))
                .findFirst()
                .orElse(null);

        if (senderDev != null) {
            String jsonText = TextHelper.textToJson(textDisplayEntity.getText());
            jsonText = TextHelper.replaceToFoE(jsonText, senderDev.usePurpleTag);
            if (!senderDev.usePurpleTag) {
                jsonText = jsonText.replace("B05BF9", "00AF0E");
            }
            textDisplayEntity.setText(TextHelper.jsonToText(jsonText));
        }
    }
}
