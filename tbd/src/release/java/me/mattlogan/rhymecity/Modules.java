package me.firefly.rhymecity;

import com.metech.tbd.AppModule;

public final class Modules {
    public static Object[] list(String apiKey) {
        return new Object[] {
                new AppModule(apiKey),
                //new DebugAppModule()
        };
    }

    private Modules() {
        // No instances.
    }
}
