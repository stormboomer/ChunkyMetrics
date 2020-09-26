package de.stormboomer.chunky.plugin;

import se.llbit.chunky.renderer.RenderMode;
import se.llbit.chunky.renderer.RenderStatusListener;

public class MetricRenderListener implements RenderStatusListener {

    MetricCollector collector;
    MetricPlugin plugin;
    public MetricRenderListener(MetricCollector c, MetricPlugin plugin){
        collector = c;
        this.plugin = plugin;
    }
    private Object renderTimeLock = new Object();
    private Object spsLock = new Object();
    private Object sppLock = new Object();

    private long renderTime = 0;
    private int sps = 0;
    private int spp = 0;

    public long getRenderTime(){
        synchronized (renderTimeLock){
            return renderTime ;
        }
    }
    public int getSPP(){
        synchronized (sppLock){
            return spp ;
        }
    }
    public int getSPS(){
        synchronized (spsLock){
            return sps ;
        }
    }


    @Override
    public void setRenderTime(long l) {
        //System.out.println("------------------- setRenderTime");
        //System.out.println("" + l);
        //System.out.println("-------------------");
        synchronized (renderTimeLock){
            renderTime = l;
        }
    }

    @Override
    public void setSamplesPerSecond(int i) {
        //System.out.println("------------------- setSamplesPerSecond");
        //System.out.println("" + i);
        //System.out.println("-------------------");
        synchronized (spsLock){
            sps = i;
        }
    }

    @Override
    public void setSpp(int i) {
        //System.out.println("------------------- setSpp");
        //System.out.println("" + i);
        //System.out.println("-------------------");
        synchronized (sppLock){
            spp = i;
        }
    }

    @Override
    public void renderStateChanged(RenderMode renderMode) {
        //Logger.debug("------------------- renderMode");
        //Logger.debug("" + renderMode.name() + " - " + plugin.chunky.getSceneManager().getScene().name);
        //Logger.debug("-------------------");
        if(plugin.config.enableMetrics){
            if(renderMode == RenderMode.RENDERING){
                Logger.debug("Render Mode Changed to: " + renderMode.name());
                collector.setEnable(true);
            }else{
                collector.setEnable(false);
            }

        }

    }
}
