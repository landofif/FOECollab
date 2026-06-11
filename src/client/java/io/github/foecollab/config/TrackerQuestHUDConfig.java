package io.github.foecollab.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerQuestHUDConfig {
    public static class QuestTracker {
        public boolean showQuestHud = true;
        public boolean hideTitle = false;
        public boolean hideLocation = false;
        public boolean hideQuestNumbers = false;
        public boolean rightAlignment = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.Gui.Excluded
        public int hudX = 0;
        @ConfigEntry.Gui.Excluded
        public int hudY = 100;
    }

    public static class DailyQuestTracker {
        public boolean showDailyQuestHud = true;
        public boolean showNotification = true;
        public boolean rightAlignment = true;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int backgroundOpacity = 40;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int fontSize = 8;
        @ConfigEntry.Gui.Excluded
        public int hudX = 15;
        @ConfigEntry.Gui.Excluded
        public int hudY = 100;
    }
}
