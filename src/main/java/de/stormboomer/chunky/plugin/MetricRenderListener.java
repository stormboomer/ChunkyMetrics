package de.stormboomer.chunky.plugin;

import se.llbit.chunky.renderer.RenderMode;
import se.llbit.chunky.renderer.RenderStatusListener;

import java.util.Date;

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

    private long lastTime = 0;
    private int calc_sps = 0;

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
    public int getCalcSPS(){
        synchronized (sppLock){
            return calc_sps ;
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
        long nowTime = new Date().getTime();
        double timeDiff = (nowTime - lastTime);
        double result = 0;

        if(timeDiff > 0){
            result =  (pixels / (timeDiff / 1000));
        }

        //Logger.info(" SPS: " + calc_sps + " with SPP: " + i);

        lastTime = new Date().getTime();
        synchronized (sppLock){
            spp = i;
            calc_sps = (int) result;
        }


    }

    private long pixels = 0;
    @Override
    public void renderStateChanged(RenderMode renderMode) {
        //Logger.debug("------------------- renderMode");
        //Logger.debug("" + renderMode.name() + " - " + plugin.chunky.getSceneManager().getScene().name);
        //Logger.debug("-------------------");
        pixels = plugin.chunky.getSceneManager().getScene().width *  plugin.chunky.getSceneManager().getScene().height;
        //Logger.info("PXIELS: " + pixels);
        lastTime = new Date().getTime();
        if(plugin.config.enableMetrics){
            if(renderMode == RenderMode.RENDERING){
                Logger.debug("Render Mode Changed to: " + renderMode.name());
                collector.setEnable(true);
            }else{
                collector.setEnable(false);
                plugin.setStartTime(new Date().getTime());
            }

        }

    }
}
