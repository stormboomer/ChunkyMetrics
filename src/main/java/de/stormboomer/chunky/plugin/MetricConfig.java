package de.stormboomer.chunky.plugin;

import se.llbit.json.JsonObject;

import java.io.*;

public class MetricConfig  {
    public boolean enableMetrics = true;
    public boolean enableMetricsOnline = true;
    public int collectInterval = 27;
    public int cpuInterval = 3;
    public int logLevel = 6;
    public String api = "https://chunkymetrics.stormboomer.de/";
    public String infoapi = "";

    public void save() throws IOException {
        File configFile = new File(MetricPlugin.configPath.toAbsolutePath().toString());
        String jsonConfig = MetricPlugin.jsonHelper.toJson(this);

        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(configFile.getAbsolutePath()), MetricPlugin.characterEncoding));
        out.write(jsonConfig);
        out.close();
    }
}
