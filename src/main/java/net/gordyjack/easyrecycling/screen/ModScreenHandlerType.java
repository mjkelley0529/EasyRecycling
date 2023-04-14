package net.gordyjack.easyrecycling.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlerType extends ScreenHandlerType {
    public ModScreenHandlerType(Factory factory, FeatureSet requiredFeatures) {
        super(factory, requiredFeatures);
    }

    public static final ScreenHandlerType<RecyclingScreenHandler> RECYCLING_TABLE = register("recycling_table",RecyclingScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String id, Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, EasyRecycling.getID(id), new ScreenHandlerType<T>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerScreenHandlerTypes() {
        EasyRecycling.logDebug("Registering Screen Handler Types");
    }
}
