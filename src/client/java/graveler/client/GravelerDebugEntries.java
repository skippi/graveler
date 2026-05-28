package graveler.client;

import graveler.GravelerMod;
import graveler.mixin.client.DebugScreenEntriesInvoker;
import net.minecraft.client.gui.components.debug.DebugEntryNoop;
import net.minecraft.resources.Identifier;

public final class GravelerDebugEntries {
  public static final Identifier VISUALIZE_STRESS =
      DebugScreenEntriesInvoker.graveler$register(
          Identifier.fromNamespaceAndPath(GravelerMod.MOD_ID, "visualize_stress"),
          new DebugEntryNoop());

  private GravelerDebugEntries() {}
}
