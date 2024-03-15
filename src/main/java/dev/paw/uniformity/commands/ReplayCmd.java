package dev.paw.uniformity.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.paw.uniformity.Uniformity;
import dev.paw.uniformity.modules.Replay;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class ReplayCmd {
    public ReplayCmd(CommandDispatcher<FabricClientCommandSource> r) {
        r.register(
                literal("macro").executes(command -> {
                            Replay replay = Uniformity.getModule(Replay.class);
                            if (replay == null) return 0;
                            replay.serializer.list();
                            return 1;
                        })
                        .then(literal("save").then(argument("name", greedyString()).executes(command -> {
                            Replay replay = Uniformity.getModule(Replay.class);
                            if (replay == null) return 0;
                            replay.serializer.save(command.getArgument("name", String.class));
                            return 1;
                        })))
                        .then(literal("load").then(argument("name", greedyString()).executes(command -> {
                            Replay replay = Uniformity.getModule(Replay.class);
                            if (replay == null) return 0;
                            replay.serializer.load(command.getArgument("name", String.class));
                            return 1;
                        })))
        );
    }
}
