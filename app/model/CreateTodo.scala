package model

import controllers.routes
import play.api.data.Form
import play.api.mvc.Call


case class ViewValueCreateTodo (
                                 title:               String                     = "Todo作成",
                                 cssSrc:              Seq[String]                = Seq("main.css"),
                                 jsSrc:               Seq[String]                = Seq("main.js"),
                                 action:              Call                       = routes.TodoController.create,
                                 form:                Form[CreateTodoForm.TodoData]    = CreateTodoForm.form,
                                 todoCategorySelect:  Seq[(String, String)]      = Nil
) extends ViewValueCommon
