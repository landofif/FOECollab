package io.github.foecollab.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TooltipShortenerConfig {
    public static class CleanerDisplay {
        @ConfigEntry.Gui.Tooltip
        public boolean shortenTooltips = true;

        @ConfigEntry.Gui.Tooltip
        public boolean shortenLocationNames = true;

        @ConfigEntry.Gui.Tooltip
        public boolean simpleTags = true;
    }
}
