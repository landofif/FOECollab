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
        if(
                minecraftClient.options.hudHidden
                && entity instanceof DisplayEntity.TextDisplayEntity textDisplayEntity
                && textDisplayEntity.getText().getString().contains("")
        ) {
            textDisplayEntity.setTextOpacity((byte) 24);

            if(!hiddenNamePlates.contains(entity.getId())) {
                hiddenNamePlates.add(entity.getId());
            }

        } else if(!minecraftClient.options.hudHidden
                && !hiddenNamePlates.isEmpty()
                && minecraftClient.world != null) {
            hiddenNamePlates.forEach(id -> {
                Entity namePlate = minecraftClient.world.getEntityById(id);
                if(namePlate != null) {
                    ((DisplayEntity.TextDisplayEntity) namePlate).setTextOpacity((byte) -1);
                }
            });
            hiddenNamePlates.clear();
        }

        // Nameplate FoE
        if(entity instanceof DisplayEntity.TextDisplayEntity textDisplayEntity
                && textDisplayEntity.getText().getString().contains("")
        ) {
            Defaults.FoEDevType senderDev = Defaults.foeDevs.values().stream()
                    .filter(foEDevType -> textDisplayEntity.getText().getString().contains(foEDevType.name))
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
}
