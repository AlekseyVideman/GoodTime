package com.github.alekseyvideman.goodtime.service;

import com.github.alekseyvideman.goodtime.GoodTime;
import com.github.alekseyvideman.goodtime.configuration.dto.ConfigurationDto;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerTimeGreetingService {

    private final PlayerRealTimeService playerRealTimeService;
    private final ConfigurationDto config;
    private final GoodTime goodTime;

    public void sendGreeting(Player player) {
        var message = colorize(getTimeMessage(player));
        long delay = config.messageDelay();
        if (config.messageType() == 0) {
            goodTime.getServer()
                    .getScheduler()
                    .scheduleSyncDelayedTask(goodTime, () -> player.sendMessage(message), delay);
        } else if (config.messageType() == 1) {
            goodTime.getServer()
                    .getScheduler()
                    .scheduleSyncDelayedTask(goodTime, () -> player.showTitle(Title.title(message,
                                    Component.empty(),
                                    Title.Times.times(
                                            Duration.ofMillis(config.titleFadeIn()),
                                            Duration.ofMillis(config.titleStay()),
                                            Duration.ofMillis(config.titleFadeOut())
                                    ))),
                            delay);
        } else if (config.messageType() == 2) {
            goodTime.getServer()
                    .getScheduler()
                    .scheduleSyncDelayedTask(goodTime, () -> player.sendActionBar(message), delay);
        }
    }
    
    private String getTimeMessage(Player player) {
        var playerHour = playerRealTimeService.getRealPlayerHour(player.getAddress().getAddress());
        var message = "";

        if (playerHour >= 6 && playerHour <= 12) {
            message = config.goodMorning();
        } else if (playerHour >= 12 && playerHour <= 18) {
            message = config.goodAfternoon();
        } else if (playerHour >= 18 && playerHour <= 23) {
            message = config.goodEvening();
        } else {
            message = config.goodNight();
        }

        return message;
    }
    
    private Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

}
