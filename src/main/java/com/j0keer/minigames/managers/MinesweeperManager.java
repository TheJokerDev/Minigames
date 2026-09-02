package com.j0keer.minigames.managers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j0keer.minigames.Minigames;
import com.j0keer.minigames.config.ConfigFile;
import com.j0keer.minigames.objects.MinesweeperMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MinesweeperManager {
    private final Map<String, MinesweeperMap> maps;
    private boolean active;

    public MinesweeperManager() {
        this.maps = new HashMap<>();
        this.active = false;
    }

    public void loadMaps() {
        this.maps.clear();
        ConfigFile mapsConfig = Minigames.getInstance().getConfigManager().getMapsConfig();
        JsonObject mapsObject = mapsConfig.getJsonObject("maps");
        if (mapsObject == null) return;

        for (Map.Entry<String, JsonElement> entry : mapsObject.entrySet()) {
            if (!entry.getValue().isJsonObject()) continue;
            String id = entry.getKey();
            JsonObject mapJson = entry.getValue().getAsJsonObject();
            MinesweeperMap map = MinesweeperMap.fromJson(id, mapJson);
            if (map != null) this.maps.put(id, map);
        }
    }

    public void saveMaps() {
        ConfigFile mapsConfig = Minigames.getInstance().getConfigManager().getMapsConfig();
        for (MinesweeperMap map : this.maps.values()) mapsConfig.set("maps." + map.getId(), map.toJson());
        mapsConfig.save();
    }

    public void saveMap(MinesweeperMap map) {
        if (map == null) return;
        this.maps.put(map.getId(), map);
        ConfigFile mapsConfig = Minigames.getInstance().getConfigManager().getMapsConfig();
        mapsConfig.set("maps." + map.getId(), map.toJson());
        mapsConfig.save();
    }

    public void addMap(MinesweeperMap map) {
        if (map == null) return;
        this.maps.put(map.getId(), map);
        saveMap(map);
    }

    public void removeMap(String id) {
        if (id == null) return;
        this.maps.remove(id);
        ConfigFile mapsConfig = Minigames.getInstance().getConfigManager().getMapsConfig();
        mapsConfig.set("maps." + id, null);
        mapsConfig.save();
    }

    public boolean resetMap(String id, MinecraftServer server) {
        MinesweeperMap map = getMap(id);
        if (map == null) return false;
        if (server != null) {
            ServerWorld world = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.of(map.getWorld())));
            map.resetBlocks(world);
        } else {
            map.reset();
        }
        return true;
    }

    public void resetAll(MinecraftServer server) {
        for (MinesweeperMap map : this.maps.values()) {
            if (server != null) {
                ServerWorld world = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.of(map.getWorld())));
                map.resetBlocks(world);
            } else {
                map.reset();
            }
        }
    }

    public MinesweeperMap getMap(String id) {
        return this.maps.get(id);
    }

    public Map<String, MinesweeperMap> getMaps() {
        return Collections.unmodifiableMap(this.maps);
    }

    public MinesweeperMap getMapAt(World world, BlockPos pos) {
        if (world == null || pos == null) return null;
        for (MinesweeperMap map : this.maps.values()) {
            if (map.isInRegion(world, pos)) return map;
        }
        return null;
    }

    public MinesweeperMap getMapAt(String worldId, BlockPos pos) {
        if (worldId == null || pos == null) return null;
        for (MinesweeperMap map : this.maps.values()) {
            if (map.isInRegion(worldId, pos)) return map;
        }
        return null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
