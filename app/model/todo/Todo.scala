package model.todo

import lib.model.Todo.Status
import lib.model.TodoCategory.Color
import lib.model.{Todo, TodoCategory}

case class ViewValueTodo(
  id:           Option[Todo.Id],
  categoryId:   TodoCategory.Id,
  categoryName: String,
  slug:         String,
  color:        Color,
  title:        String,
  body:         String,
  state:        Status
)

object ViewValueTodo {
  def from(todo: Todo.EmbeddedId,category: TodoCategory.EmbeddedId): ViewValueTodo ={
    ViewValueTodo(
      id            = todo.v.id,
      categoryId    = todo.v.categoryId,
      categoryName  = category.v.name,
      slug          = category.v.slug,
      color         = category.v.color,
      title         = todo.v.title,
      body          = todo.v.body,
      state         = todo.v.state
    )
  }
}