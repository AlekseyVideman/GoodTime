package com.github.alekseyvideman.goodtime;

import com.github.alekseyvideman.goodtime.configuration.guice.GeneralGuiceConfiguration;
import com.github.alekseyvideman.goodtime.listener.PlayerJoinListener;
import com.github.alekseyvideman.goodtime.service.PlayerRealTimeService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GoodTime extends JavaPlugin implements Listener {

    @Getter(onMethod_ = @Override)
    private FileConfiguration config;

    private PlayerRealTimeService realTimeService;

    @Override
    public void onEnable() {
        createConfig();

        var injector = Guice.createInjector(new GeneralGuiceConfiguration());
        setupListeners(injector);

        this.realTimeService = injector.getInstance(PlayerRealTimeService.class);
    }

    @Override
    public void onDisable() {
        realTimeService.closeDataSource();
    }

    private void createConfig() {
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
            config = YamlConfiguration.loadConfiguration(file);
            return;
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    private void setupListeners(Injector injector) {
        getServer().getPluginManager().registerEvents(injector.getInstance(PlayerJoinListener.class), this);
    }
}
