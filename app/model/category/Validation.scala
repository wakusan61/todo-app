package model.category

import play.api.data.validation.Constraints

object Validation {
  val alphabeticPattern = Constraints.pattern("[a-zA-Z0-9]+".r)
}