package gg.mooncraft.services.restfulweb.scheduler;

import gg.mooncraft.services.restfulweb.ApplicationBootstrap;
import me.eduardwayland.mooncraft.waylander.scheduler.AsyncScheduler;
import me.eduardwayland.mooncraft.waylander.scheduler.Scheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppScheduler extends AsyncScheduler implements Scheduler {
    
    /*
    Fields
     */
    private final Executor syncExecutor;
    
    /*
    Constructor
     */
    public AppScheduler() {
        super(ApplicationBootstrap.getLogger().getName());
        this.syncExecutor = Executors.newSingleThreadExecutor();
    }
    
    /*
    Override Methods
     */
    @Override
    public Executor sync() {
        return syncExecutor;
    }
}