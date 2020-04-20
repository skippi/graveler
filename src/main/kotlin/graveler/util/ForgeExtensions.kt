package graveler.util

import net.minecraftforge.common.util.LazyOptional

object ForgeExtensions {
    // Forge... really?
    val <T> T?.toLazyOptional: LazyOptional<T?>
        get() = when (this) {
            null -> LazyOptional.empty()
            else -> LazyOptional.of { this!! }
        }
}
