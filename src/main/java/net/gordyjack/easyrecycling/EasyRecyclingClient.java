package net.gordyjack.easyrecycling;

import net.fabricmc.api.ClientModInitializer;
import net.gordyjack.easyrecycling.screen.ModScreenHandlerType;
import net.gordyjack.easyrecycling.screen.RecyclingScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class EasyRecyclingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlerType.RECYCLING_TABLE, RecyclingScreen::new);
    }
}
