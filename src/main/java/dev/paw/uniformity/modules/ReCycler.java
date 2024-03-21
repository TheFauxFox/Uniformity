package dev.paw.uniformity.modules;

import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.events.ClientTickEvent;
import dev.paw.uniformity.events.MouseButtonEvent;
import dev.paw.uniformity.events.PacketEvent;
import dev.paw.uniformity.events.Render3dEvent;
import dev.paw.uniformity.utils.Color;
import dev.paw.uniformity.utils.Render3D;
import dev.paw.uniformity.utils.Rotation;
import dev.paw.uniformity.utils.Timer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.network.packet.s2c.play.SetTradeOffersS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.RaycastContext;
import org.dizitart.jbus.Subscribe;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReCycler extends KeyboundModule {
    public TradeOfferList lastOffers = null;
    public VillagerTradeInfo villagerInfo = null;
    public VillagerEntity villager = null;
    public BlockPos lecternPos = null;
    public Enchantment targetEnchant = null;
    public int threshold = -1;
    public final Timer timer = new Timer();
    public final Timer delayTimer = new Timer();
    public int step = 0;
    public boolean stepping;
    public boolean useButton = false;
    public static KeyBinding startKeybind = new KeyBinding("dev.paw.uniformity.keybind.recyclerStart", -1, "dev.paw.uniformity.name");

    public ReCycler() {
        super("ReCycler", -1);
    }

    @Override
    public boolean isEnabled() {
        return Uniformity.config.recyclerToggle;
    }

    @Override
    public void registerKeybindEvent() {
        KeyBindingHelper.registerKeyBinding(startKeybind);
    }

    @Override
    public void onKeybind() {
        while (startKeybind.wasPressed()) {
            startStepping();
        }
    }

    @Override
    public void setEnabled(boolean value) {
        Uniformity.config.recyclerToggle = value;
        if (value) onEnable();
        else onDisable();
    }

    public void chatMsg(String key, Object ...args) {
        Uniformity.sendPrefixedMessage(Text.translatable("dev.paw.uniformity.recycler.prefix").append(Text.translatable(key, args)));
    }

    @Subscribe
    public void onSendPacket(PacketEvent.OnSend evt) {
        if (!isEnabled()) return;
        if (evt.getPacket() instanceof  PlayerInteractEntityC2SPacket && mc.targetedEntity instanceof VillagerEntity ent && ent.getVillagerData().getProfession().equals(VillagerProfession.LIBRARIAN) && mc.options.sneakKey.isPressed()) {
            villager = ent;
            chatMsg("dev.paw.uniformity.recycler.setVillagerMessage");
        }
    }

    @Subscribe
    public void onRecvPacket(PacketEvent.OnRecieved evt) {
        if (!isEnabled()) return;
        if ((evt.getPacket() instanceof CloseScreenS2CPacket || evt.getPacket() instanceof CloseHandledScreenC2SPacket) && stepping) {
            evt.cancel();
        }
        if (evt.getPacket() instanceof OpenScreenS2CPacket p && (stepping || mc.options.sneakKey.isPressed())) {
            if (Objects.equals(p.getScreenHandlerType(), ScreenHandlerType.MERCHANT)) {
                evt.cancel();
            }
        }
        if (evt.getPacket() instanceof SetTradeOffersS2CPacket p) {
            lastOffers = p.getOffers();
        }
    }

    @Subscribe
    public void onMouseButton(MouseButtonEvent evt) {
        if (isEnabled() && evt.button == MouseButtonEvent.Button.Right) {
            useButton = true;
        }
    }

    @Subscribe
    public void onRender3d(Render3dEvent evt) {
        if (mc.player == null || mc.world == null || !isEnabled()) return;
        Render3D.begin(evt.matrices);

        if (villager != null) {
            evt.matrices.push();
            Render3D.drawOutlinedBox(villager.getBoundingBox(), evt.matrices, Color.rgba(200, 200, 200, 128));
            Render3D.drawSolidBox(villager.getBoundingBox(), evt.matrices, Color.rgba(85,85,85, 70));
            evt.matrices.pop();
        }

        if (lecternPos != null) {
            evt.matrices.push();
            Render3D.drawOutlinedBox(new Box(lecternPos), evt.matrices, Color.rgba(200, 200, 200, 128));
            Render3D.drawSolidBox(new Box(lecternPos), evt.matrices, Color.rgba(85,85,85, 70));
            evt.matrices.pop();
        }

        Render3D.end(evt.matrices);
    }

    public void onEnable() {
        villager = null;
        villagerInfo = null;
        lecternPos = null;
        lastOffers = null;
        targetEnchant = null;
        threshold = -1;
        stepping = false;
    }

    public void startStepping() {
        if (!sanityCheck()) {
            return;
        }
        chatMsg("dev.paw.uniformity.recycler.startMessage");
        this.timer.reset();
        this.delayTimer.reset();
        stepping = true;
    }

    public void onDisable() {
        villager = null;
        villagerInfo = null;
        lecternPos = null;
        lastOffers = null;
        targetEnchant = null;
        threshold = -1;
        stepping = false;
    }

    @Override
    public void onClientTick(ClientTickEvent evt) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null || !this.isEnabled()) return;
        if (villager != null && (villager.isDead() || villager.getHealth() == 0)) {
            villager = null;
        }
        while (useButton && mc.options.sneakKey.isPressed()) {
            useButton = false;
            if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                Vec3d tPos = mc.crosshairTarget.getPos();
                BlockPos pos = new BlockPos((int)Math.floor(tPos.x), (int)Math.floor(tPos.y), (int)Math.floor(tPos.z));
                Block block = mc.world.getBlockState(pos).getBlock();
                if (block instanceof LecternBlock) {
                    if (!pos.equals(lecternPos)) {
                        lecternPos = pos;
                        chatMsg("dev.paw.uniformity.recycler.setLecturnMessage");
                    }
                }
            }
        }
        if (villager != null && villagerInfo == null) {
            if (lastOffers != null) {
                villagerInfo = new VillagerTradeInfo(villager, lastOffers);
                lastOffers = null;
            }
        }
        if (stepping && this.isEnabled() && sanityCheck()) {
            tick();
        }
    }

    private boolean _lecternSanityCheck() {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return false;
        Uniformity.logger.debug("Lectern sanity check");
        return lecternPos != null && mc.world.getBlockState(lecternPos).getBlock() instanceof LecternBlock;
    }

    private boolean _villagerSanityCheck() {
        Uniformity.logger.debug("Villy sanity check");
        if (villager == null) return false;
        Uniformity.logger.debug("Profession sanity check");
        if (!villager.getVillagerData().getProfession().equals(VillagerProfession.LIBRARIAN)) return false;
        Uniformity.logger.debug("Trades sanity check");
        forceInteractWithVillager();
        return villagerInfo != null;
    }

    private boolean _itemsSanityCheck() {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return false;
        Uniformity.logger.debug("Items sanity check");
        boolean mainAxe = mc.player.getMainHandStack().getItem() instanceof AxeItem;
        boolean offhandLect = mc.player.getOffHandStack().getItem().equals(Items.LECTERN);
        return mainAxe && offhandLect;
    }

    private boolean _durabilitySanityCheck() {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return false;
        Uniformity.logger.debug("Durabilty sanity check");
        if (mc.player.getMainHandStack().getItem() instanceof AxeItem) {
            if (mc.player.getMainHandStack().getMaxDamage() - mc.player.getMainHandStack().getDamage() <= 10) {
                chatMsg("dev.paw.uniformity.recycler.axeDurabilityMessage");
                setEnabled(false);
                return false;
            }
        }
        return true;
    }

    private boolean _bookSanityCheck() {
        if (villagerInfo == null || villagerInfo.bookless()) return false;
        boolean isTargetBook = villagerInfo.getEnchantment().equals(targetEnchant);
        boolean maxed = villagerInfo.isMaxLvl();
        boolean withinOffset = villagerInfo.isCheapest() || villagerInfo.priceDifference() <= threshold;
        Uniformity.logger.debug("Book sanity check");
        return isTargetBook && maxed && withinOffset;
    }

    private boolean _lecturnBreakCheck() {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return false;
        Uniformity.logger.debug("Breaking lectern");
        if (mc.world.getBlockState(lecternPos).getBlock() instanceof LecternBlock) {
            Rotation.Rotations rotations = Rotation.getNeededRotations(lecternPos.toCenterPos());
            if (rotations == null) return false;
            rotations.lookAt();
            mc.interactionManager.updateBlockBreakingProgress(lecternPos, Direction.UP);
            mc.player.swingHand(Hand.MAIN_HAND);
            return false;
        } else if (mc.world.getBlockState(lecternPos).getBlock() instanceof AirBlock) {
            return true;
        }
        return mc.world.getBlockState(lecternPos).getBlock() instanceof AirBlock;
    }

    private boolean _villagerLoseProfesionCheck() {
        Uniformity.logger.debug("Lose librarian check");
        delayTimer.reset();
        return !villagerInfo.getEntity().getVillagerData().getProfession().equals(VillagerProfession.LIBRARIAN);
    }

    private boolean _placeLecturnCheck() {
        if (mc.world == null || mc.player == null || mc.interactionManager == null || !delayTimer.hasElapsed(Uniformity.config.reCycler.delayMS)) return false;
        Uniformity.logger.debug("Place back lecturn");
        villagerInfo = null;
        lastOffers = null;
        tryToPlace(lecternPos, mc.player.getEyePos());
        return mc.world.getBlockState(lecternPos).getBlock() instanceof LecternBlock;
    }

    public void tick() {
        if (mc.world == null || mc.player == null || mc.interactionManager == null || !stepping) return;
        switch (step) {
            case 0 -> step += _lecternSanityCheck() ? 1 : 0;
            case 1 -> step += _itemsSanityCheck() ? 1 : 0;
            case 2 -> step += _durabilitySanityCheck() ? 1 : 0;
            case 3 -> step += _villagerSanityCheck() ? 1 : 0;
            case 4 ->  {
                if (_bookSanityCheck()) {
                    chatMsg("dev.paw.uniformity.recycler.bookFoundMessage", villagerInfo.getEnchantment().getName(villagerInfo.getBookLvl()).getString(), villagerInfo.getPrice());
                    mc.player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
                    mc.player.playSound(SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.MASTER, 0.5f, 1);
                    mc.player.playSound(SoundEvents.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, SoundCategory.MASTER, 0.4f, 1);
                    chatMsg("dev.paw.uniformity.recycler.timeTakenMessage", timer.getTimeStr());
                    stepping = false;
                    mc.player.closeHandledScreen();
                    forceInteractWithVillager();
                    setEnabled(false);
                } else {
                    step += 1;
                }
            }
            case 5 -> step += _lecturnBreakCheck() ? 1 : 0;
            case 6 -> step += _villagerLoseProfesionCheck() ? 1 : 0;
            case 7 -> step = _placeLecturnCheck() ? 0 : 7;
        }
    }

    private void tryToPlace(BlockPos pos, Vec3d eyesPos) {
        if (mc.world == null || mc.player == null || mc.interactionManager == null) return;
        Vec3d posVec = Vec3d.ofCenter(pos);
        double distanceSqPosVec = eyesPos.squaredDistanceTo(posVec);

        for(Direction side : Direction.values()) {
            BlockPos neighbor = pos.offset(side);
            if(mc.world.getBlockState(neighbor).getOutlineShape(mc.world, pos) == VoxelShapes.empty() || mc.world.getBlockState(neighbor).isReplaceable()) continue;
            Vec3d dirVec = Vec3d.of(side.getVector());
            Vec3d hitVec = posVec.add(dirVec.multiply(0.5));
            if (eyesPos.squaredDistanceTo(hitVec) > mc.interactionManager.getReachDistance()) continue;
            if (distanceSqPosVec > eyesPos.squaredDistanceTo(posVec.add(dirVec))) continue;
            if (mc.world.raycast(new RaycastContext(eyesPos, hitVec, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getType() != HitResult.Type.MISS) continue;
            Rotation.Rotations rotation = Rotation.getNeededRotations(hitVec);
            if (rotation == null) return;
            PlayerMoveC2SPacket.LookAndOnGround packet = new PlayerMoveC2SPacket.LookAndOnGround(rotation.yaw, rotation.pitch, mc.player.isOnGround());
            mc.player.networkHandler.sendPacket(packet);
            BlockHitResult hitResult = new BlockHitResult(hitVec, side, pos, false);
            Hand hand = Hand.OFF_HAND;
            mc.interactionManager.interactBlock(mc.player, hand, hitResult);
            mc.interactionManager.interactItem(mc.player, hand);
            mc.player.swingHand(hand);
            return;
        }
    }

    public List<String> getEnchantList() {
        ArrayList<String> enchants = new ArrayList<>();
        for (Enchantment f : Registries.ENCHANTMENT) {
            String name = f.getName(1).getString();
            name = name.endsWith(" I") ? name.replaceFirst(" I$", "").replace(" ", "_").toUpperCase() : name.replace(" ", "_").toUpperCase();
            if (!name.equals("SOUL_SPEED") && !name.equals("SWIFT_SNEAK")) {
                enchants.add(name);
            }
        }
        return enchants;
    }

    public void forceInteractWithVillager() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.interactionManager == null || mc.getNetworkHandler() == null) return;
        if (villager == null) return;
        lastOffers = null;
        Rotation.Rotations.from(villager).lookAt();
        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.interactAt(villager, mc.player.isSneaking(), Hand.MAIN_HAND, villager.getEyePos()));
        mc.getNetworkHandler().sendPacket(PlayerInteractEntityC2SPacket.interact(villager, mc.player.isSneaking(), Hand.MAIN_HAND));
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    public boolean sanityCheck() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return false;
        if (mc.currentScreen instanceof MerchantScreen) {
            mc.currentScreen.close();
        }
        if (villager == null) {
            chatMsg("dev.paw.uniformity.recycler.sanityVillager");
            return false;
        }
        if (villager.distanceTo(mc.player) > 3) {
            chatMsg("dev.paw.uniformity.recycler.sanityVillagerDistance");
            return false;
        }
        if (lecternPos == null) {
            chatMsg("dev.paw.uniformity.recycler.sanityLecturn");
            return false;
        }
        if (!(mc.player.getMainHandStack().getItem() instanceof AxeItem) || !(mc.player.getOffHandStack().getItem().equals(Items.LECTERN))) {
            chatMsg("dev.paw.uniformity.recycler.sanityMainOffHand");
            return false;
        }
        if (targetEnchant == null) {
            chatMsg("dev.paw.uniformity.recycler.sanitySetEnchant");
            return false;
        }
        if (threshold == -1) {
            chatMsg("dev.paw.uniformity.recycler.sanitySetThreshold");
            return false;
        }
        return true;
    }

    public static class VillagerTradeInfo {
        private final VillagerEntity entity;
        private ItemStack book;
        private int bookLvl;
        private Enchantment enchant;
        private int emeraldPrice;

        public VillagerTradeInfo(VillagerEntity villager, TradeOfferList tradeOffers) {
            this.entity = villager;

            for (TradeOffer trade: tradeOffers) {
                if (trade.getSellItem().getItem() instanceof EnchantedBookItem) {
                    emeraldPrice = trade.getAdjustedFirstBuyItem().getCount();
                    book = trade.getSellItem();
                    break;
                }
            }
            if (bookless()) return;

            NbtList nbt = EnchantedBookItem.getEnchantmentNbt(book);
            NbtCompound element = nbt.getCompound(0);
            String bookType = element.getString("id");
            bookLvl = element.getInt("lvl");
            enchant = Registries.ENCHANTMENT.get(new Identifier(bookType));
        }

        public boolean bookless() {
            return book == null;
        }

        public VillagerEntity getEntity() {
            return entity;
        }

        public int getPrice() {
            return emeraldPrice;
        }

        public int getMaxLvl() {
            return enchant.getMaxLevel();
        }

        public int getBookLvl() {
            return bookLvl;
        }

        public boolean isMaxLvl() {
            return getMaxLvl() == getBookLvl();
        }

        public int getCheapest() {
            return (getMaxLvl() * 5) + 2;
        }

        public boolean isCheapest() {
            return getCheapest() == getPrice();
        }

        public int priceDifference() {
            return getPrice() - getCheapest();
        }

        public Enchantment getEnchantment() {
            return enchant;
        }
    }
}
