package com.github.alekseyvideman.goodtime.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetAddress;
import java.time.OffsetTime;
import java.time.ZoneId;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerRealTimeService {

    private final DatabaseReader databaseReader;

    public int getRealPlayerHour(InetAddress playerip) {
        int hour = 0;

        try {
            var city = databaseReader.city(playerip);
            var location = city.getLocation();
            var locationTimeZone = location.getTimeZone();
            var offsetTime = OffsetTime.now(ZoneId.of(locationTimeZone));

            hour = offsetTime.getHour();
        } catch (IOException | GeoIp2Exception e) {
            //0
        }

        return hour;
    }

    @SneakyThrows
    public void closeDataSource() {
        databaseReader.close();
    }

}
