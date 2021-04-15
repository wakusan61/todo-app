package model.todo

import lib.model.Todo.Status
import lib.model.{Todo, TodoCategory}
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText}

object CreateTodoForm {
  case class TodoData(title:String, body:String, categoryId:Long){
    def toWithNoId():Todo#WithNoId =  new Todo(
      id = None,
      categoryId = ixias.model.tag[TodoCategory][Long](categoryId),
      title = title,
      body = body,
      state = Status.TODO).toWithNoId
  }

  val form = Form(
    mapping (
      "title" -> nonEmptyText(minLength = 1,maxLength = 20),
      "body"  -> nonEmptyText(minLength = 1,maxLength = 255),
      "categoryId" -> longNumber,
    )(TodoData.apply)(TodoData.unapply)
  )
}
