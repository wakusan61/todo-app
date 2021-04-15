/**
 *
 * to do sample project
 *
 */

package model

import controllers.routes
import play.api.mvc.Call

// Topページのviewvalue
case class ViewValueHome(
  title:            String,
  cssSrc:           Seq[String],
  jsSrc:            Seq[String],
  link:             Map[String,String] = Map(
    "todoList" -> routes.TodoController.list().url,
    "categoryList" -> routes.CategoryController.list().url,
  )
) extends ViewValueCommon

