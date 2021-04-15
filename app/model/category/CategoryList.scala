package model.category

import controllers.routes
import lib.model.TodoCategory
import lib.model.TodoCategory.Color
import model.ViewValueCommon

case class ViewValueCategoryList(
  title:               String                         = "カテゴリー一覧",
  cssSrc:              Seq[String]                    = Seq("main.css"),
  jsSrc:               Seq[String]                    = Seq("main.js"),
  categoryList:        Seq[TodoCategory]              = Nil,
  colorSelect:         Seq[(String,String)]           = Color.values.map(color =>(color.code.toString,color.name)),
  link:                Map[String,String]             = Map(
    "home" -> routes.HomeController.index().url
  ),
) extends ViewValueCommon
