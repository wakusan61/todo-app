package lib.model

import ixias.model.{@@, Entity, EntityModel, Identity, NOW, the}
import ixias.util.EnumStatus
import lib.model.Todo.{Id, Status}

import java.time.LocalDateTime

case class Todo (
  id:           Option[Id],
  categoryId:   TodoCategory.Id,
  title:        String,
  body:         String,
  state:        Status,
  updatedAt:    LocalDateTime = NOW,
  createdAt:    LocalDateTime = NOW
) extends EntityModel[Id]

object Todo {

  val Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId     [Id,Todo]
  type EmbeddedId = Entity.EmbeddedId [Id,Todo]

  sealed abstract class Status(val code:Short, val name:String) extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object TODO   extends Status(code = 0,name = "TODO(着手前)")
    case object DOING  extends Status(code = 1,name = "進行中")
    case object DONE   extends Status(code = 2,name = "完了")
  }

  def apply(categoryId: TodoCategory.Id,
            title:      String,
            body:       String,
            state:      Status): WithNoId = {
    new Todo(None, categoryId, title, body, state).toWithNoId
  }
}