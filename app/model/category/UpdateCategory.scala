package model.category

import controllers.routes
import lib.model.TodoCategory.Color
import model.ViewValueCommon
import play.api.data.Form
import play.api.mvc.Call

case class ViewValueUpdateCategory (
  title:        String                                = "カテゴリー更新",
  cssSrc:       Seq[String]                           = Seq("main.css"),
  jsSrc:        Seq[String]                           = Seq("main.js"),
  action:       Call                                  = routes.CategoryController.update,
  form:         Form[UpdateCategoryForm.CategoryData] = UpdateCategoryForm.form,
  colorSelect:  Seq[(String,String)]                  = Color.values.map(color => (color.code.toString,color.name))
) extends ViewValueCommon
