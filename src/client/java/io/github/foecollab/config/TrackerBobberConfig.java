package io.github.foecollab.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class TrackerBobberConfig {
    public static class BobberTracker {
        public boolean skyLightWarning = true;
        public boolean showWaitingTime = false;
        @ConfigEntry.ColorPicker
        public int timerColor = 0xFFFFFF;
        public boolean timerSecondsSuffix = true;
        public boolean timerAsHud = false;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 40)
        public int timerHudFontSize = 20;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int timerHudX = 50;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
        public int timerHudY = 60;
        public boolean showBait = true;
    }
}
