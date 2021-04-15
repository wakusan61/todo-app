package controllers

import ixias.model.tag
import lib.model.Todo
import lib.persistence.onMySQL.TodoCategoryRepository
import lib.persistence.onMySQL.TodoRepository
import model.{CreateTodoForm, UpdateTodoForm, ViewValueCreateTodo, ViewValueError, ViewValueTodo, ViewValueTodoList, ViewValueUpdateTodo}
import play.api.data.Form
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

  /**
   * Todo作成フォームを表示します。
   * @return
   */
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

  /**
   * フォームからTodoを作成します。
   * @return
   */
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
              Future(InternalServerError(views.html.Error(
                ViewValueError(message = "Todoの作成に失敗しました。", throwable = Some(e)))
              ))
            }}
      }
    )
  }

  /**
   * 更新フォームを表示します。
   * @param id TodoId
   * @return
   */
  def showUpdateForm(id: Long) = Action.async {implicit req =>
    for {
      todo <- TodoRepository.get(tag[Todo][Long](id))
      categorySeq <- TodoCategoryRepository.list()
    } yield {
      todo match {
        case None => NotFound(views.html.Error(ViewValueError(message = "Todoが存在しません。")))
        case Some(t) => {
          val categorySelect = categorySeq.map(category => (category.v.id.get.toString,category.v.name))
          val form = UpdateTodoForm.form.fill(UpdateTodoForm.TodoData(
            id          = t.v.id.get,
            title       = t.v.title,
            body        = t.v.body,
            categoryId  = t.v.categoryId,
            state       = t.v.state.code
          ))
          Ok(views.html.UpdateTodo(ViewValueUpdateTodo(todoCategorySelect = categorySelect,form = form)))
        }
      }
    }
  }

  /**
   * フォームからTodoを更新します。
   *
   * @return
   */
  def update() = Action.async {implicit req =>
    UpdateTodoForm.form.bindFromRequest().fold (
      hasErrors => {
        Future(BadRequest(views.html.UpdateTodo(ViewValueUpdateTodo(form = hasErrors))))
      }
      ,todoData => {
        TodoRepository.update(todoData.toEmbeddedId())
          .map(_ => Redirect(routes.TodoController.list()))
          .recoverWith{
            case e => {
              Future(InternalServerError(views.html.Error(
                ViewValueError(message = "Todoの更新に失敗しました。", throwable = Some(e)))
              ))
            }
          }
      }
    )
  }

  /**
   * Todoを削除します。
   * @param id
   * @return
   */
  def remove(id: Long) = Action.async {implicit req =>
    TodoRepository.remove(tag[Todo][Long](id))
      .map(_ => Redirect(routes.TodoController.list()))
      .recoverWith{
        case e => {
          Future(InternalServerError(views.html.Error(
            ViewValueError(message = "Todoの削除に失敗しました。", throwable = Some(e)))
          ))
        }
      }
  }
}
