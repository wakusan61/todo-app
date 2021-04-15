package model

import ixias.model.tag
import lib.model.Todo.Status
import lib.model.{Todo, TodoCategory}
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText, shortNumber}

object UpdateTodoForm {
  case class TodoData(id:Long,title:String,body:String,categoryId:Long,state:Short) {
    def toEmbeddedId():Todo#EmbeddedId = new Todo (
      id            = Some(tag[Todo][Long](id)),
      categoryId    = tag[TodoCategory][Long](categoryId),
      title         = title,
      body          = body,
      state         = Status(state)
    ).toEmbeddedId
  }

  val form = Form(
    mapping (
      "id"          -> longNumber,
      "title"       -> nonEmptyText(minLength = 1,maxLength = 20),
      "body"        -> nonEmptyText(minLength = 1,maxLength = 255),
      "categoryId"  -> longNumber,
      "state"       -> shortNumber
    )(TodoData.apply)(TodoData.unapply)
  )
}
