package com.binhjcao.graveler.mixin.client;

import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(net.minecraft.client.gui.components.debug.DebugScreenEntries.class)
public interface DebugScreenEntriesInvoker {
  @Invoker("register")
  static Identifier graveler$register(Identifier id, DebugScreenEntry entry) {
    throw new AssertionError();
  }
}
