package util;

import java.awt.Color;

/**
 * Shared colors for the retro dungeon interface.
 */
public final class ColorPalette {
    public static final Color BACKGROUND_DARK = Color.decode("#0F1729");
    public static final Color PANEL_BG = Color.decode("#1A2238");
    public static final Color PANEL_BG_LIGHT = Color.decode("#24304F");
    public static final Color BORDER_GOLD = Color.decode("#D4A95C");
    public static final Color TEXT_PRIMARY = Color.decode("#E8E6D9");
    public static final Color TEXT_DIM = Color.decode("#8B8B8B");
    public static final Color HP_GREEN = Color.decode("#5CD658");
    public static final Color HP_YELLOW = Color.decode("#F5D547");
    public static final Color HP_RED = Color.decode("#E84545");
    public static final Color MANA_BLUE = Color.decode("#4A90E2");
    public static final Color STAMINA_YELLOW = Color.decode("#F0A030");
    public static final Color SHADOW = new Color(0, 0, 0, 120);
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static final String FONT_FAMILY = "Consolas";

    private ColorPalette() {
    }
}
