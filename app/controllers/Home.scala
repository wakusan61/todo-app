/**
 *
 * to do sample project
 *
 */

package controllers

import lib.persistence.{TodoCategoryRepository, TodoRepository}
import model.ViewValueHome

import javax.inject._
import play.api.mvc._
import slick.jdbc.{JdbcProfile, MySQLProfile}

import scala.concurrent.ExecutionContext


@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Home",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.Home(vv))
  }
}
