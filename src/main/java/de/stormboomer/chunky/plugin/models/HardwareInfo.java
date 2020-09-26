package de.stormboomer.chunky.plugin.models;

import de.stormboomer.chunky.plugin.Logger;
import de.stormboomer.chunky.plugin.MetricPlugin;
import oshi.SystemInfo;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;

public class HardwareInfo {
    public HardwareInfo(SystemInfo systemInfo){
        cpu = new CPU(systemInfo);
        ram = new RAM(systemInfo);
        os = new OperatingSystemInfo(systemInfo);

        List<OSFileStore> filestores = systemInfo.getOperatingSystem().getFileSystem().getFileStores();

        for(OSFileStore fileStore : filestores){
            fileSystemInfo.add(new FileSystemInfo(fileStore));
        }

        //Logger.debug("Extracted the following HW information: \n" + MetricPlugin.jsonHelper.toJson(this));
    }
    public CPU cpu;
    public RAM ram;
    public List<FileSystemInfo> fileSystemInfo = new ArrayList<FileSystemInfo>();
    public OperatingSystemInfo os;

}

