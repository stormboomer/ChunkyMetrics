package de.stormboomer.chunky.plugin.models;

import oshi.software.os.OSFileStore;

public class FileSystemInfo {
    public String Description;
    public String Name;
    public String Label;
    public String LogicalVolume;
    public String Mount;
    public String Options;
    public String UUID;
    public String Volume;
    public long FreeInodes;
    public long FreeSpace;
    public String Type;
    public long TotalInodes;
    public long TotalSpace;
    public long UsableSpace;
    public FileSystemInfo(OSFileStore fileStore){
        Description = fileStore.getDescription();
        Name = fileStore.getName();
        Label = fileStore.getLabel();
        LogicalVolume = fileStore.getLogicalVolume();
        Mount = fileStore.getMount();
        Options = fileStore.getOptions();
        UUID = fileStore.getUUID();
        Volume = fileStore.getVolume();
        FreeInodes = fileStore.getFreeInodes();
        FreeSpace = fileStore.getFreeSpace();
        Type = fileStore.getType();
        TotalInodes = fileStore.getTotalInodes();
        TotalSpace = fileStore.getTotalSpace();
        UsableSpace = fileStore.getUsableSpace();
    }
}
