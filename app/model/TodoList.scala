package model

import controllers.routes

case class ViewValueTodoList(
  title:            String = "Todo一覧",
  cssSrc:           Seq[String] = Seq("main.css"),
  jsSrc:            Seq[String] = Seq("main.js"),
  todoList:         Seq[ViewValueTodo] = Nil,
  link:             Map[String,String] = Map(
    "createForm" -> routes.TodoController.showCreateForm().url
  ),
  errors:           Seq[String] = Nil
) extends ViewValueCommon
