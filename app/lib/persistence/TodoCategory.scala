package lib.persistence
import ixias.persistence.SlickRepository
import lib.model.TodoCategory
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

case class TodoCategoryRepository[P <: JdbcProfile]()(implicit val driver: P)
  extends SlickRepository[TodoCategory.Id, TodoCategory, P]
  with db.SlickResourceProvider[P] {
  import api._

  /**
   * カテゴリーを1件取得します。
   * @param id カテゴリーID
   * @return
   */
  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable,"slave") { _
      .filter(_.id === id)
      .result.headOption
    }

  /**
   * カテゴリーを追加します。
   * @param entity TodoCategory#WithNoId
   * @return
   */
  def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(TodoCategoryTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }

  /**
   * カテゴリーを更新します。
   * @param entity TodoCategory#WithNoId
   * @return
   */
  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _ <- old match {
            case None    => DBIO.successful(0)
            case Some(_) => row.update(entity.v)
        }
      } yield old
    }

  /**
   * カテゴリーを1件削除します。
   * @param id カテゴリーID
   * @return
   */
  def remove(id: Id): Future[Option[EntityEmbeddedId]] = {
    RunDBAction(TodoCategoryTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }
  }

  /**
   * カテゴリーを1件削除します。紐づくTODOも全件削除します。
   * @param id カテゴリーID
   * @return
   */
  def removeWithTodo(id: Id): Future[Int] = {
    DBAction(TodoTable) { case (db,slick) =>
      DBAction(TodoCategoryTable) {
        case (_,slick2) =>
        val action =  for {
          _ <- slick.filter(_.categoryId === id).delete
          deletedId <- slick2.filter(_.id === id).delete
        } yield deletedId
        db.run(action.transactionally)
      }
    }
  }

  def list(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoCategoryTable,"slave") {
      _.result
    }
}
