package de.stormboomer.chunky.plugin;

import se.llbit.json.JsonObject;

public class MetricConfig  {
    public boolean enableMetrics = true;
    public boolean enableMetricsOnline = true;
    public int collectInterval = 27;
    public int cpuInterval = 3;
    public int logLevel = 6;
    public String api = "https://chunkymetrics.stormboomer.de/";
}
