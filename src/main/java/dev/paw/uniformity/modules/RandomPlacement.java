package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientPlayerEvent;
import dev.paw.uniformity.utils.Number;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import org.dizitart.jbus.Subscribe;

import java.util.ArrayList;

public class RandomPlacement extends KeyboundModule {
    public RandomPlacement() {
        super("RandomPlacement", -1);
    }

    @Subscribe
    public void onPlaceBlock(ClientPlayerEvent.ItemUseEvent.POST evt) {
        if (!this.isEnabled() || mc.player == null || mc.world == null) return;
        PlayerInventory inv = mc.player.getInventory();
        ArrayList<Integer> hotbar = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).getItem() instanceof BlockItem && !inv.getStack(i).isEmpty()) {
                hotbar.add(i);
            }
        }
        inv.selectedSlot = hotbar.get(Number.randInt(0, hotbar.size()));
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.randomPlacementToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.randomPlacementToggle = value;
    }
}
