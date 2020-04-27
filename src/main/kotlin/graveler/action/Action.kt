package graveler.action

interface Action {
  val weight: Int get() = 1

  fun apply(context: ActionContext)
}
