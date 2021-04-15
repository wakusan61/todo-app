package model.category

import controllers.routes
import lib.model.TodoCategory.Color
import model.ViewValueCommon
import play.api.data.Form
import play.api.mvc.Call

case class ViewValueCreateCategory (
  title:        String                                = "カテゴリー一覧",
  cssSrc:       Seq[String]                           = Seq("main.css"),
  jsSrc:        Seq[String]                           = Seq("main.js"),
  action:       Call                                  = routes.CategoryController.create,
  form:         Form[CreateCategoryForm.CategoryData] = CreateCategoryForm.form,
  colorSelect:  Seq[(String,String)]                  = Color.values.map(color => (color.code.toString,color.name))
) extends ViewValueCommon
