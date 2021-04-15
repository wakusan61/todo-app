package model

import controllers.routes
import lib.model.Todo.Status
import play.api.data.Form
import play.api.mvc.Call

case class ViewValueUpdateTodo (
  title:               String                         = "Todo更新",
  cssSrc:              Seq[String]                    = Seq("main.css"),
  jsSrc:               Seq[String]                    = Seq("main.js"),
  action:              Call                           = routes.TodoController.update,
  form:                Form[UpdateTodoForm.TodoData]  = UpdateTodoForm.form,
  todoCategorySelect:  Seq[(String, String)]          = Nil,
  stateSelect:         Seq[(String, String)]          = Status.values.map(
    status => (status.code.toString , status.name)
  )
) extends ViewValueCommon
