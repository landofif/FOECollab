package io.github.foecollab.handler;

/// Tracks when the custom "bite" title was last triggered so {@link io.github.foecollab.screens.hud.BiteTitleHud}
/// can fade it in and out. Kept separate from {@link TitleHandler} (the caught-fish popup) so the
/// two titles have independent text, color and position.
public class BiteTitleHandler {
    private static BiteTitleHandler INSTANCE = new BiteTitleHandler();

    public long showedAt = 0L;
    /// How long the title stays fully opaque (ms), before the fade-out.
    public long time = 1000L;

    public static BiteTitleHandler instance() {
        if (INSTANCE == null) {
            INSTANCE = new BiteTitleHandler();
        }
        return INSTANCE;
    }

    public void trigger() {
        this.showedAt = System.currentTimeMillis();
    }
}
