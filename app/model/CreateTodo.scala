package model

import controllers.routes
import play.api.data.{Form, FormError}
import play.api.mvc.Call


case class ViewValueCreateTodo (
  title:            String                  = "Todo作成",
  cssSrc:           Seq[String]             = Seq("main.css"),
  jsSrc:            Seq[String]             = Seq("main.js"),
  action:           Call                    = routes.TodoController.create,
  form:             Form[TodoForm.TodoData] = TodoForm.form
) extends ViewValueCommon
