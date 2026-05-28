package graveler;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public final class GravelerAttachments {
  public static final AttachmentType<Scheduler> SCHEDULER =
      AttachmentRegistry.createDefaulted(
          Identifier.fromNamespaceAndPath(GravelerMod.MOD_ID, "scheduler"), Scheduler::new);

  public static final AttachmentType<StressMap> STRESS_MAP =
      AttachmentRegistry.create(
          Identifier.fromNamespaceAndPath(GravelerMod.MOD_ID, "stress_map"),
          builder -> builder.initializer(StressMap::new).persistent(StressMap.CODEC));

  private GravelerAttachments() {}

  static void register() {}
}
