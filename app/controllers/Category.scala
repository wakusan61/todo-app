package controllers

import ixias.model.tag
import lib.model.TodoCategory
import lib.persistence.onMySQL.TodoCategoryRepository
import model.ViewValueError
import model.category.{CreateCategoryForm, UpdateCategoryForm, ViewValueCategoryList, ViewValueCreateCategory, ViewValueUpdateCategory}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class CategoryController @Inject()(val messagesControllerComponents: MessagesControllerComponents)
  extends MessagesAbstractController(messagesControllerComponents){
  /**
   * カテゴリー一覧を表示します。
   * @return
   */
  def list() = Action.async {implicit req =>
    TodoCategoryRepository.list()
      .map(categorySeq => Ok(views.html.category.CategoryList(ViewValueCategoryList(
        categoryList = categorySeq.map(e => e.v)
      ))))
  }

  /**
   * カテゴリー作成フォームを表示します。
   * @return
   */
  def showCreateForm() = Action.async {implicit req =>
    Future(Ok(views.html.category.CreateCategory(ViewValueCreateCategory())))
  }

  /**
   * カテゴリーを作成します。
   *
   * @return
   */
  def create() = Action.async {implicit req =>
    CreateCategoryForm.form.bindFromRequest().fold(
      hasErrors => {
        Future(BadRequest(views.html.category.CreateCategory(ViewValueCreateCategory(form = hasErrors))))
      },
      categoryData => {
        TodoCategoryRepository.add(categoryData.toWithNoId())
          .map(_ => Redirect(routes.CategoryController.list()))
          .recoverWith {
            case e => {
              Future(InternalServerError(views.html.Error(
                ViewValueError(message = "カテゴリーの作成に失敗しました。", throwable = Some(e))
              )))
            }
          }
      }
    )
  }

  /**
   * カテゴリー更新フォームを表示します。
   * @param id カテゴリーID
   * @return
   */
  def showUpdateForm(id: Long) = Action.async { implicit req =>
    for {
      categoryOpt <- TodoCategoryRepository.get(tag[TodoCategory][Long](id))
    } yield categoryOpt match {
        case None => NotFound(views.html.Error(ViewValueError(message = "カテゴリーが存在しません。")))
        case Some(category) => {
          val form = UpdateCategoryForm.form.fill(UpdateCategoryForm.CategoryData(
            id    = category.v.id.get,
            name  = category.v.name,
            slug  = category.v.slug,
            color = category.v.color.code
          ))
          Ok(views.html.category.UpdateCategory(ViewValueUpdateCategory(form = form)))
        }
    }
  }

  /**
   * カテゴリーを更新します。
   *
   * @return
   */
  def update() = Action.async {implicit req =>
    UpdateCategoryForm.form.bindFromRequest().fold(
      hasErrors => {
        Future(BadRequest(views.html.category.UpdateCategory(ViewValueUpdateCategory(form = hasErrors))))
      },
      categoryData => {
        TodoCategoryRepository.update(categoryData.toEmbeddedId())
          .map {
            case None => InternalServerError(views.html.Error(
              ViewValueError(message = "カテゴリーの更新に失敗しました。")
            ))
            case _ => Redirect(routes.CategoryController.list())
          }
          .recoverWith {
            case e => {
              Future(InternalServerError(views.html.Error(
                ViewValueError(message = "カテゴリーの更新に失敗しました。", throwable = Some(e))
              )))
            }
          }
      }
    )
  }

  /**
   * カテゴリーを削除します。紐づくTODOも全て削除します。
   * @param id カテゴリーID
   * @return
   */
  def removeWithTodo(id: Long) = Action.async { implicit req =>
    TodoCategoryRepository.removeWithTodo(tag[TodoCategory][Long](id))
      .map(_ => Redirect(routes.CategoryController.list()))
      .recoverWith {
        case e => {
          Future(InternalServerError(views.html.Error(
            ViewValueError(message = "カテゴリの削除に失敗しました。", throwable = Some(e)))
          ))
        }
      }
  }
}
