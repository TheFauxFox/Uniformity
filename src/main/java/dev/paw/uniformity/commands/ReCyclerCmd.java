package dev.paw.uniformity.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.ReCycler;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.registry.Registries;
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
                        .executes(command -> {
                            ReCycler rc = Uniformity.getModule(ReCycler.class);
                            if (rc != null) {
                                String name = rc.targetEnchant.getName(1).getString();
                                name = name.endsWith(" I") ? name.replaceFirst(" I$", "") : name;
                                rc.chatMsg("Current book: "+name+" with threshold of "+rc.threshold);
                                return 1;
                            }
                            return 0;
                        })
                        .then(literal("setEnchant").then(argument("enchant", word()).executes(command -> {
                            ReCycler rc = Uniformity.getModule(ReCycler.class);
                            if (rc != null) {
                                if (rc.getEnchantList().contains(command.getArgument("enchant", String.class).toUpperCase())) {
                                    rc.targetEnchant = Registries.ENCHANTMENT.get(new Identifier("minecraft:" + command.getArgument("enchant", String.class).toLowerCase()));
                                    rc.chatMsg("Sucessfully set target enchant to " + command.getArgument("enchant", String.class).toUpperCase());
                                    return 1;
                                }
                                rc.chatMsg("Possible enchantments: " + Arrays.toString(rc.getEnchantList().toArray()));
                            }
                            return 0;
                        })))
                        .then(literal("setThreshold").then(argument("threshold", integer(0,57)).executes(command -> {
                            ReCycler rc = Uniformity.getModule(ReCycler.class);
                            if (rc != null) {
                                int t = command.getArgument("threshold", Integer.class);
                                if (t <= 57 && t >= 0) {
                                    rc.chatMsg("Sucessfully set threshold to "+t);
                                    rc.threshold = t;
                                    return 1;
                                }
                            }
                            return 0;
                        })))
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
        );
    }
}
