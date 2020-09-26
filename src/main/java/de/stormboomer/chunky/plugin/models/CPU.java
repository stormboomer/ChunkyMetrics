package de.stormboomer.chunky.plugin.models;

import javafx.scene.shape.Arc;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.software.os.OperatingSystem;

public class CPU{
    public int LogicalCoreCount;
    public int PhysicalCoreCount;
    public int SocketCount;
    public long MaxFreq;
    public String Name;
    public String Vendor;
    public String Architecture;
    public long VendorFreq;
    public String ProcessorID;

    public CPU(SystemInfo systemInfo){
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        CentralProcessor.ProcessorIdentifier processorID = processor.getProcessorIdentifier();
        LogicalCoreCount = processor.getLogicalProcessorCount();
        PhysicalCoreCount = processor.getPhysicalProcessorCount();
        SocketCount = processor.getPhysicalPackageCount();
        MaxFreq = processor.getMaxFreq();
        Name = processorID.getName();
        Vendor = processorID.getVendor();
        Architecture = processorID.getMicroarchitecture();
        VendorFreq = processorID.getVendorFreq();
        ProcessorID = processorID.getProcessorID();
    }
}
