package controllers

import lib.persistence.onMySQL.TodoCategoryRepository
import lib.persistence.onMySQL.TodoRepository
import model.{CreateTodoForm, ViewValueCreateTodo, ViewValueTodo, ViewValueTodoList}
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
        todoList  =  todoList
      )))
    }
  }

  def showCreateForm() = Action.async { implicit req =>
    TodoCategoryRepository.list()
      .map(categorySeq => {
        val categorySelect = categorySeq.map(category => (category.v.id.get.toString,category.v.name))
        Ok(views.html.CreateTodo(ViewValueCreateTodo(todoCategorySelect = categorySelect)))
      }).recoverWith {
        case t =>  Future(BadRequest(views.html.CreateTodo(
          ViewValueCreateTodo(
            form = CreateTodoForm.form.withGlobalError(s"Todoカテゴリ取得時にエラーが発生しました。:${t.getMessage}")
          ))))
      }
  }

  def create() = Action.async { implicit req =>
    CreateTodoForm.form.bindFromRequest().fold(
      hasErrors => {
        Future(BadRequest(views.html.CreateTodo(ViewValueCreateTodo(form = hasErrors))))
      },
      todoData => {
        TodoRepository.add(todoData.toWithNoId())
          .map(_ => Redirect(routes.TodoController.list()))
          .recoverWith {
            case e => {
              val form = CreateTodoForm.form.fill(todoData).withGlobalError(s"DB登録時にエラーが発生しました:${e.getMessage}")
              Future(BadRequest(views.html.CreateTodo(ViewValueCreateTodo(form = form))))
            }}
      }
    )
  }
}
