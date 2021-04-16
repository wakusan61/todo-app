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
    case object RED      extends Color(code = 1,name = "red")
    case object GREEN    extends Color(code = 2, name = "green")
    case object BLUE     extends Color(code = 3, name = "blue")
    case object FUCHSIA  extends Color(code = 4, name = "fuchsia")
    case object GRAY     extends Color(code = 5, name = "gray")
    case object GREY     extends Color(code = 6, name = "grey")
    case object BLACK    extends Color(code = 7, name = "black")
    case object LIME     extends Color(code = 8, name = "lime")
    case object MAROON   extends Color(code = 9, name = "maroon")
    case object NAVY     extends Color(code = 10,name = "navy")
    case object OLIVE    extends Color(code = 11,name = "olive")
    case object PURPLE   extends Color(code = 12,name = "purple")
    case object AQUA     extends Color(code = 13, name = "aqua")
    case object SILVER   extends Color(code = 14,name = "silver")
    case object TEAL     extends Color(code = 15,name = "teal")
    case object WHITE    extends Color(code = 16,name = "white")
    case object YELLOW   extends Color(code = 17,name = "yellow")  }

  def apply(name:String,slug:String,color:Color):WithNoId = {
    new TodoCategory(
      id = None,
      name = name,
      slug = slug,
      color = color
    ).toWithNoId
  }
}


