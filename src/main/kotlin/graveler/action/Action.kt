package graveler.action

interface Action {
  val weight: Double get() = 1.0

  fun apply(context: ActionContext)
}
