package controllers

import lib.persistence.onMySQL.TodoCategoryRepository
import model.category.{CreateCategoryForm, ViewValueCategoryList, ViewValueCreateCategory}
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class CategoryController @Inject()(val messagesControllerComponents: MessagesControllerComponents)
  extends MessagesAbstractController(messagesControllerComponents){
  def list() = Action.async {implicit req =>
    TodoCategoryRepository.list()
      .map(categorySeq => Ok(views.html.category.CategoryList(ViewValueCategoryList(
        categoryList = categorySeq.map(e => e.v)
      ))))
  }

  def showCreateForm() = Action.async {implicit req =>
    Future(Ok(views.html.category.CreateCategory(ViewValueCreateCategory())))
  }

  def create() = Action.async {
    Future(Ok(""))
//    CreateCategoryForm.form.bindFromRequest().fold()
  }
}
