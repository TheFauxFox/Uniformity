package dev.paw.uniformity.config;

import dev.paw.uniformity.utils.Color;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@Config(name = "uniformity")
@Config.Gui.Background(value="cloth-config2:transparent")
public class ModConfig implements ConfigData {
    public boolean fpsToggle = true;
    public boolean displayInfoToggle = false;
    public boolean toolBreakToggle = true;
    public boolean chatHistoryToggle = true;
    public boolean beeInfoToggle = true;
    public boolean numericPingToggle = true;
    public boolean stepToggle = false;
    public boolean darkLoadingScreenToggle = true;
    public boolean horseStatsToggle = true;
    public boolean fullbrightToggle = false;
    public boolean autoRecconnectToggle = false;
    public boolean noChatReportToggle = true;
    public boolean disableTutorialToast = true;
    /* FREECAM MUST ALWAYS BE DISABLED */
    @ConfigEntry.Gui.Excluded
    public boolean freecamToggle = false;
    public boolean creativeToolsToggle = true;
    public boolean entityOutlineToggle = false;
    public boolean noFogToggle = true;
    public boolean noHurtAngleToggle = true;
    public boolean lowFireToggle = true;
    /* RECYCLER MUST ALWAYS BE DISABLED */
    @ConfigEntry.Gui.Excluded
    public boolean recyclerToggle = false;

    @ConfigEntry.Gui.CollapsibleObject
    public NumericPing numericPing = new NumericPing();

    @ConfigEntry.Gui.CollapsibleObject
    public DarkLoadingScreen darkLoadingScreen = new DarkLoadingScreen();

    @ConfigEntry.Gui.CollapsibleObject
    public AntiToolBreak antiToolBreak = new AntiToolBreak();

    @ConfigEntry.Gui.CollapsibleObject
    public Freecam freecam = new Freecam();

    @ConfigEntry.Gui.CollapsibleObject
    public AutoReconnect autoReconnect = new AutoReconnect();

    @ConfigEntry.Gui.CollapsibleObject
    public EntityOutline entityOutline = new EntityOutline();

    @ConfigEntry.Gui.CollapsibleObject
    public DisplayInfo displayInfo = new DisplayInfo();

    public static class AntiToolBreak {
        public int duraValue = 5;
        public boolean playSound = false;
        public boolean showWarning = true;
        public boolean swords = true;
        public boolean pickaxes = true;
        public boolean shovels = true;
        public boolean axes = true;
        public boolean bows = true;
        public boolean tridents = true;
        public boolean hoes = true;
        public boolean shears = false;
    }

    public static class NumericPing {
        public int pingWidthOverride = 45;
        public String lowPingColor = Color.GREEN.asHex;
        public String lowMidPingColor = Color.YELLOW.asHex;
        public String midPingColor = Color.GOLD.asHex;
        public String highPingColor = Color.RED.asHex;
        public String extremePingColor = Color.RED.asHex;
    }

    public static class DarkLoadingScreen {
        public String loadingScreenHex = Color.rgb(25, 25, 25).asHex;
    }

    public static class Freecam {
        public boolean highlightPlayer = false;
        public boolean toggleOnDamage = true;
    }

    public static class AutoReconnect {
        public boolean timeJitter = false;
        public int reconnectSeconds = 5;
        public int timeJitterSeconds = 2;
    }

    public static class EntityOutline {
        public boolean animalHighlight = true;
        public String animalHighlightHex = Color.GREEN.asHex;
        public boolean mobHighlight = true;
        public String mobHighlightHex = Color.RED.asHex;
        public boolean angerableHighlight = true;
        public String angerableHighlightHex = Color.YELLOW.asHex;
        public boolean playerHighlight = true;
        public String playerHighlightHex = Color.DARK_PURPLE.asHex;
    }

    public static class DisplayInfo {
        public boolean coords = true;
        public boolean netherCoords = true;
        public boolean speed = true;
        public boolean biome = true;
        public boolean lightLevel = true;
        public boolean blockLevel = true;
        public boolean showRecycler = true;
    }
}
