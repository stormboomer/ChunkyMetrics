package de.stormboomer.chunky.plugin.models;

import oshi.hardware.PhysicalMemory;

public class RAMModule {

    public long Capacity;
    public String MemoryType;
    public String Manufacturer;
    public String BankLabel;
    public long ClockSpeed;

    public RAMModule(PhysicalMemory memory){
        Capacity = memory.getCapacity();
        MemoryType = memory.getMemoryType();
        Manufacturer = memory.getManufacturer();
        BankLabel = memory.getBankLabel();
        ClockSpeed = memory.getClockSpeed();
    }
}
