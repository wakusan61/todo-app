package model.category

import lib.model.TodoCategory
import lib.model.TodoCategory.Color
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, shortNumber}

object CreateCategoryForm {
  case class CategoryData(name:String,slug:String,color:Short){
    def toWithNoId():TodoCategory#WithNoId = {
      new TodoCategory(None,name,slug,Color(color)).toWithNoId
    }
  }

  val form = Form(
    mapping (
      "name" -> nonEmptyText,
      "slug" -> nonEmptyText,
      "color" -> shortNumber
    )(CategoryData.apply)(CategoryData.unapply)
  )
}
