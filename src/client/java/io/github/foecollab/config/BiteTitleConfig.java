package io.github.foecollab.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class BiteTitleConfig {
    public static class BiteTitle {
        @ConfigEntry.Gui.Tooltip
        public boolean enabled = false;
        @ConfigEntry.Gui.Tooltip
        public String text = "BITE!";
        @ConfigEntry.ColorPicker
        @ConfigEntry.Gui.Tooltip
        public int textColor = 0xFFFF55;
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        @ConfigEntry.Gui.Tooltip
        public HudAlignment alignment = HudAlignment.CENTER;
        @ConfigEntry.BoundedDiscrete(max = 40, min = 2)
        public int scale = 15;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudX = 50;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int hudY = 40;
    }
}
