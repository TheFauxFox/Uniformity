package dev.paw.uniformity.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.ReCycler;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import java.util.Arrays;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ReCyclerCmd {
    public ReCyclerCmd(CommandDispatcher<FabricClientCommandSource> r) {
        r.register(
            literal("recycler")
                .then(literal("clear").executes(command -> {
                    ReCycler rc = Uniformity.getModule(ReCycler.class);
                    if (rc != null) {
                        rc.targetEnchant = null;
                        rc.threshold = 0;
                        rc.villager = null;
                        rc.villagerInfo = null;
                        return 1;
                    }
                    return 0;
                }))
                .then(literal("start").executes(command -> {
                    ReCycler rc = Uniformity.getModule(ReCycler.class);
                    if (rc != null) {
                        rc.startStepping();
                    }
                    return 1;
                }))
                .then(literal("set").then(argument("enchant", word()).then(argument("threshold", integer(0, 56)).executes(command -> {
                    ReCycler rc = Uniformity.getModule(ReCycler.class);
                    if (rc != null && MinecraftClient.getInstance().world != null) {
                        String enchant = command.getArgument("enchant", String.class).toUpperCase();
                        String enchantBase = enchant;
                        if (rc.getEnchantList().contains(enchant)) {
                            if (enchant.equals("SWEEPING_EDGE")) enchant = "SWEEPING";
                            rc.targetEnchant = MinecraftClient.getInstance().world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).get(Identifier.of("minecraft", enchant.toLowerCase()));
                            rc.chatMsg("Sucessfully set target enchant to " + enchantBase);
                        } else {
                            rc.chatMsg("§cFailed to set target enchant to " + enchantBase);
                            rc.chatMsg("Possible enchantments: " + Arrays.toString(rc.getEnchantList().toArray()));
                        }
                        int t = command.getArgument("threshold", Integer.class);
                        if (t <= 57 && t >= 0) {
                            rc.chatMsg("Sucessfully set threshold to " + t);
                            rc.threshold = t;
                        } else {
                            rc.chatMsg("§cThreshold must be between 0 and 57");
                        }
                    }
                    return 0;
                }))))
        );
    }
}
