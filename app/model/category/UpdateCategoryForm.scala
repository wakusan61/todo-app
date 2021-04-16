package model.category

import ixias.model.tag
import lib.model.TodoCategory
import lib.model.TodoCategory.Color
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, nonEmptyText, shortNumber}

object UpdateCategoryForm {
  case class CategoryData(id: Long, name: String, slug: String,color: Short){
    def toEmbeddedId(): TodoCategory#EmbeddedId = new TodoCategory(
      id = Some(tag[TodoCategory][Long](id)),
      name = name,
      slug = slug,
      color = Color(color)
    ).toEmbeddedId
  }

  val form = Form(
    mapping(
      "id"    -> longNumber,
      "name"  -> nonEmptyText,
      "slug"  -> nonEmptyText,
      "color" -> shortNumber
    )(CategoryData.apply)(CategoryData.unapply)
  )
}
