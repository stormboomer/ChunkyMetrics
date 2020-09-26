package de.stormboomer.chunky.plugin;

import com.google.gson.reflect.TypeToken;
import de.stormboomer.chunky.plugin.models.HardwareInfo;
import de.stormboomer.chunky.plugin.models.MetricStat;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import se.llbit.chunky.PersistentSettings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetricCollector {
    private MetricPlugin plugin;
    private boolean enabled = false;
    private Object lock = new Object();;
    public void setEnable(boolean b){
        synchronized(lock) {
            enabled = b;
        }
    }
    public boolean isEnabled(){
        synchronized(lock) {
            return(enabled);
        }
    }


    MetricCollector(MetricPlugin plugin){
        this.plugin = plugin;
        pid = plugin.os.getProcessId();
    }

    private HardwareInfo hwInfo;
    private int pid;
    public MetricStat collectStats(){
        OSProcess osProcess = plugin.os.getProcess(pid);

        try {
            Thread.sleep(plugin.config.cpuInterval * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //double mainCPU = osProcess.getProcessCpuLoadBetweenTicks(osProcess) / new SystemInfo().getHardware().getProcessor().getLogicalProcessorCount() * 100 * 100;
        MetricStat stat = new MetricStat(osProcess, hwInfo, plugin.chunky, plugin.metricRenderListener);
        return stat;
    }

    private List<MetricStat> allStats = new ArrayList<>();
    public void saveStat(MetricStat stat){
        Path metricFolder = Paths.get(PersistentSettings.settingsDirectory().getAbsolutePath(), "metric_storage");
        Path metricFile = Paths.get(PersistentSettings.settingsDirectory().getAbsolutePath(), "metric_storage", "metrics." + plugin.startTime +".json");
        File metricFolderFile = new File(metricFolder.toAbsolutePath().toString());
        if(!metricFolderFile.exists() || !metricFolderFile.isDirectory()){
            metricFolderFile.mkdir();
        }


        try {
            allStats.add(stat);
            String jsonString = MetricPlugin.jsonHelperMinified.toJson(allStats);
            File metricFileFile = new File(metricFile.toAbsolutePath().toString());
            if(metricFileFile.exists()){
                //byte[] currentStatBytes = Files.readAllBytes(metricFile);
                //String currentStatInfo = new String(currentStatBytes, plugin.characterEncoding);
                //List<MetricStat> allStats = plugin.jsonHelperMinified.fromJson(currentStatInfo, new TypeToken<List<MetricStat>>(){}.getType());

                Files.write(metricFile, jsonString.getBytes(), StandardOpenOption.WRITE);
            }else{
                //String jsonString = MetricPlugin.jsonHelperMinified.toJson(stat);
                //String create = "[\n" + jsonString +"\n]";
                Files.write(metricFile,  jsonString.getBytes(), StandardOpenOption.CREATE);
            }

        }catch (IOException e) {
            //exception handling left as an exercise for the reader
            Logger.error("There was an error saving Metric data");
            e.printStackTrace();
        }
    }
    public void start(){
        Thread MetricCollectorThread = new Thread(){
            public void run(){
                hwInfo = new HardwareInfo(plugin.systemInfo);
                try{
                    MetricDataSender.registerPC(hwInfo);
                }catch(Exception ex){
                    if(MetricPlugin.config.logLevel > 6){
                        ex.printStackTrace();
                    }
                }

                while(true){
                    if(isEnabled()){
                        MetricStat stat = collectStats();
                        saveStat(stat);
                        try{
                            MetricDataSender.sendStats(hwInfo.os.ComputerID, stat);
                        }catch(Exception ex){
                            if(MetricPlugin.config.logLevel > 6){
                                ex.printStackTrace();
                            }
                        }
                    }
                    try {
                        if(isEnabled()) {
                            Thread.sleep(plugin.config.collectInterval * 1000);
                        }else{
                            Thread.sleep(1000); //if we wait for activation lets just wait a second instead
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        MetricCollectorThread.setDaemon(true);
        MetricCollectorThread.setName("MetricCollectorThread");
        MetricCollectorThread.start();
    }

}
