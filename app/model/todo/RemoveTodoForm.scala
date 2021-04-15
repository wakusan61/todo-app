package model.todo

import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping}

object RemoveTodoForm {
  case class TodoData(id:Long)

  val form = Form(
      mapping ("id" -> longNumber)(TodoData.apply)(TodoData.unapply)
  )
}
