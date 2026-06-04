package io.github.foecollab.screens.movehud;

import io.github.foecollab.config.FOEConfig;
import io.github.foecollab.handler.TitleHandler;
import io.github.foecollab.handler.screens.hud.*;
import io.github.foecollab.screens.widget.movablebox.MovableBoxWidget;
import io.github.foecollab.util.TextHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class MoveHudScreen extends Screen {
    private final Screen parent;
    FOEConfig config = FOEConfig.getConfig();

    public MoveHudScreen(MinecraftClient minecraftClient, Screen parent) {
        super(Text.literal("Move Hud Elements"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        this.renderWidgets();
    }

    private void renderWidgets() {
        List<MovableBoxWidget> movableBoxWidgetList = new ArrayList<>();

        // Fish Tracker HUD
        List<Text> fishTrackerTextList = FishTrackerHudHandler.instance().assembleFishText();
        int fishTrackerX = config.fishTracker.hudX;
        int fishTrackerY = config.fishTracker.hudY;
        MovableBoxWidget.Alignment fishTrackerAlignment = config.fishTracker.rightAlignment ? MovableBoxWidget.Alignment.RIGHT : MovableBoxWidget.Alignment.LEFT;
        int fishTrackerFontSize = config.fishTracker.fontSize;
        int fishTrackerMaxLength = fishTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, fishTrackerX, fishTrackerY, fishTrackerAlignment, Text.literal("Fish Tracker"), fishTrackerFontSize, 2, 20, fishTrackerTextList.size(), fishTrackerMaxLength, (xPercent, yPercent) -> {
            config.fishTracker.hudX = xPercent;
            config.fishTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.fishTracker.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Contest Tracker HUD
        List<Text> contestTrackerTextList = ContestHudHandler.instance().assembleContestText();
        int contestTrackerX = config.contestTracker.hudX;
        int contestTrackerY = config.contestTracker.hudY;
        MovableBoxWidget.Alignment contestTrackerAlignment = config.contestTracker.rightAlignment ? MovableBoxWidget.Alignment.RIGHT : MovableBoxWidget.Alignment.LEFT;
        int contestTrackerFontSize = config.contestTracker.fontSize;
        int contestTrackerMaxLength = contestTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, contestTrackerX, contestTrackerY, contestTrackerAlignment, Text.literal("Contest Tracker"), contestTrackerFontSize, 2, 20, contestTrackerTextList.size(), contestTrackerMaxLength, (xPercent, yPercent) -> {
            config.contestTracker.hudX = xPercent;
            config.contestTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.contestTracker.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Pet Tracker HUD
        List<Text> petTrackerTextList = PetEquipHudHandler.instance().assemblePetText();
        int petTrackerX = config.petEquipTracker.activePetHUDOptions.hudX;
        int petTrackerY = config.petEquipTracker.activePetHUDOptions.hudY;
        MovableBoxWidget.Alignment petTrackerAlignment = config.petEquipTracker.activePetHUDOptions.rightAlignment ? MovableBoxWidget.Alignment.RIGHT : MovableBoxWidget.Alignment.LEFT;
        int petTrackerFontSize = config.petEquipTracker.activePetHUDOptions.fontSize;
        int petTrackerMaxLength = petTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, petTrackerX, petTrackerY, petTrackerAlignment, Text.literal("Pet Tracker"), petTrackerFontSize, 1, 20, petTrackerTextList.size(), petTrackerMaxLength + 16 + 8, (xPercent, yPercent) -> {
            config.petEquipTracker.activePetHUDOptions.hudX = xPercent;
            config.petEquipTracker.activePetHUDOptions.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.petEquipTracker.activePetHUDOptions.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Quest Tracker HUD
        List<Text> questTrackerTextList = QuestTrackerHudHandler.instance().assembleQuestText();
        int questTrackerX = config.questTracker.hudX;
        int questTrackerY = config.questTracker.hudY;
        MovableBoxWidget.Alignment questTrackerAlignment = config.questTracker.rightAlignment ? MovableBoxWidget.Alignment.RIGHT : MovableBoxWidget.Alignment.LEFT;
        int questTrackerFontSize = config.questTracker.fontSize;
        int questTrackerMaxLength = questTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, questTrackerX, questTrackerY, questTrackerAlignment, Text.literal("Quest Tracker"), questTrackerFontSize, 2, 20, questTrackerTextList.size(), questTrackerMaxLength, (xPercent, yPercent) -> {
            config.questTracker.hudX = xPercent;
            config.questTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.questTracker.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Daily Quest Tracker HUD
        List<Text> dailyQuestTrackerTextList = DailyQuestTrackerHudHandler.instance().assembleQuestText();
        int dailyQuestTrackerX = config.dailyQuestTracker.hudX;
        int dailyQuestTrackerY = config.dailyQuestTracker.hudY;
        MovableBoxWidget.Alignment dailyQuestTrackerAlignment = config.dailyQuestTracker.rightAlignment ? MovableBoxWidget.Alignment.RIGHT : MovableBoxWidget.Alignment.LEFT;
        int dailyQuestTrackerFontSize = config.dailyQuestTracker.fontSize;
        int dailyQuestTrackerMaxLength = dailyQuestTrackerTextList.stream().map(textRenderer::getWidth).max(Integer::compareTo).orElse(0);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, dailyQuestTrackerX, dailyQuestTrackerY, dailyQuestTrackerAlignment, Text.literal("Daily Quest Tracker"), dailyQuestTrackerFontSize, 2, 20, dailyQuestTrackerTextList.size(), dailyQuestTrackerMaxLength, (xPercent, yPercent) -> {
            config.dailyQuestTracker.hudX = xPercent;
            config.dailyQuestTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.dailyQuestTracker.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Bait HUD
        Text baitText = BaitHudHandler.instance().assembleBaitText();
        int baitX = config.baitTracker.hudX;
        int baitY = config.baitTracker.hudY;
        int baitFontSize = config.baitTracker.fontSize;
        int baitMaxLength = textRenderer.getWidth(baitText) + 16 + 8;
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, baitX, baitY, MovableBoxWidget.Alignment.CENTER, Text.literal("Bait"), baitFontSize, 1, 20, 1, baitMaxLength, (xPercent, yPercent) -> {
            config.baitTracker.hudX = xPercent;
            config.baitTracker.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.baitTracker.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Title Popup HUD (the "new title system" that shows the fish you caught)
        int titleX = config.titlePopup.hudX;
        int titleY = config.titlePopup.hudY;
        int titleFontSize = config.titlePopup.scale;
        Text titleLabel = Text.literal("Title Popup");
        int titleMaxLength = textRenderer.getWidth(titleLabel);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, titleX, titleY, MovableBoxWidget.Alignment.CENTER, titleLabel, titleFontSize, 2, 20, 1, titleMaxLength, (xPercent, yPercent) -> {
            config.titlePopup.hudX = xPercent;
            config.titlePopup.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.titlePopup.scale = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Notification HUD
        MovableBoxWidget.Alignment notificationAlignment = switch (config.notifications.alignment) {
            case LEFT -> MovableBoxWidget.Alignment.LEFT;
            case RIGHT -> MovableBoxWidget.Alignment.RIGHT;
            case CENTER -> MovableBoxWidget.Alignment.CENTER;
        };
        Text notificationLabel = Text.literal("Notifications");
        int notificationX = config.notifications.hudX;
        int notificationY = config.notifications.hudY;
        int notificationFontSize = config.notifications.fontSize;
        int notificationMaxLength = textRenderer.getWidth(notificationLabel);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, notificationX, notificationY, notificationAlignment, notificationLabel, notificationFontSize, 1, 20, 1, notificationMaxLength, (xPercent, yPercent) -> {
            config.notifications.hudX = xPercent;
            config.notifications.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.notifications.fontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Bite Title HUD (the custom "bite" title, separate from the caught-fish popup)
        int biteTitleX = config.biteTitle.hudX;
        int biteTitleY = config.biteTitle.hudY;
        int biteTitleFontSize = config.biteTitle.scale;
        Text biteTitleLabel = Text.literal(config.biteTitle.text == null || config.biteTitle.text.isEmpty() ? "Bite Title" : config.biteTitle.text);
        int biteTitleMaxLength = textRenderer.getWidth(biteTitleLabel);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, biteTitleX, biteTitleY, MovableBoxWidget.Alignment.CENTER, biteTitleLabel, biteTitleFontSize, 2, 40, 1, biteTitleMaxLength, (xPercent, yPercent) -> {
            config.biteTitle.hudX = xPercent;
            config.biteTitle.hudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.biteTitle.scale = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        // Bobber Timer HUD (the waiting time, when shown as a HUD element)
        int bobberTimerX = config.bobberTracker.timerHudX;
        int bobberTimerY = config.bobberTracker.timerHudY;
        int bobberTimerFontSize = config.bobberTracker.timerHudFontSize;
        Text bobberTimerLabel = Text.literal("Bobber Timer");
        int bobberTimerMaxLength = textRenderer.getWidth(bobberTimerLabel);
        movableBoxWidgetList.add(new MovableBoxWidget(textRenderer, bobberTimerX, bobberTimerY, MovableBoxWidget.Alignment.CENTER, bobberTimerLabel, bobberTimerFontSize, 1, 40, 1, bobberTimerMaxLength, (xPercent, yPercent) -> {
            config.bobberTracker.timerHudX = xPercent;
            config.bobberTracker.timerHudY = yPercent;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }, fontSize -> {
            config.bobberTracker.timerHudFontSize = fontSize;
            AutoConfig.getConfigHolder(FOEConfig.class).save();
        }));

        movableBoxWidgetList.forEach(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int alphaInt = (int) ((40 / 100f) * 255f) << 24;
        context.fill(0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), 24, alphaInt);
        Text barHudText = TextHelper.concat(
                Text.literal("Bar HUD "), Text.literal("Not movable").formatted(Formatting.GRAY, Formatting.ITALIC),
                Text.literal("   •   ").formatted(Formatting.DARK_GRAY),
                Text.literal("Scroll to resize").formatted(Formatting.GRAY, Formatting.ITALIC));
        context.drawText(textRenderer, barHudText, MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - textRenderer.getWidth(barHudText) / 2, 8, 0xFFFFFFFF, true);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
