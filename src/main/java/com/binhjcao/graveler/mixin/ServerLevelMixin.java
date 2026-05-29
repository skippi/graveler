package com.binhjcao.graveler.mixin;

import com.binhjcao.graveler.Mixins;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
  @Inject(
      method =
          "updateNeighborsAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/redstone/Orientation;)V",
      at = @At("HEAD"))
  private void graveler$updateNeighborsAt(
      BlockPos pos, Block block, Orientation orientation, CallbackInfo ci) {
    Mixins.updateNeighbors((ServerLevel) (Object) this, pos);
  }
}
