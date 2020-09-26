package de.stormboomer.chunky.plugin.models;

import de.stormboomer.chunky.plugin.Logger;
import de.stormboomer.chunky.plugin.MetricPlugin;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

public class OperatingSystemInfo {
    public String Family;
    public String Manufacturer;
    public String Version;
    public int Bitness;
    public String ComputerID;

    public OperatingSystemInfo(SystemInfo systemInfo){
        OperatingSystem os = systemInfo.getOperatingSystem();
        Version = os.getVersionInfo().toString();
        Manufacturer = os.getManufacturer();
        Family = os.getFamily();
        Bitness = os.getBitness();
        ComputerID = getComputerIdentifier(systemInfo);

    }
    public String getComputerIdentifier(SystemInfo systemInfo) {
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

        String vendor = operatingSystem.getManufacturer();
        String processorSerialNumber = computerSystem.getSerialNumber();
        String processorIdentifier = centralProcessor.getProcessorIdentifier().getIdentifier();
        String OS = Manufacturer + " " + Family + " " + Version + " " + Bitness;
        String Memory = hardwareAbstractionLayer.getMemory().getTotal() + "";
        int processors = centralProcessor.getLogicalProcessorCount();

        String delimiter = "-";

        return String.format("%08x", vendor.hashCode()) + delimiter
                + String.format("%08x", processorSerialNumber.hashCode()) + delimiter
                + String.format("%08x", processorIdentifier.hashCode()) + delimiter
                + String.format("%08x", OS.hashCode()) + delimiter
                + String.format("%08x", Memory.hashCode()) + delimiter + processors;
    }
}
