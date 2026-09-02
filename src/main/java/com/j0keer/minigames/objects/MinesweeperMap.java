package com.j0keer.minigames.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j0keer.minigames.blocks.MineBlock;
import com.j0keer.minigames.blocks.MineState;
import com.j0keer.minigames.registries.BlockRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class MinesweeperMap {
    private final String id;
    private final String world;
    private final BlockPos pos1;
    private final BlockPos pos2;
    private final int minX;
    private final int maxX;
    private final int minZ;
    private final int maxZ;
    private final int floorY;
    private final Set<BlockPos> mines;
    private final Set<BlockPos> triggeredMines;

    public MinesweeperMap(String id, String world, BlockPos pos1, BlockPos pos2) {
        this(id, world, pos1, pos2, new HashSet<>());
    }

    public MinesweeperMap(String id, String world, BlockPos pos1, BlockPos pos2, Set<BlockPos> mines) {
        this.id = id;
        this.world = world;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.minX = Math.min(pos1.getX(), pos2.getX());
        this.maxX = Math.max(pos1.getX(), pos2.getX());
        this.minZ = Math.min(pos1.getZ(), pos2.getZ());
        this.maxZ = Math.max(pos1.getZ(), pos2.getZ());
        this.floorY = Math.min(pos1.getY(), pos2.getY());
        this.mines = mines != null ? mines : new HashSet<>();
        this.triggeredMines = new HashSet<>();
    }

    public boolean isInRegion(World targetWorld, BlockPos pos) {
        if (targetWorld == null || pos == null) return false;
        String worldId = targetWorld.getRegistryKey().getValue().toString();
        return isInRegion(worldId, pos);
    }

    public boolean isInRegion(String worldId, BlockPos pos) {
        if (!this.world.equals(worldId)) return false;

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        boolean inXZ = x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        boolean inY = y == floorY || y == floorY + 1;
        return inXZ && inY;
    }

    public void addMine(BlockPos pos) {
        BlockPos minePos = new BlockPos(pos.getX(), floorY, pos.getZ());
        mines.add(minePos);
    }

    public void removeMine(BlockPos pos) {
        BlockPos minePos = new BlockPos(pos.getX(), floorY, pos.getZ());
        mines.remove(minePos);
        triggeredMines.remove(minePos);
    }

    public boolean hasMine(BlockPos pos) {
        BlockPos minePos = new BlockPos(pos.getX(), floorY, pos.getZ());
        return mines.contains(minePos);
    }

    public boolean isMineActive(BlockPos pos) {
        BlockPos minePos = new BlockPos(pos.getX(), floorY, pos.getZ());
        return mines.contains(minePos) && !triggeredMines.contains(minePos);
    }

    public void triggerMine(BlockPos pos) {
        BlockPos minePos = new BlockPos(pos.getX(), floorY, pos.getZ());
        triggeredMines.add(minePos);
    }

    public int countSurroundingMines(BlockPos pos) {
        int count = 0;
        int x = pos.getX();
        int z = pos.getZ();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;
                if (hasMine(new BlockPos(x + dx, floorY, z + dz))) count++;
            }
        }
        return count;
    }

    public void reset() {
        triggeredMines.clear();
    }

    public void resetBlocks(ServerWorld serverWorld) {
        reset();
        if (serverWorld == null) return;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos pos = new BlockPos(x, floorY, z);
                BlockState state = serverWorld.getBlockState(pos);
                if (!state.contains(MineBlock.STATE)) {
                    serverWorld.setBlockState(pos, BlockRegistries.MINE_BLOCK.getDefaultState().with(MineBlock.STATE, MineState.HIDDEN).with(MineBlock.HAS_MINE, hasMine(pos)), Block.NOTIFY_ALL);
                } else {
                    serverWorld.setBlockState(pos, state.with(MineBlock.STATE, MineState.HIDDEN).with(MineBlock.HAS_MINE, hasMine(pos)), Block.NOTIFY_ALL);
                }
            }
        }
    }

    public JsonObject toJson() {
        JsonObject mapJson = new JsonObject();
        mapJson.addProperty("world", this.world);

        JsonObject pos1Json = new JsonObject();
        pos1Json.addProperty("x", this.pos1.getX());
        pos1Json.addProperty("y", this.pos1.getY());
        pos1Json.addProperty("z", this.pos1.getZ());
        mapJson.add("pos1", pos1Json);

        JsonObject pos2Json = new JsonObject();
        pos2Json.addProperty("x", this.pos2.getX());
        pos2Json.addProperty("y", this.pos2.getY());
        pos2Json.addProperty("z", this.pos2.getZ());
        mapJson.add("pos2", pos2Json);

        JsonArray minesArray = new JsonArray();
        for (BlockPos mine : this.mines) {
            JsonObject mineJson = new JsonObject();
            mineJson.addProperty("x", mine.getX());
            mineJson.addProperty("z", mine.getZ());
            minesArray.add(mineJson);
        }
        mapJson.add("mines", minesArray);

        return mapJson;
    }

    public static MinesweeperMap fromJson(String id, JsonObject json) {
        if (json == null) return null;

        String world = json.has("world") ? json.get("world").getAsString() : "minecraft:overworld";

        JsonObject pos1Obj = json.getAsJsonObject("pos1");
        BlockPos pos1 = new BlockPos(pos1Obj.get("x").getAsInt(), pos1Obj.get("y").getAsInt(), pos1Obj.get("z").getAsInt());

        JsonObject pos2Obj = json.getAsJsonObject("pos2");
        BlockPos pos2 = new BlockPos(pos2Obj.get("x").getAsInt(), pos2Obj.get("y").getAsInt(), pos2Obj.get("z").getAsInt());

        int floor = Math.min(pos1.getY(), pos2.getY());
        Set<BlockPos> mines = new HashSet<>();

        if (json.has("mines") && json.get("mines").isJsonArray()) {
            JsonArray minesArray = json.getAsJsonArray("mines");
            for (JsonElement element : minesArray) {
                if (element.isJsonObject()) {
                    JsonObject mineObj = element.getAsJsonObject();
                    int x = mineObj.get("x").getAsInt();
                    int z = mineObj.get("z").getAsInt();
                    mines.add(new BlockPos(x, floor, z));
                }
            }
        }

        return new MinesweeperMap(id, world, pos1, pos2, mines);
    }

    public String getId() {
        return id;
    }

    public String getWorld() {
        return world;
    }

    public BlockPos getPos1() {
        return pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public int getFloorY() {
        return floorY;
    }

    public Set<BlockPos> getMines() {
        return mines;
    }

    public Set<BlockPos> getTriggeredMines() {
        return triggeredMines;
    }
}
