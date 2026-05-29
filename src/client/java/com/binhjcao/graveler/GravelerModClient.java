package com.binhjcao.graveler;

import com.binhjcao.graveler.client.GravelerDebugEntries;
import com.binhjcao.graveler.client.StressDebugRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.resources.Identifier;

public class GravelerModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    Identifier visualizeStress = GravelerDebugEntries.VISUALIZE_STRESS;

    ClientTickEvents.END_CLIENT_TICK.register(
        minecraft -> {
          if (minecraft.debugEntries.isCurrentlyEnabled(visualizeStress)) {
            StressDebugRenderer.refresh(minecraft);
          } else {
            StressDebugRenderer.clear();
          }
        });
  }
}
