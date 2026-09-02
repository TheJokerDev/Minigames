package com.j0keer.minigames;

import com.j0keer.minigames.commands.MinigamesCommand;
import com.j0keer.minigames.config.ConfigManager;
import com.j0keer.minigames.events.PlayerEvents;
import com.j0keer.minigames.managers.MinesweeperManager;
import com.j0keer.minigames.registries.BlockRegistries;
import com.j0keer.minigames.registries.EntityRegistries;
import com.j0keer.minigames.registries.ItemGroupRegistries;
import com.j0keer.minigames.registries.ItemRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Minigames implements ModInitializer {
    public static final String MOD_ID = "minigames";
    private static Minigames instance;
    private ConfigManager configManager;
    private MinesweeperManager minesweeperManager;

    @Override
    public void onInitialize() {
        instance = this;

        this.configManager = new ConfigManager();
        this.configManager.loadConfigs();

        this.minesweeperManager = new MinesweeperManager();
        this.minesweeperManager.loadMaps();

        BlockRegistries.registerModBlocks();
        ItemRegistries.registerModItems();
        EntityRegistries.registerModEntities();
        ItemGroupRegistries.registerModItemGroups();

        PlayerEvents.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            MinigamesCommand.register(dispatcher);
        });
    }

    private void initNetworking() {
        
    }

    public static Minigames getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MinesweeperManager getMinesweeperManager() {
        return minesweeperManager;
    }
}
