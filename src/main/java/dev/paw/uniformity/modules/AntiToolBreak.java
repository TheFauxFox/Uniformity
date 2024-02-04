package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class AntiToolBreak extends Module {
    public AntiToolBreak() {
        super("AntiToolBreak");
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.toolBreakToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.toolBreakToggle = value;
    }

    public boolean shouldCancelToolUse(boolean checkOffhand) {
        if (mc.player == null) return false;
        if (!mc.options.attackKey.isPressed() && !mc.options.useKey.isPressed()) return false;
        if (Uniformity.antiToolBreakOverrideKeybind.isPressed() || !Uniformity.config.toolBreakToggle) {
            return false;
        }

        ArrayList<ItemStack> stackList = new ArrayList<>();
        stackList.add(mc.player.getMainHandStack());
        if (checkOffhand) stackList.add(mc.player.getOffHandStack());

        for (ItemStack stack: stackList) {
            if (!stack.isDamaged()) continue;

            Item item = stack.getItem();
            int durabilityValue = Uniformity.config.antiToolBreak.duraValue;
            boolean swords = Uniformity.config.antiToolBreak.swords;
            boolean pickaxes = Uniformity.config.antiToolBreak.pickaxes;
            boolean shovels = Uniformity.config.antiToolBreak.shovels;
            boolean axes = Uniformity.config.antiToolBreak.axes;
            boolean bows = Uniformity.config.antiToolBreak.bows;
            boolean tridents = Uniformity.config.antiToolBreak.tridents;
            boolean hoes = Uniformity.config.antiToolBreak.hoes;
            boolean shears = Uniformity.config.antiToolBreak.shears;

            if (
                    (swords && item instanceof SwordItem) ||
                    (pickaxes && item instanceof PickaxeItem) ||
                    (shovels && item instanceof ShovelItem) ||
                    (axes && item instanceof AxeItem) ||
                    (bows && (item instanceof BowItem || item instanceof CrossbowItem)) ||
                    (tridents && item instanceof TridentItem) ||
                    (hoes && item instanceof HoeItem) ||
                    (shears && item instanceof ShearsItem)
            ) {
                if (stack.getMaxDamage() - stack.getDamage() <= durabilityValue) {
                    if (Uniformity.config.antiToolBreak.showWarning) {
                        mc.inGameHud.setSubtitle(Text.of("§f" + item.getName().getString() + " is about to break!"));
                        mc.inGameHud.setTitleTicks(0, 1, 0);
                        mc.inGameHud.setTitle(Text.of("§fWarning!"));
                        mc.inGameHud.setOverlayMessage(Text.of("§7§l\uD835\uDCE4§8 » §cPress §f" + Uniformity.antiToolBreakOverrideKeybind.getBoundKeyLocalizedText().getString() + "§c to allow tool breaking"), true);
                    }
                    if (Uniformity.config.antiToolBreak.playSound) {
                        mc.player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 0.2f, 1);
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
