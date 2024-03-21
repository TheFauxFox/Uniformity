package dev.paw.uniformity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.paw.uniformity.commands.ReCyclerCmd;
import dev.paw.uniformity.commands.ReplayCmd;
import dev.paw.uniformity.config.ModConfig;
import dev.paw.uniformity.events.ClientTickEvent;
import dev.paw.uniformity.modules.*;
import dev.paw.uniformity.modules.Module;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.tutorial.TutorialStep;
import net.minecraft.item.*;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.dizitart.jbus.JBus;
import org.dizitart.jbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Uniformity implements ClientModInitializer {
    public static ModConfig config;
    public static Logger logger = LoggerFactory.getLogger("Uniformity");

    public static JBus bus = new JBus();

    public static boolean isInit = false;
    private final ConfigHolder<ModConfig> configHolder;
    public static final ArrayList<Module> modules = new ArrayList<>();
    private boolean wasKeybindSave = false;

    public static final KeyBinding configKeybind = new KeyBinding("dev.paw.uniformity.keybind.config_key", GLFW.GLFW_KEY_RIGHT_SHIFT, "dev.paw.uniformity.name");
    public static final KeyBinding antiToolBreakOverrideKeybind = new KeyBinding("dev.paw.uniformity.keybind.antitoolbreakOverride", GLFW.GLFW_KEY_GRAVE_ACCENT, "dev.paw.uniformity.name");
    public static final Zoom zoom = new Zoom();
    public static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Uniformity() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        FabricLoader.getInstance().getModContainer("uniformity").ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("uniformity", "dark_gui"), modContainer, ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier("uniformity", "smooth_font"), modContainer, ResourcePackActivationType.NORMAL);
        });

        configHolder = AutoConfig.getConfigHolder(ModConfig.class);
        config = configHolder.getConfig();
        configHolder.registerSaveListener((holder, cfg) -> {
            if (wasKeybindSave) {
                scheduler.schedule(() -> {
                    holder.setConfig(config);
                    configHolder.save();
                }, 20, TimeUnit.MILLISECONDS);
                wasKeybindSave = false;
            } else {
                config = cfg;
            }
            return ActionResult.PASS;
        });
        configHolder.load();
        config.freecamToggle = false;
        config.recyclerToggle = false;

        modules.add(new AntiToolBreak());
        modules.add(new AutoClicker());
        modules.add(new AutoFish());
        modules.add(new AutoReconnect());
        modules.add(new AutoRefill());
        modules.add(new BeeInfo());
        modules.add(new CreativeTools());
        modules.add(new DarkLoadingScreen());
        modules.add(new DirectionDisplay());
        modules.add(new DisplayInfo());
        modules.add(new EntityOutline());
        modules.add(new ExtendChatHistory());
        modules.add(new FPSDisplay());
        modules.add(new Freecam());
        modules.add(new Fullbright());
        modules.add(new HorseStats());
        modules.add(new LowFire());
        modules.add(new MeasuringTape());
        modules.add(new MobNameplates());
        modules.add(new NoChatReportToast());
        modules.add(new NoFog());
        modules.add(new NoHurtAngle());
        modules.add(new NoTelemetry());
        modules.add(new NumericPing());
        modules.add(new ReCycler());
        modules.add(new Replay());
        modules.add(new SnipperAura());
        modules.add(new Step());
        modules.add(zoom);
        bus.register(this);
    }

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(configKeybind);
        KeyBindingHelper.registerKeyBinding(zoom.zoomKeybind);
        KeyBindingHelper.registerKeyBinding(antiToolBreakOverrideKeybind);
        for (Module m: modules) {
            if (m instanceof KeyboundModule kbm) {
                KeyBindingHelper.registerKeyBinding(kbm.keyBind);
                m.registerKeybindEvent();
            }
            bus.register(m);
        }
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> {
                    new ReCyclerCmd(dispatcher);
                    new ReplayCmd(dispatcher);
                }
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
        if (config.disableTutorialToast) {
            MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTutorialManager().setStep(TutorialStep.NONE));
        }
    }

    @Subscribe
    public void onClientTick(ClientTickEvent evt) {
        while (configKeybind.wasPressed()) {
            MinecraftClient.getInstance().setScreen(AutoConfig.getConfigScreen(ModConfig.class, MinecraftClient.getInstance().currentScreen).get());
        }
        zoom.isZooming = zoom.zoomKeybind.isPressed();
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
            } else {
                m.onKeybind();
            }
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
