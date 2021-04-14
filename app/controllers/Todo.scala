package controllers

import lib.persistence.onMySQL.TodoCategoryRepository
import lib.persistence.onMySQL.TodoRepository
import model.{TodoForm, ViewValueCreateTodo, ViewValueTodo, ViewValueTodoList}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class TodoController @Inject()(val messagesControllerComponents: MessagesControllerComponents)
  extends MessagesAbstractController(messagesControllerComponents)
{
  /**
   * Todo一覧画面を表示します。
   * @return
   */
  def list() = Action.async { implicit req =>
    for {
      todoSeq         <- TodoRepository.list()
      todoCategorySeq <- TodoCategoryRepository.list()
    } yield {
      val todoList = todoSeq.map(todo => {
        ViewValueTodo.from(todo,todoCategorySeq.find(_.v.id.get == todo.v.categoryId).get)
      })
      Ok(views.html.TodoList(ViewValueTodoList(
        title     = "ToDoリスト",
        cssSrc    = Seq("main.css"),
        jsSrc     = Seq("main.js"),
        todoList  =  todoList
      )))
    }
  }

  def createForm() = Action.async { implicit req =>
    Future(Ok(views.html.CreateTodo(ViewValueCreateTodo())))
  }

  def create() = Action.async { implicit req =>
    TodoForm.form.bindFromRequest().fold(
      hasErrors => {
        Future(BadRequest(views.html.CreateTodo(ViewValueCreateTodo(form = hasErrors))))
      },
      todoData => {
        TodoRepository.add(todoData.toWithNoId())
          .map(_ => Redirect(routes.TodoController.list()))
          .recoverWith {
            case e => {
              val form = TodoForm.form.fill(todoData).withGlobalError(e.getMessage,e.getStackTrace)
              Future(BadRequest(views.html.CreateTodo(ViewValueCreateTodo(form = form))))
            }}
      }
    )
  }
}
