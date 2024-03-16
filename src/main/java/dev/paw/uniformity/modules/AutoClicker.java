package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientTickEvent;
import dev.paw.uniformity.utils.Timer;
import net.minecraft.util.Hand;

public class AutoClicker extends KeyboundModule {
    private final Timer timer = new Timer();

    public AutoClicker() {
        super("AutoClicker", -1);
    }

    @Override
    public void onClientTick(ClientTickEvent evt) {
        if (!isEnabled() || mc.interactionManager == null || mc.player == null) return;
        if (!readyToClick()) return;
        timer.reset();
        if (mc.targetedEntity != null) {
            mc.interactionManager.attackEntity(mc.player, mc.targetedEntity);
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private boolean readyToClick() {
        return timer.hasElapsed(
                Uniformity.config.autoClicker.autoClickerUseCPS ?
                        1000 / Uniformity.config.autoClicker.autoClickerCPS :
                        Uniformity.config.autoClicker.autoClickerMS
        );
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.autoClickerToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.autoClickerToggle = value;
    }
}
