package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.CaughtFishEvent;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.dizitart.jbus.Subscribe;

public class AutoFish extends KeyboundModule {
    public AutoFish() {
        super("AutoFish", -1);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.autoFishToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.autoFishToggle = value;
    }

    @Subscribe
    public void onCaughtFish(CaughtFishEvent evt) {
        if (isEnabled() && evt.fisher == mc.player) {
            doAutoFish();
        }
    }

    public void doAutoFish() {
        if (mc.player == null || mc.interactionManager == null) return;
        if (mc.player.getMainHandStack().getItem() == Items.FISHING_ROD) {
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);
        } else if (mc.player.getOffHandStack().getItem() == Items.FISHING_ROD) {
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
            mc.player.swingHand(Hand.OFF_HAND);
            mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
            mc.player.swingHand(Hand.OFF_HAND);
        }
    }
}
