package de.stormboomer.chunky.plugin.models;

import oshi.hardware.VirtualMemory;

public class RAMVirtual {

    public long SwapPagesIn;
    public long SwapPagesOut;
    public long SwapTotal;
    public long SwapUsed;
    public long VirtualMax;
    public long VirtualUsed;

    public RAMVirtual(VirtualMemory virtualMemory){
        SwapPagesIn =  virtualMemory.getSwapPagesIn();
        SwapPagesOut = virtualMemory.getSwapPagesOut();
        SwapTotal = virtualMemory.getSwapTotal();
        SwapUsed = virtualMemory.getSwapUsed();
        VirtualUsed = virtualMemory.getVirtualInUse();
        VirtualMax = virtualMemory.getVirtualMax();
    }
}
