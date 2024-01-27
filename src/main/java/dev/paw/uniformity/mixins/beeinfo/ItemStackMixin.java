package dev.paw.uniformity.mixins.beeinfo;

import dev.paw.uniformity.Uniformity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract boolean isEmpty();

    @Shadow public abstract Item getItem();

    @Shadow @Nullable public abstract NbtCompound getNbt();

    @Inject(method = "getTooltip", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void onGetTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        if (!Uniformity.config.beeInfoToggle) return;
        if (this.isEmpty()) return;
        if (this.getItem() != Items.BEEHIVE && this.getItem() != Items.BEE_NEST) return;

        NbtCompound nbt = this.getNbt();
        if (nbt == null) return;

        int honeyLevel = nbt.getCompound("BlockStateTag").getInt("honey_level");
        NbtList beeList = nbt.getCompound("BlockEntityTag").getList("Bees", NbtElement.COMPOUND_TYPE);
        int beeCount = beeList.size();

        boolean hasCustomBees = false;

        for (int ix = 0; ix < beeCount; ix ++) {
            NbtCompound bee = beeList.getCompound(ix).getCompound("EntityData");
            if (bee.contains("CustomName", NbtElement.STRING_TYPE)) {
                String beeName = bee.getString("CustomName");

                list.add(Math.min(1, list.size()), Text.of("§7   " + beeName));
                hasCustomBees = true;
            }
        }

        if (hasCustomBees) {
            list.add(Math.min(1, list.size()), Text.translatable("dev.paw.uniformity.bee_info.custom_names"));
            list.add(Math.min(1, list.size()), Text.of(""));
        }

        list.add(Math.min(1, list.size()), Text.translatable("dev.paw.uniformity.bee_info.bee_count", beeCount));
        list.add(Math.min(1, list.size()), Text.translatable("dev.paw.uniformity.bee_info.honey_level", honeyLevel));
    }
}
