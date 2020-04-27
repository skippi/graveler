package graveler.action

interface Action {
  val priority: Int

  fun apply(context: ActionContext)
}
