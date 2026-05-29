package com.binhjcao.graveler.action;

public interface Action {
  default double weight() {
    return 1.0;
  }

  void apply(ActionContext context);
}
