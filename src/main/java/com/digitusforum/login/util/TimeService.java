package com.digitusforum.login.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeService {
    private static Map<String, Long> starts = new HashMap<>();

    public static String startCounting(){
        String uuid = UUID.randomUUID().toString();
        starts.put(uuid, System.currentTimeMillis());
        return uuid;
    }

    public static void persistElapsedTime(String uuid, String endpoint){
        long elapsedTimeMillis = System.currentTimeMillis()-starts.get(uuid);
        float elapsedTimeInSecond = elapsedTimeMillis/1000F;
        System.out.println(endpoint + " endpoint done in " +  elapsedTimeInSecond + " seconds");
    }

    public static void persistElapsedTime(String uuid, String endpoint, int statusCode){
        long elapsedTimeMillis = System.currentTimeMillis()-starts.get(uuid);
        float elapsedTimeInSecond = elapsedTimeMillis/1000F;
        System.out.println(endpoint + " endpoint done in " +  elapsedTimeInSecond + " seconds and returned " + statusCode);
    }

    public static void persistElapsedTimeout(String uuid, String endpoint){
        long elapsedTimeMillis = System.currentTimeMillis()-starts.get(uuid);
        float elapsedTimeInSecond = elapsedTimeMillis/1000F;
        System.out.println(endpoint + " endpoint timedout in " +  elapsedTimeInSecond + " seconds");
    }
}