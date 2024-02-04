package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.TimeUnit;

public class AutoRefill extends Module {
    private int lastUsedSlot = -1;
    private Item lastItemMain = Items.AIR;
    private Item lastItemOffhand = Items.AIR;
    private Hand swungHand = Hand.MAIN_HAND;

    public AutoRefill() {
        super("AutoRefill");
    }

    public void onHandSwing(Hand hand) {
        swungHand = hand;
    }

    public void onItemUsePRE() {
        if (mc.player == null) return;
        lastUsedSlot = getSlot();
        lastItemMain = getMainItem();
        lastItemOffhand = getOffItem();
    }

    public void onItemUsePOST() {
        if (mc.player == null || mc.interactionManager == null || !isEnabled()) return;
        PlayerInventory inv = mc.player.getInventory();
        if (swungHand == Hand.MAIN_HAND && lastUsedSlot == getSlot() && getMainItem() == Items.AIR && lastItemMain != Items.AIR) {
            if (inv.contains(lastItemMain.getDefaultStack())) {
                int i = inv.getSlotWithStack(lastItemMain.getDefaultStack());
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, GLFW.GLFW_MOUSE_BUTTON_1, SlotActionType.PICKUP, mc.player);

                Uniformity.scheduler.schedule(() -> mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mc.player.getInventory().selectedSlot + PlayerInventory.MAIN_SIZE, GLFW.GLFW_MOUSE_BUTTON_1, SlotActionType.PICKUP, mc.player), Uniformity.config.autoRefill.refillMS, TimeUnit.MILLISECONDS);
            }
        } else if (swungHand == Hand.OFF_HAND && getOffItem() == Items.AIR && lastItemOffhand != Items.AIR) {
            if (inv.contains(lastItemOffhand.getDefaultStack())) {
                Uniformity.logger.info("offhand refill");
                int i = inv.getSlotWithStack(lastItemOffhand.getDefaultStack());
                mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, GLFW.GLFW_MOUSE_BUTTON_1, SlotActionType.PICKUP, mc.player);

                Uniformity.scheduler.schedule(() -> mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 45, GLFW.GLFW_MOUSE_BUTTON_1, SlotActionType.PICKUP, mc.player), Uniformity.config.autoRefill.refillMS, TimeUnit.MILLISECONDS);
            }
        }
    }

    private int getSlot() {
        if (mc.player == null) return 0;
        return mc.player.getInventory().selectedSlot;
    }

    private Item getMainItem() {
        if (mc.player == null) return Items.AIR;
        return mc.player.getMainHandStack().getItem();
    }

    private Item getOffItem() {
        if (mc.player == null) return Items.AIR;
        return mc.player.getOffHandStack().getItem();
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.autoRefillToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.autoRefillToggle = value;
    }
}
