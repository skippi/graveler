package graveler

import net.minecraft.util.text._

object CaoLocale {
  def createModText(msg: String): TextComponentString = {
    new TextComponentString(
      s"${TextFormatting.GREEN}cao:${TextFormatting.RESET} ${msg}"
        .replace("\r", "")
    )
  }

  def createLivesCountText(lives: Int): TextComponentString =
    createModText(s"You have ${lives} lives left.")

  def createActivateCurseText(lives: Lives): TextComponentString = {
    assert(lives.curseTicks != 0)

    if (lives.curseTicks == -1)
      createModText(
        s"You've been permanently ${TextFormatting.DARK_RED}cursed${TextFormatting.RESET}."
      )
    else
      createModText(
        s"You've been ${TextFormatting.RED}cursed${TextFormatting.RESET} for ${TickMath
          .ticksToSeconds(lives.curseTicks)} seconds."
      )
  }

  def createLivesRespawnText(lives: Lives): TextComponentString = {
    var msg = s"${createLivesCountText(lives.count).getText}"

    if (lives.isCursed) {
      msg += s"\n${createActivateCurseText(lives).getText}"
    }

    new TextComponentString(msg)
  }

  def createLivesStatusText(lives: Lives) = {
    new TextComponentString(
      s"${createLivesCountText(lives.count).getText}\n${createCurseStatusText(lives).getText}"
    )
  }

  def createCurseStatusText(lives: Lives) = {
    if (lives.curseTicks == -1)
      createModText(
        s"You are permanently ${TextFormatting.DARK_RED}cursed${TextFormatting.RESET}."
      )
    else if (lives.curseTicks > 0)
      createModText(
        s"You are ${TextFormatting.RED}cursed${TextFormatting.RESET} for ${TickMath
          .ticksToSeconds(lives.curseTicks)} more seconds."
      )
    else
      createModText("No active curse.")
  }

  def createGameStatusText(activePlayerCount: Int, ticks: Long) = {
    val totalSeconds = TickMath.ticksToSeconds(ticks)
    val minutes = totalSeconds / 60
    val remSeconds = totalSeconds % 60

    val phase =
      if (activePlayerCount > 0) s"${TextFormatting.GREEN}Active"
      else s"${TextFormatting.DARK_RED}End"
    val playerCountLabel =
      if (activePlayerCount > 0) s"${TextFormatting.GREEN}${activePlayerCount}"
      else s"${TextFormatting.DARK_RED}${activePlayerCount}"

    createModText(
      s"""
      |- Phase: ${phase}${TextFormatting.RESET}
      |- Alive Player Count: ${playerCountLabel}${TextFormatting.RESET}
      |- Time: ${minutes}:${remSeconds}""".stripMargin
    )
  }
}
