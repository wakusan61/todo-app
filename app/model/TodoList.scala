package model

import controllers.routes
import lib.model.Todo
import play.api.mvc.{Action, Call, Result}

case class ViewValueTodoList(
  title:            String = "Todo一覧",
  cssSrc:           Seq[String] = Seq("main.css"),
  jsSrc:            Seq[String] = Seq("main.js"),
  todoList:         Seq[ViewValueTodo] = Nil,
  removeCall:       (Todo.Id) => Call = (id:Todo.Id) => controllers.routes.TodoController.remove(id),
  link:             Map[String,String] = Map(
    "createForm" -> routes.TodoController.showCreateForm().url,
    "home"       -> routes.HomeController.index().url
  ),
  errors:           Seq[String] = Nil
) extends ViewValueCommon
