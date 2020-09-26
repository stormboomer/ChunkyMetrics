package de.stormboomer.chunky.plugin.models;

import de.stormboomer.chunky.plugin.MetricCollector;
import de.stormboomer.chunky.plugin.MetricRenderListener;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import se.llbit.chunky.main.Chunky;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetricStat {
    public String Time;
    public String Scene;

    public double CPUUsage;
    public long ResidentSetSize;
    public long VirtualMemorySize;
    public long MajorFaults;
    public long MinorFaults;
    public int Priority;
    public int ThreadCount;
    public long UserTime;
    public long KernelTime ;

    public long RenderTime = 0;
    public int sps = 0;
    public int spp = 0;

    public MetricStat(OSProcess osProcess, HardwareInfo hwInfo, Chunky chunky, MetricRenderListener listener){
        CPUUsage = roundDouble(osProcess.getProcessCpuLoadBetweenTicks(osProcess) / hwInfo.cpu.LogicalCoreCount * 100, 2);
        ResidentSetSize = osProcess.getResidentSetSize();
        VirtualMemorySize = osProcess.getVirtualSize();
        MajorFaults = osProcess.getMajorFaults();
        MinorFaults = osProcess.getMinorFaults();
        Priority = osProcess.getPriority();
        ThreadCount = osProcess.getThreadCount();
        UserTime =  osProcess.getUserTime();
        KernelTime = osProcess.getKernelTime();

        RenderTime = listener.getRenderTime();
        sps = listener.getSPS();
        spp = listener.getSPP();

        Scene = chunky.getSceneManager().getScene().name;

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Time = formatter.format(date);
    }
    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
