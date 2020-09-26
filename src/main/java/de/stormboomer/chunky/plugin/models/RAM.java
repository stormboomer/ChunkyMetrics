package de.stormboomer.chunky.plugin.models;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.PhysicalMemory;

import java.util.ArrayList;
import java.util.List;

public class RAM {
    public long Available;
    public long PageSize;
    public long Total;
    public RAMVirtual Virtual;
    public List<RAMModule> Modules = new ArrayList<>();

    public RAM(SystemInfo systemInfo){
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        Available = memory.getAvailable();
        PageSize = memory.getPageSize();
        Total = memory.getTotal();
        Virtual = new RAMVirtual(memory.getVirtualMemory());
        for(PhysicalMemory mem : systemInfo.getHardware().getMemory().getPhysicalMemory()){
            Modules.add(new RAMModule(mem));
        }
    }
}
