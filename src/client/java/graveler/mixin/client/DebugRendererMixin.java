package graveler.mixin.client;

import graveler.client.GravelerDebugEntries;
import graveler.client.StressDebugRenderer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public abstract class DebugRendererMixin {
  @Shadow private List<DebugRenderer.SimpleDebugRenderer> renderers;

  @Inject(method = "refreshRendererList", at = @At("TAIL"))
  private void graveler$addStressRenderer(CallbackInfo ci) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.debugEntries.isCurrentlyEnabled(GravelerDebugEntries.VISUALIZE_STRESS)) {
      renderers.add(new StressDebugRenderer());
    }
  }
}
