package com.j0keer.minigames.events;

import com.google.gson.JsonObject;
import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.blocks.MineBlock;
import com.j0keer.minigames.blocks.MineState;
import com.j0keer.minigames.config.ConfigFile;
import com.j0keer.minigames.objects.MinesweeperMap;
import com.j0keer.minigames.registries.ItemRegistries;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerEvents {
    private static final Map<UUID, BlockPos> pos1Map = new HashMap<>();
    private static final Map<UUID, BlockPos> pos2Map = new HashMap<>();
    private static final Map<UUID, BlockPos> lastPlayerPosMap = new HashMap<>();

    public static void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            if (world.isClient() || hand != Hand.MAIN_HAND) return ActionResult.PASS;

            if (player.getMainHandStack().isOf(ItemRegistries.SELECTION_WAND) && hasAdminPermission(player)) {
                setPos1(player.getUuid(), pos);
                player.sendMessage(Text.literal("§d[Minigames] §aPosición 1 establecida en §e" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), false);
                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient() || hand != Hand.MAIN_HAND) return ActionResult.PASS;

            ItemStack heldItem = player.getMainHandStack();

            if (heldItem.isOf(ItemRegistries.SELECTION_WAND) && hasAdminPermission(player)) {
                BlockPos pos = hitResult.getBlockPos();
                setPos2(player.getUuid(), pos);
                player.sendMessage(Text.literal("§d[Minigames] §aPosición 2 establecida en §e" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), false);
                return ActionResult.SUCCESS;
            }

            if (heldItem.isOf(Items.TNT) && hasAdminPermission(player)) {
                BlockPos clickedPos = hitResult.getBlockPos();
                BlockPos targetPos = clickedPos.offset(hitResult.getSide());
                MinesweeperMap map = Minigames.getInstance().getMinesweeperManager().getMapAt(world, clickedPos);
                if (map == null) map = Minigames.getInstance().getMinesweeperManager().getMapAt(world, targetPos);

                if (map != null) {
                    BlockPos minePos = map.isInRegion(world, clickedPos) ? clickedPos : targetPos;
                    map.addMine(minePos);
                    Minigames.getInstance().getMinesweeperManager().saveMap(map);

                    BlockState state = world.getBlockState(minePos);
                    if (state.contains(MineBlock.HAS_MINE)) world.setBlockState(minePos, state.with(MineBlock.HAS_MINE, true), Block.NOTIFY_ALL);

                    player.sendMessage(Text.literal("§d[Minigames] §aMina (TNT) añadida en §eX: " + minePos.getX() + " Z: " + minePos.getZ() + " §apara el mapa §b" + map.getId()), false);
                }
            }

            if (heldItem.isOf(Items.TARGET) && hasAdminPermission(player)) {
                BlockPos clickedPos = hitResult.getBlockPos();
                BlockPos targetPos = clickedPos.offset(hitResult.getSide());
                MinesweeperMap map = Minigames.getInstance().getMinesweeperManager().getMapAt(world, clickedPos);
                if (map == null) map = Minigames.getInstance().getMinesweeperManager().getMapAt(world, targetPos);

                if (map != null) {
                    BlockPos minePos = map.isInRegion(world, clickedPos) ? clickedPos : targetPos;
                    if (map.hasMine(minePos)) {
                        map.removeMine(minePos);
                        Minigames.getInstance().getMinesweeperManager().saveMap(map);

                        BlockState state = world.getBlockState(minePos);
                        if (state.contains(MineBlock.HAS_MINE)) world.setBlockState(minePos, state.with(MineBlock.HAS_MINE, false), Block.NOTIFY_ALL);

                        player.sendMessage(Text.literal("§d[Minigames] §cMina removida en §eX: " + minePos.getX() + " Z: " + minePos.getZ() + " §cpara el mapa §b" + map.getId()), false);
                    } else {
                        player.sendMessage(Text.literal("§d[Minigames] §eNo hay ninguna mina registrada en X: " + minePos.getX() + " Z: " + minePos.getZ()), false);
                    }
                }
            }

            return ActionResult.PASS;
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!Minigames.getInstance().getMinesweeperManager().isActive()) return;

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.isCreative() || player.isSpectator() || player.isDead()) {
                    lastPlayerPosMap.remove(player.getUuid());
                    continue;
                }

                BlockPos currentPos = player.getBlockPos();
                BlockPos lastPos = lastPlayerPosMap.get(player.getUuid());
                if (lastPos != null && lastPos.equals(currentPos)) continue;
                lastPlayerPosMap.put(player.getUuid(), currentPos);

                ServerWorld world = (ServerWorld) player.getWorld();
                BlockPos steppingPos = player.getSteppingPos();

                MinesweeperMap map = Minigames.getInstance().getMinesweeperManager().getMapAt(world, steppingPos);
                if (map == null) map = Minigames.getInstance().getMinesweeperManager().getMapAt(world, currentPos);
                if (map == null) continue;

                BlockPos floorPos = map.isInRegion(world, steppingPos) ? steppingPos : currentPos;
                floorPos = new BlockPos(floorPos.getX(), map.getFloorY(), floorPos.getZ());

                if (map.isMineActive(floorPos)) {
                    map.triggerMine(floorPos);

                    BlockState state = world.getBlockState(floorPos);
                    if (state.contains(MineBlock.STATE)) {
                        world.setBlockState(floorPos, state.with(MineBlock.STATE, MineState.EXPLODED), Block.NOTIFY_ALL);
                    }

                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getY(), player.getZ(), 1, 0, 0, 0, 0);
                    player.kill();
                } else if (!map.hasMine(floorPos)) {
                    int count = map.countSurroundingMines(floorPos);
                    MineState mineState = MineState.fromNumber(count);
                    BlockState state = world.getBlockState(floorPos);

                    if (state.contains(MineBlock.STATE) && state.get(MineBlock.STATE) != mineState) {
                        world.setBlockState(floorPos, state.with(MineBlock.STATE, mineState), Block.NOTIFY_ALL);
                    }
                }
            }
        });
    }

    public static boolean hasAdminPermission(PlayerEntity player) {
        return player.hasPermissionLevel(2);
    }

    public static void setPos1(UUID uuid, BlockPos pos) {
        pos1Map.put(uuid, pos);
    }

    public static void setPos2(UUID uuid, BlockPos pos) {
        pos2Map.put(uuid, pos);
    }

    public static BlockPos getPos1(UUID uuid) {
        return pos1Map.get(uuid);
    }

    public static BlockPos getPos2(UUID uuid) {
        return pos2Map.get(uuid);
    }

    public static boolean saveMapSelection(UUID uuid, String mapName, World world) {
        BlockPos p1 = getPos1(uuid);
        BlockPos p2 = getPos2(uuid);
        if (p1 == null || p2 == null) return false;

        ConfigFile mapsConfig = Minigames.getInstance().getConfigManager().getMapsConfig();

        JsonObject mapData = new JsonObject();
        mapData.addProperty("world", world.getRegistryKey().getValue().toString());

        JsonObject pos1Json = new JsonObject();
        pos1Json.addProperty("x", p1.getX());
        pos1Json.addProperty("y", p1.getY());
        pos1Json.addProperty("z", p1.getZ());
        mapData.add("pos1", pos1Json);

        JsonObject pos2Json = new JsonObject();
        pos2Json.addProperty("x", p2.getX());
        pos2Json.addProperty("y", p2.getY());
        pos2Json.addProperty("z", p2.getZ());
        mapData.add("pos2", pos2Json);

        mapsConfig.set("maps." + mapName, mapData);
        mapsConfig.save();
        return true;
    }
}
