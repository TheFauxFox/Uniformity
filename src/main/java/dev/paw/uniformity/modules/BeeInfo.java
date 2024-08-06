package dev.paw.uniformity.modules;

import com.mojang.serialization.Decoder;
import com.mojang.serialization.MapDecoder;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.GetTooltipEvent;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.dizitart.jbus.Subscribe;

public class BeeInfo extends Module {
    public BeeInfo() {
        super("BeeInfo");
    }

    @Subscribe
    public void onItemStack(GetTooltipEvent evt) {
        if (!isEnabled()) return;
        if (evt.item != Items.BEEHIVE && evt.item != Items.BEE_NEST) return;

        ComponentMap nbt = evt.nbt;
        if (nbt == null) return;

        int honeyLevel = nbt.get(DataComponentTypes.BLOCK_ENTITY_DATA).copyNbt().getInt("honey_level");
        NbtList beeList = nbt.get(DataComponentTypes.BLOCK_ENTITY_DATA).copyNbt().getList("Bees", NbtElement.COMPOUND_TYPE);
        int beeCount = beeList.size();

        boolean hasCustomBees = false;

        for (int ix = 0; ix < beeCount; ix ++) {
            NbtCompound bee = beeList.getCompound(ix).getCompound("EntityData");
            if (bee.contains("CustomName", NbtElement.STRING_TYPE)) {
                String beeName = bee.getString("CustomName");

                evt.tooltipList.add(Math.min(1, evt.tooltipList.size()), Text.of("§7   " + beeName));
                hasCustomBees = true;
            }
        }

        if (hasCustomBees) {
            evt.tooltipList.add(Math.min(1, evt.tooltipList.size()), Text.translatable("dev.paw.uniformity.bee_info.custom_names"));
            evt.tooltipList.add(Math.min(1, evt.tooltipList.size()), Text.of(""));
        }

        evt.tooltipList.add(Math.min(1, evt.tooltipList.size()), Text.translatable("dev.paw.uniformity.bee_info.bee_count", beeCount));
        evt.tooltipList.add(Math.min(1, evt.tooltipList.size()), Text.translatable("dev.paw.uniformity.bee_info.honey_level", honeyLevel));
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.beeInfoToggle;
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.beeInfoToggle = value;
    }
}
