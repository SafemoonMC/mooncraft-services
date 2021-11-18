package gg.mooncraft.services.minecraft.bungee.scheduler;

import me.eduardwayland.mooncraft.waylander.scheduler.AsyncScheduler;

import java.util.concurrent.Executor;

/**
 * BungeeCord has no concept of sync tasks because the entire proxy is async.
 * The "synchronous executor" is misleading.
 */
public class BungeeScheduler extends AsyncScheduler {
    
    /*
    Fields
     */
    private final Executor syncExecutor;
    
    /*
    Constructor
     */
    public BungeeScheduler() {
        super("MSMinecraft");
        this.syncExecutor = Runnable::run;
    }
    
    /*
    Override Methods
     */
    @Override
    public Executor sync() {
        return syncExecutor;
    }
}