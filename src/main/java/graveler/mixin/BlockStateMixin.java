package graveler.mixin;

import graveler.Mixins;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {
  @Inject(method = "onPlace", at = @At("HEAD"))
  private void graveler$onPlace(
      Level level, BlockPos pos, BlockState state, boolean movedByPiston, CallbackInfo ci) {
    Mixins.onBlockAdded(level, pos);
  }
}
