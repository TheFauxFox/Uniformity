package dev.paw.uniformity.events;

import net.minecraft.component.ComponentMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.text.Text;

import java.util.List;

public class GetTooltipEvent extends Event {
    public final ComponentMap nbt;
    public final Item item;
    public final PlayerEntity holder;
    public final List<Text> tooltipList;

    public GetTooltipEvent(ComponentMap nbtData, Item itemType, PlayerEntity holder, List<Text> list) {
        this.nbt = nbtData;
        this.item = itemType;
        this.holder = holder;
        this.tooltipList = list;
    }
}
