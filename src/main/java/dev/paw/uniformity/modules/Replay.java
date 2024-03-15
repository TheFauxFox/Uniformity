package dev.paw.uniformity.modules;

import com.google.gson.Gson;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.Render3dEvent;
import dev.paw.uniformity.utils.Rotation;
import dev.paw.uniformity.utils.Timer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.dizitart.jbus.Subscribe;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Replay extends KeyboundModule {
    public static KeyBinding recordKey, playbackKey;
    public static ArrayList<Step> steps = new ArrayList<>();
    public static Step step;
    private final Timer playbackTimer = new Timer();
    public final Serializer serializer = new Serializer();

    public boolean recording = false;
    public boolean playing = false;
    public boolean infinite = false;
    public int stepIndex = 0;

    public Replay() {
        super("Replay", -1);
    }

    @Override
    public void registerKeybindEvent() {
        recordKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("dev.paw.uniformity.replay.keybind.startRecording", GLFW.GLFW_KEY_F9, "dev.paw.uniformity.replay.keybind.category"));
        playbackKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("dev.paw.uniformity.replay.keybind.stopRecording", GLFW.GLFW_KEY_F10, "dev.paw.uniformity.replay.keybind.category"));
    }

    public static void chat(String msg, Object... obj) {
        rawChat(Text.translatable("dev.paw.uniformity.replay.prefix").append(Text.translatable(msg, obj)));
    }

    public static void rawChat(Text msg) {
        Uniformity.sendPrefixedMessage(msg);
    }

    @Subscribe
    public void worldRenderTick(Render3dEvent evt) {
        if (!this.isEnabled()) return;
        while (recordKey.wasPressed()) {
            if (recording) {
                steps.add(step.close());
                recording = false;
                chat("dev.paw.uniformity.replay.stopRecording");
                step.toJson();
            } else {
                chat("dev.paw.uniformity.replay.startRecording");
                steps.clear();
                playing = false;
                recording = true;
                step = Step._new();
            }
        }
        if (recording) {
            Step _step = Step._new();
            if (!step.equals(_step)) {
                steps.add(step.close());
                step = _step;
            }
        }
        while (playbackKey.wasPressed()) {
            if (playing) {
                chat("dev.paw.uniformity.replay.stopPlaying");
                step.unpress();
                playing = false;
                infinite = false;
            } else if (!steps.isEmpty()) {
                if (MinecraftClient.getInstance().options.sneakKey.isPressed()) {
                    infinite = true;
                }
                chat("dev.paw.uniformity.replay.startPlaying", infinite ? "[looped]" : "");
                stepIndex = 0;
                recording = false;
                playing = true;
                step = steps.get(stepIndex);
                stepIndex += 1;
            }
        }
        if (playing) {
            if (!playbackTimer.hasElapsed(step.duration)) {
                step.apply();
            } else {
                if (steps.size() > stepIndex) {
                    step = steps.get(stepIndex);
                    stepIndex += 1;
                    playbackTimer.reset();
                } else {
                    if (infinite) {
                        stepIndex = 0;
                        chat("dev.paw.uniformity.replay.looping");
                    } else {
                        chat("dev.paw.uniformity.replay.stopPlaying");
                        step.unpress();
                        playing = false;
                        infinite = false;
                    }
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.replayToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.replayToggle = value;
    }

    public static class Step {
        public static MinecraftClient mc = MinecraftClient.getInstance();
        public boolean fw, bk, lf, rt, sn, jp, us, at;
        public Rotation.Rotations ro;
        public long duration;
        public final Timer timer;

        private Step(boolean fw, boolean bk, boolean lf, boolean rt, boolean sn, boolean jp, boolean us, boolean at, Rotation.Rotations ro) {
            this.fw = fw;
            this.bk = bk;
            this.lf = lf;
            this.rt = rt;
            this.sn = sn;
            this.jp = jp;
            this.us = us;
            this.at = at;
            this.ro = ro;
            this.timer = new Timer();
        }

        public void apply() {
            mc.options.forwardKey.setPressed(fw);
            mc.options.backKey.setPressed(bk);
            mc.options.leftKey.setPressed(lf);
            mc.options.rightKey.setPressed(rt);
            mc.options.sneakKey.setPressed(sn);
            mc.options.jumpKey.setPressed(jp);
            boolean wasPressed = mc.options.useKey.isPressed();
            if (us && !wasPressed && mc.targetedEntity != null && mc.interactionManager != null && mc.player != null && mc.crosshairTarget != null) {
                switch (mc.crosshairTarget.getType()) {
                    case MISS -> mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
                    case BLOCK -> mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, (BlockHitResult) mc.crosshairTarget);
                    case ENTITY -> mc.interactionManager.interactEntity(mc.player, mc.targetedEntity, Hand.MAIN_HAND);
                }
            }
            mc.options.useKey.setPressed(us);
            wasPressed = mc.options.attackKey.isPressed();
            if (at && !wasPressed && mc.targetedEntity != null && mc.interactionManager != null && mc.player != null) {
                mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
                mc.player.swingHand(Hand.MAIN_HAND);
            }
            else if (!at && wasPressed && mc.interactionManager != null && mc.player != null && mc.interactionManager.getBlockBreakingProgress() > -1) {
                mc.interactionManager.cancelBlockBreaking();
            }
            mc.options.attackKey.setPressed(at);
            ro.lookAt();
        }

        public Step close() {
            duration = timer.getElapsed();
            return this;
        }

        public void unpress() {
            mc.options.forwardKey.setPressed(false);
            mc.options.backKey.setPressed(false);
            mc.options.leftKey.setPressed(false);
            mc.options.rightKey.setPressed(false);
            mc.options.sneakKey.setPressed(false);
            mc.options.jumpKey.setPressed(false);
            mc.options.useKey.setPressed(false);
            mc.options.attackKey.setPressed(false);
        }

        public static boolean _forwardKey() {
            return mc.options.forwardKey.isPressed();
        }

        public static boolean _backKey() {
            return mc.options.backKey.isPressed();
        }

        public static boolean _leftKey() {
            return mc.options.leftKey.isPressed();
        }

        public static boolean _rightKey() {
            return mc.options.rightKey.isPressed();
        }

        public static boolean _sneakKey() {
            return mc.options.sneakKey.isPressed();
        }

        public static boolean _jumpKey() {
            return mc.options.jumpKey.isPressed();
        }

        public static boolean _useKey() {
            return mc.options.useKey.isPressed();
        }

        public static boolean _attackKey() {
            return mc.options.attackKey.isPressed();
        }

        public static Rotation.Rotations _rotation() {
            return Rotation.Rotations.from(mc.player);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Step sp) {
                return sp.fw == fw && sp.bk == bk && sp.lf == lf && sp.rt == rt && sp.sn == sn && sp.jp == jp && sp.us == us && sp.at == at && sp.ro.equals(ro);
            }
            return false;
        }

        public static Step _new() {
            return new Step(_forwardKey(), _backKey(), _leftKey(), _rightKey(), _sneakKey(), _jumpKey(), _useKey(), _attackKey(), _rotation());
        }

        public void toJson() {
            Gson gson = new Gson();
            Uniformity.logger.info(gson.toJson(this));
        }
    }

    public static class Serializer {
        private final File recordingPath;

        public Serializer() {
            recordingPath = new File(MinecraftClient.getInstance().runDirectory, "macros/");
            recordingPath.mkdir();
        }

        public void save(String name) {
            File f = new File(recordingPath, name+".json");
            try (FileWriter w = new FileWriter(f)) {
                w.write(new Gson().toJson(Replay.steps));
                chat("dev.paw.uniformity.replay.saveRecording", name);
            } catch (IOException e) {
                chat("dev.paw.uniformity.replay.saveFailed", name);
            }
        }

        @SuppressWarnings("unchecked")
        public void load(String name) {
            File f = new File(recordingPath, name+".json");
            try (FileReader w = new FileReader(f)) {
                StringBuilder data = new StringBuilder();
                int i;
                while ((i = w.read()) != -1) {
                    data.append((char) i);
                }
                ArrayList<Object> arr = new Gson().fromJson(data.toString(), ArrayList.class);
                ArrayList<Step> steps = new ArrayList<>();
                for (Object o: arr) {
                    steps.add(new Gson().fromJson(o.toString(), Step.class));
                }
                Replay.steps.clear();
                Replay.steps = steps;
                chat("dev.paw.uniformity.replay.loadedRecording", name);
            } catch (IOException e) {
                chat("dev.paw.uniformity.replay.loadFailed", name);
            }
        }

        public void list() {
            String[] fileList = recordingPath.list();
            if (fileList == null) return;
            rawChat(Text.translatable("dev.paw.uniformity.replay.listHeader"));
            for(String s: fileList) {
                rawChat(Text.translatable("dev.paw.uniformity.replay.listFormat", s.replaceFirst("\\.json$", "")));
            }
        }
    }
}
