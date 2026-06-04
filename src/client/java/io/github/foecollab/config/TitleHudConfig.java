package io.github.foecollab.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TitleHudConfig {
    public static class TitlePopup {
        @ConfigEntry.Gui.Tooltip
        public boolean useNewTitleSystem = true;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        @ConfigEntry.Gui.Tooltip
        public HudAlignment alignment = HudAlignment.CENTER;
        @ConfigEntry.BoundedDiscrete(max = 20, min = 2)
        public int scale = 10;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 50;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 50;
    }
}
