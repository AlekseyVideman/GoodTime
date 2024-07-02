package com.github.alekseyvideman.goodtime.configuration.guice;

import com.github.alekseyvideman.goodtime.GoodTime;
import com.github.alekseyvideman.goodtime.configuration.dto.ConfigurationDto;
import com.github.alekseyvideman.goodtime.service.PlayerRealTimeService;
import com.github.alekseyvideman.goodtime.service.PlayerTimeGreetingService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class GeneralGuiceConfiguration extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlayerRealTimeService.class);
        bind(PlayerTimeGreetingService.class);
    }

    @Provides
    public ConfigurationDto configurationDto(FileConfiguration config) {
        var T = new ConfigurationDto(
                config.getString("messages.goodMorning"),
                config.getString("messages.goodAfternoon"),
                config.getString("messages.goodEvening"),
                config.getString("messages.goodNight"),

                config.getLong("options.messageDelay"),
                config.getInt("options.messageType"),

                config.getInt("options.title.fadeIn"),
                config.getInt("options.title.stay"),
                config.getInt("options.title.fadeOut")
        );
        System.out.println(T);
        return T;
    }

    @Provides
    public FileConfiguration fileConfiguration(GoodTime goodTime) {
        var t =goodTime.getConfig();
        System.out.println(t.getValues(true));
        return t;
    }

    @Provides
    public GoodTime goodTime() {
        return ((GoodTime) Bukkit.getPluginManager().getPlugin("GoodTime"));
    }

    @Provides
    public DatabaseReader databaseReader(File database) throws IOException {
        return new DatabaseReader.Builder(database)
                .withCache(new CHMCache(30))
                .build();
    }

    @Provides
    public File dataBaseFile(GoodTime goodTime) {
        var file = new File(goodTime.getDataFolder() + File.separator + "GeoLite2-City.mmdb");

        if (!file.exists()) {
            throw new IllegalStateException("MaxMind GeoLite2-City database not found. Download one.");
        }

        return file;
    }
}
