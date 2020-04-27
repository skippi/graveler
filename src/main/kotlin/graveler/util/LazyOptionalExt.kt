@file:JvmMultifileClass

package io.skippi.cotm.util

import net.minecraftforge.common.util.LazyOptional

// Forge... really?
val <T> T?.toLazyOptional: LazyOptional<T?>
  get() = when (this) {
    null -> LazyOptional.empty()
    else -> LazyOptional.of { this!! }
  }

val <T> LazyOptional<T>.toNullable: T?
  get() = orElse(null)
