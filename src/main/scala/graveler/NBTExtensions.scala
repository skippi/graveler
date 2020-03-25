package graveler

import scala.collection.mutable.Set
import scala.collection.mutable.HashSet
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString

object NBTExtensions {
  implicit class ExpandedNBTTagList(private val list: NBTTagList) {
    def toStringSet: Set[String] = {
      val result = new HashSet[String]
      (0 until list.tagCount) foreach { result += list.getStringTagAt(_) }

      result
    }
  }

  implicit class NBTExpandedStringSet(private val stringSet: Set[String]) {
    def toTagList: NBTTagList = {
      val result = new NBTTagList
      stringSet foreach { str =>
        result.appendTag(new NBTTagString(str))
      }

      result
    }
  }
}
