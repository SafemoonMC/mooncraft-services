package mc.mooncraft.services.minecraft.bungee.api;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public final class ApiRegistrationUtility {
    
    /*
    Fields
     */
    private static final Method REGISTER;
    private static final Method UNREGISTER;
    
    /*
    Initializer
     */
    static {
        try {
            REGISTER = MSMinecraftProvider.class.getDeclaredMethod("register", MSMinecraft.class);
            REGISTER.setAccessible(true);
            
            UNREGISTER = MSMinecraftProvider.class.getDeclaredMethod("unregister");
            UNREGISTER.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /*
    Methods
     */
    public static void registerProvider(@NotNull MSMinecraft instance) {
        try {
            REGISTER.invoke(null, instance);
            System.out.println("MCSBungee provider has been registered.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void unregisterProvider() {
        try {
            UNREGISTER.invoke(null);
            System.out.println("MCSBungee provider has been unregistered.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}