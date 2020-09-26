package de.stormboomer.chunky.plugin;

public class Logger {
    public static String pluginName = "[ChunkyMetrics]";
    public static void debug(String s){
        if(MetricPlugin.config.logLevel >= 7){
            System.out.println(pluginName + "[DEBUG] " + s);
        }
    }
    public static void info(String s){
        System.out.println(pluginName + "[INFO] " + s);
    }
    public static void error(String s){
        System.out.println(pluginName + "[ERROR] " + s);
    }
    public static void warn(String s){
        System.out.println(pluginName + "[WARN] " + s);
    }
}
