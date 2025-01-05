package com.github.alekseyvideman.goodtime.listener;

import com.github.alekseyvideman.goodtime.service.PlayerTimeGreetingService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerJoinListener implements Listener {

    private final PlayerTimeGreetingService timeGreetingService;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        timeGreetingService.sendGreeting(e.getPlayer());
    }
}
