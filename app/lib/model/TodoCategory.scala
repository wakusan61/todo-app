package lib.model
import TodoCategory._
import ixias.model.{@@, Entity, EntityModel, Identity, NOW, the}
import ixias.util.EnumStatus

import java.time.LocalDateTime
case class TodoCategory (
  id:       Option[Id],
  name:     String,
  slug:     String,
  color:    Color,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

object TodoCategory {
  val Id = the[Identity[Id]]
  type Id = Long @@ TodoCategory
  type WithNoId = Entity.WithNoId [Id,TodoCategory]
  type EmbeddedId = Entity.EmbeddedId[Id,TodoCategory]

  sealed abstract class Color(val code:Short,val name:String) extends EnumStatus
  object Color extends EnumStatus.Of[Color] {
    case object RED     extends Color(code = 1,name = "red")
    case object BLUE    extends Color(code = 2,name = "blue")
    case object GREEN   extends Color(code = 3,name = "green")
  }

  def apply(name:String,slug:String,color:Color):WithNoId = {
    new TodoCategory(
      id = None,
      name = name,
      slug = slug,
      color = color
    ).toWithNoId
  }
}


