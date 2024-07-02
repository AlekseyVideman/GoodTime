package com.github.alekseyvideman.goodtime.configuration.dto;

public record ConfigurationDto(
        String goodMorning,
        String goodAfternoon,
        String goodEvening,
        String goodNight,

        long messageDelay,
        int messageType,

        int titleFadeIn,
        int titleStay,
        int titleFadeOut
) {}
