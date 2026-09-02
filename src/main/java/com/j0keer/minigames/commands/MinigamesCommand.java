package com.j0keer.minigames.commands;

import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.events.PlayerEvents;
import com.j0keer.minigames.objects.MinesweeperMap;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MinigamesCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("minigames")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("mines")
                        .then(CommandManager.literal("createmap")
                                .then(CommandManager.argument("id", StringArgumentType.string())
                                        .executes(context -> {
                                            ServerCommandSource source = context.getSource();
                                            ServerPlayerEntity player = source.getPlayer();
                                            if (player == null) {
                                                source.sendFeedback(() -> Text.literal("§cEste comando solo puede ser ejecutado por un jugador."), false);
                                                return 0;
                                            }

                                            String mapId = StringArgumentType.getString(context, "id");
                                            BlockPos pos1 = PlayerEvents.getPos1(player.getUuid());
                                            BlockPos pos2 = PlayerEvents.getPos2(player.getUuid());
                                            if (pos1 == null || pos2 == null) {
                                                player.sendMessage(Text.literal("§c[Minigames] Debes marcar la posición 1 y 2 con el hacha de madera primero."), false);
                                                return 0;
                                            }

                                            String worldId = player.getWorld().getRegistryKey().getValue().toString();
                                            MinesweeperMap map = new MinesweeperMap(mapId, worldId, pos1, pos2);
                                            Minigames.getInstance().getMinesweeperManager().addMap(map);
                                            player.sendMessage(Text.literal("§d[Minigames] §aMapa §e" + mapId + " §acreado y guardado correctamente."), false);
                                            return 1;
                                        })
                                )
                        )
                        .then(CommandManager.literal("removemap")
                                .then(CommandManager.argument("id", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            Minigames.getInstance().getMinesweeperManager().getMaps().keySet().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            String mapId = StringArgumentType.getString(context, "id");
                                            boolean removed = Minigames.getInstance().getMinesweeperManager().removeMap(mapId);
                                            if (!removed) {
                                                context.getSource().sendFeedback(() -> Text.literal("§c[Minigames] El mapa §e" + mapId + " §cno existe."), false);
                                                return 0;
                                            }
                                            context.getSource().sendFeedback(() -> Text.literal("§d[Minigames] §aEl mapa §e" + mapId + " §aha sido eliminado."), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(CommandManager.literal("active")
                                .then(CommandManager.argument("state", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean state = BoolArgumentType.getBool(context, "state");
                                            Minigames.getInstance().getMinesweeperManager().setActive(state);
                                            String status = state ? "§aactivado" : "§cdesactivado";
                                            context.getSource().sendFeedback(() -> Text.literal("§d[Minigames] §fEl minijuego Buscaminas ha sido " + status + "§f."), true);
                                            return 1;
                                        })
                                )
                        )
                        .then(CommandManager.literal("reset")
                                .then(CommandManager.argument("id", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            Minigames.getInstance().getMinesweeperManager().getMaps().keySet().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            String mapId = StringArgumentType.getString(context, "id");
                                            boolean reset = Minigames.getInstance().getMinesweeperManager().resetMap(mapId, context.getSource().getServer());
                                            if (!reset) {
                                                context.getSource().sendFeedback(() -> Text.literal("§c[Minigames] El mapa §e" + mapId + " §cno existe."), false);
                                                return 0;
                                            }
                                            context.getSource().sendFeedback(() -> Text.literal("§d[Minigames] §aEl mapa §e" + mapId + " §aha sido reseteado."), true);
                                            return 1;
                                        })
                                )
                                .executes(context -> {
                                    Minigames.getInstance().getMinesweeperManager().resetAll(context.getSource().getServer());
                                    context.getSource().sendFeedback(() -> Text.literal("§d[Minigames] §aTodos los mapas de buscaminas han sido reseteados."), true);
                                    return 1;
                                })
                        )
                )
        );
    }
}
