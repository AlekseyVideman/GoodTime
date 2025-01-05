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
        goodTime.getServer()
                .getScheduler().
                runTaskAsynchronously(goodTime, getGreetingRunnable(player));
    }

    private Runnable getGreetingRunnable(Player player) {
        return () -> {

            var message = colorize(getTimeMessage(player));
            long delay = config.messageDelay();

            goodTime.getServer()
                    .getScheduler()
                    .scheduleSyncDelayedTask(goodTime, createMessageRunnable(player, message), delay);

        };
    }

    @SuppressWarnings("all")
    private String getTimeMessage(Player player) {
        Integer playerHour = playerRealTimeService.getRealPlayerHour(player);

        return switch (playerHour) {
            case Integer hour when hour >= 6 && hour <= 12 -> config.goodMorning();
            case Integer hour when hour >= 12 && hour <= 18 -> config.goodAfternoon();
            case Integer hour when hour >= 18 && hour <= 23 -> config.goodEvening();

            default -> config.goodNight();
        };
    }

    private Runnable createMessageRunnable(Player player, Component message) {
        return switch (config.messageType()) {

            case 0 -> () -> player.sendMessage(message);
            case 1 -> () -> player.showTitle(Title.title(message,
                    Component.empty(),
                    Title.Times.times(
                            Duration.ofMillis(config.titleFadeIn()),
                            Duration.ofMillis(config.titleStay()),
                            Duration.ofMillis(config.titleFadeOut())
                    )));
            case 2 -> () -> player.sendActionBar(message);
            default -> throw new IllegalStateException("Unexpected message type: " + config.messageType());

        };
    }

    private Component colorize(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

}
