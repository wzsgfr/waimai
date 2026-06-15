package com.sky.utils;

import com.sky.properties.BaiduMapConfig;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SearchHttpAK {

    private static final String GEOCODING_URL = "https://api.map.baidu.com/geocoding/v3/";
    private static final double DISTANCE_THRESHOLD = 5000.0; // 5公里

    private final String ak;

    public SearchHttpAK(BaiduMapConfig baiduMapConfig) {
        this.ak = baiduMapConfig.getAk();
    }

    public boolean isDistanceExceed5Km(String address1, String address2) throws Exception {
        double[] coord1 = getCoordinate(address1);
        double[] coord2 = getCoordinate(address2);
        double distance = calculateDistance(coord1[0], coord1[1], coord2[0], coord2[1]);
        return distance > DISTANCE_THRESHOLD;
    }

    private double[] getCoordinate(String address) throws Exception {
        String params = "address=" + URLEncoder.encode(address, StandardCharsets.UTF_8) +
                "&output=json&ak=" + ak;
        String fullUrl = GEOCODING_URL + "?" + params;
        String response = httpGet(fullUrl);
        return parseLocation(response);
    }

    private String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private double[] parseLocation(String json) throws Exception {
        Pattern lngPattern = Pattern.compile("\"lng\"\\s*:\\s*([\\d.]+)");
        Pattern latPattern = Pattern.compile("\"lat\"\\s*:\\s*([\\d.]+)");
        Matcher lngMatcher = lngPattern.matcher(json);
        Matcher latMatcher = latPattern.matcher(json);

        if (!lngMatcher.find() || !latMatcher.find()) {
            throw new Exception("解析经纬度失败，响应内容：" + json);
        }

        double lng = Double.parseDouble(lngMatcher.group(1));
        double lat = Double.parseDouble(latMatcher.group(1));
        return new double[]{lng, lat};
    }

    public double calculateDistance(double lng1, double lat1, double lng2, double lat2) {
        final double EARTH_RADIUS = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}