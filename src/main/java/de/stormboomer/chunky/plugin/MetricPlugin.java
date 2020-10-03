package de.stormboomer.chunky.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.stormboomer.chunky.plugin.models.HardwareInfo;
import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import se.llbit.chunky.PersistentSettings;
import se.llbit.chunky.Plugin;
import se.llbit.chunky.main.Chunky;
//import se.llbit.chunky.plugin.AmbientOcclusion;
import se.llbit.chunky.renderer.RenderController;
import se.llbit.log.Log;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class MetricPlugin implements Plugin {
    public static Gson jsonHelper = new GsonBuilder().setPrettyPrinting().create();
    public static Gson jsonHelperMinified = new Gson();
    public static Charset characterEncoding = StandardCharsets.UTF_8;
    public static Path configPath = Paths.get(PersistentSettings.settingsDirectory().getAbsolutePath(), "metric_settings.json");

    public static MetricConfig config = null;
    public static SystemInfo systemInfo;
    public static OperatingSystem os;

    private static Object startTimeLock = new Object();
    public static void setStartTime(long st){
        synchronized (startTimeLock){
            startTime = st;
        }
    }
    public static long getStartTime(){
        synchronized (startTimeLock){
            return startTime;
        }
    }
    private static long startTime;

    public MetricConfig getMetricConfig() throws IOException {
        if(config != null){
            return config;
        }
        File configFile = new File(configPath.toAbsolutePath().toString());
        MetricConfig cfg = new MetricConfig();
        if(!configFile.exists()){
           /* String jsonConfig = jsonHelper.toJson(cfg);

            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(configFile.getAbsolutePath()), characterEncoding));
            out.write(jsonConfig);
            out.close();

            */
            cfg.save();
        }else if(configFile.exists() && !configFile.isDirectory()){
            byte[] configFileBytes = Files.readAllBytes(configPath.toAbsolutePath());
            String jsonConfig = new String(configFileBytes, characterEncoding);
            cfg = jsonHelper.fromJson(jsonConfig, MetricConfig.class);
        }else{
            throw new FileNotFoundException("The configuration file for ChunkyMetrics could not be found / loaded");
        }
        config = cfg;
        return cfg;
    }

    public Chunky chunky;
    public MetricRenderListener metricRenderListener;
    public MetricCollector metricCollector;
    @Override
    public void attach(Chunky chunky) {
        this.chunky = chunky;
        setStartTime(new Date().getTime());
        try{
            //Log.info("Attached to chunky BLA " + configPath.toAbsolutePath());
            getMetricConfig();
            systemInfo = new SystemInfo();
            os = systemInfo.getOperatingSystem();
            Logger.debug("Current PID: " + os.getProcessId());
            //Logger.info(config.enableMetrics + " - " + config.enableMetricsOnline);
            Chunky.loadDefaultTextures();
            RenderController rc = chunky.getRenderController();
            metricCollector = new MetricCollector(this);
            metricRenderListener = new MetricRenderListener(metricCollector, this);
            rc.getRenderer().addRenderListener(metricRenderListener);
            metricCollector.start();

        }catch(Exception ex){
            Logger.error("Error while enabling ChunkyMetrics");
            ex.printStackTrace();
        }

    }
    public static void main(String[] args) throws Exception {
        //this is not triggered dont know why
        // Start Chunky normally with this plugin attached.
        Log.info("Started Metrics");
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();

        double temp = systemInfo.getHardware().getSensors().getCpuTemperature();
        int fans[] = systemInfo.getHardware().getSensors().getFanSpeeds();
        long[] currentFreq = systemInfo.getHardware().getProcessor().getCurrentFreq();
        HardwareInfo HardwareInfo = new HardwareInfo(systemInfo);

        String json = jsonHelper.toJson(HardwareInfo);
        /*
        Chunky.loadDefaultTextures();
        Chunky chunky = new Chunky(ChunkyOptions.getDefaults());
        //new AmbientOcclusion().attach(chunky);
        ChunkyFx.startChunkyUI(chunky);
        */
    }

}
