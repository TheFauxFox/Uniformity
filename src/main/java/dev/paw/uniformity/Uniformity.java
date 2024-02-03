package dev.paw.uniformity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.paw.uniformity.commands.ReCyclerCmd;
import dev.paw.uniformity.config.ModConfig;
import dev.paw.uniformity.modules.*;
import dev.paw.uniformity.modules.Module;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.tutorial.TutorialStep;
import net.minecraft.item.*;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Uniformity implements ClientModInitializer {
    public static Logger logger = LoggerFactory.getLogger("Uniformity");
    public static boolean isInit = false;
    public static ModConfig config;
    private final ConfigHolder<ModConfig> configHolder;
    public static final ArrayList<Module> modules = new ArrayList<>();
    private boolean wasKeybindSave = false;
    public static final KeyBinding zoomKeybind = new KeyBinding("dev.paw.uniformity.keybind.zoom_key", GLFW.GLFW_KEY_C, "dev.paw.uniformity.name");
    public static final KeyBinding configKeybind = new KeyBinding("dev.paw.uniformity.keybind.config_key", GLFW.GLFW_KEY_RIGHT_SHIFT, "dev.paw.uniformity.name");
    public static final KeyBinding antiToolBreakOverrideKeybind = new KeyBinding("dev.paw.uniformity.keybind.antitoolbreakOverride", GLFW.GLFW_KEY_GRAVE_ACCENT, "dev.paw.uniformity.name");
    public static final Zoom zoom = new Zoom();

    public Uniformity() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(ModConfig.class);
        config = configHolder.getConfig();
        configHolder.registerSaveListener((holder, cfg) -> {
            if (wasKeybindSave) {
                holder.setConfig(config);
                wasKeybindSave = false;
            }
            return ActionResult.PASS;
        });
        configHolder.load();
        config.freecamToggle = false;
        config.recyclerToggle = false;
        modules.add(new Fullbright());
        modules.add(new Step());
        modules.add(new NumericPing());
        modules.add(new HorseStats());
        modules.add(new AntiToolBreak());
        modules.add(new Freecam());
        modules.add(new EntityOutline());
        modules.add(zoom);
        modules.add(new ReCycler());
        modules.add(new DisplayInfo());
        modules.add(new AutoFish());
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(configKeybind);
        KeyBindingHelper.registerKeyBinding(zoomKeybind);
        KeyBindingHelper.registerKeyBinding(antiToolBreakOverrideKeybind);
        for (Module m: modules) {
            if (m instanceof KeyboundModule kbm) {
                KeyBindingHelper.registerKeyBinding(kbm.keyBind);
            }
        }
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> new ReCyclerCmd(dispatcher)
        );
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> {
            try {
                ItemStack pot = Items.SPLASH_POTION.getDefaultStack();
                pot.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"Splash Potion of Instant Death\",\"color\":\"white\",\"italic\":false}',Lore:['{\"text\":\"Instant Death\",\"color\":\"red\",\"italic\":false}']},HideFlags:34,Enchantments:[{}],custom_potion_effects:[{id:\"minecraft:instant_health\",amplifier:125b,duration:2000}],CustomPotionColor:8010938}"));
                content.add(pot);
                pot = Items.LINGERING_POTION.getDefaultStack();
                pot.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"Lingering Potion of Instant Death\",\"color\":\"white\",\"italic\":false}',Lore:['{\"text\":\"Instant Death\",\"color\":\"red\",\"italic\":false}']},HideFlags:34,Enchantments:[{}],custom_potion_effects:[{id:\"minecraft:instant_health\",amplifier:125b,duration:2000}],CustomPotionColor:8010938}"));
                content.add(pot);
                content.add(Items.BUNDLE);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKeybind.wasPressed()) {
                MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(ModConfig.class, MinecraftClient.getInstance().currentScreen).get());
            }
            zoom.isZooming = zoomKeybind.isPressed();
            if (zoom.isZooming) {
                zoom.wasZooming = true;
            } else if (zoom.wasZooming) {
                zoom.wasZooming = false;
                zoom.resetZoomDivisor();
                zoom.zoomStep = 0;
            }
            for (Module m: modules) {
                if (m instanceof KeyboundModule kbm) {
                    while (kbm.keyBind.wasPressed()) {
                        boolean v = m.toggle();
                        MinecraftClient
                                .getInstance()
                                .inGameHud
                                .setOverlayMessage(Text.translatable("dev.paw.uniformity.prefix").append(Text.translatable(v ? "dev.paw.uniformity.enabled" : "dev.paw.uniformity.disabled", m.name)), false);
                        wasKeybindSave = true;
                        configHolder.save();
                    }
                }
                m.onClientTick();
            }
        });
        if (config.disableTutorialToast) {
            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTutorialManager().setStep(TutorialStep.NONE));
        }
    }

    public static void sendPrefixedMessage(Text msg) {
        MinecraftClient.getInstance()
                .inGameHud
                .getChatHud()
                .addMessage(Text.translatable("dev.paw.uniformity.prefix").append(msg));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T getModule(Class<T> clazz) {
        for (Module m: modules) {
            if (m.getClass().equals(clazz)) {
                return (T) m;
            }
        }
        return null;
    }

    public static void onClientInit() {
        isInit = true;
        zoom.unbindConflictingKey(MinecraftClient.getInstance());
        logger.info("Hello World, I'm back.");
    }
}
