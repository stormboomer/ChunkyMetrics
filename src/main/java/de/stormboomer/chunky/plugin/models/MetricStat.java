package de.stormboomer.chunky.plugin.models;

import de.stormboomer.chunky.plugin.MetricCollector;
import de.stormboomer.chunky.plugin.MetricPlugin;
import de.stormboomer.chunky.plugin.MetricRenderListener;
import oshi.SystemInfo;
import oshi.hardware.VirtualMemory;
import oshi.software.os.OSProcess;
import se.llbit.chunky.PersistentSettings;
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
    public int ChunkyThreadCount;
    public int ChunkyCPULoad;
    public long UserTime;
    public long KernelTime;

    public long SwapPagesIn;
    public long SwapPagesOut;
    public long SwapTotal;
    public long SwapUsed;
    public long VirtualMax;
    public long VirtualUsed;

    public long RenderTime = 0;
    public int sps = 0;
    public int spp = 0;

    public long RunNumber = 0;

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
        ChunkyThreadCount = PersistentSettings.getNumThreads();
        ChunkyCPULoad = PersistentSettings.getCPULoad();

        VirtualMemory virtualMemory = MetricPlugin.systemInfo.getHardware().getMemory().getVirtualMemory();
        SwapPagesIn =  virtualMemory.getSwapPagesIn();
        SwapPagesOut = virtualMemory.getSwapPagesOut();
        SwapTotal = virtualMemory.getSwapTotal();
        SwapUsed = virtualMemory.getSwapUsed();
        VirtualUsed = virtualMemory.getVirtualInUse();
        VirtualMax = virtualMemory.getVirtualMax();

        RenderTime = listener.getRenderTime();
        sps = listener.getCalcSPS();
        spp = listener.getSPP();

        RunNumber = MetricPlugin.getStartTime();

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
