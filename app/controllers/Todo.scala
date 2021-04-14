package controllers

import lib.persistence.onMySQL.TodoCategoryRepository
import lib.persistence.onMySQL.TodoRepository
import model.{ViewValueTodo, ViewValueTodoList}
import play.api.mvc.{BaseController, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TodoController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext)  extends BaseController {
  def list() = Action.async { implicit req =>
    for {
      todoSeq         <- TodoRepository.list()
      todoCategorySeq <- TodoCategoryRepository.list()
    } yield {
      val todoList = todoSeq.map(todo => {
        ViewValueTodo.from(todo,todoCategorySeq.find(_.v.id.get == todo.v.categoryId).get)
      })
      Ok(views.html.TodoList(ViewValueTodoList(
        title = "ToDoリスト",
        cssSrc = Seq("main.css"),
        jsSrc  = Seq("main.js"),
        todoList =  todoList
      )))
    }
  }
}
