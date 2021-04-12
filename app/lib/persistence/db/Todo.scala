package lib.persistence.db

import ixias.model.NOW
import ixias.persistence.model.Table
import lib.model.Todo
import slick.jdbc.JdbcProfile

import java.time.LocalDateTime

class TodoTable[P <: JdbcProfile]()(implicit val driver:P)
  extends Table[Todo,P] {
  import api._

  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave" -> DataSourceName("ixias.db.mysql://slave/to_do")
  )

  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query


  class Table(tag: Tag) extends BasicTable(tag,"to_do") {
    import Todo._
    import lib.model.TodoCategory

    // Columns
    /* @1 */ def id          = column[Id]               ("id",          O.UInt64,O.PrimaryKey,O.AutoInc)
    /* @2 */ def categoryId  = column[TodoCategory.Id]  ("category_id", O.UInt64)
    /* @3 */ def title       = column[String]           ("title",       O.Utf8Char255)
    /* @4 */ def body        = column[String]           ("body",        O.Text)
    /* @5 */ def state       = column[Status]           ("state",       O.UInt8)
    /* @6 */ def updatedAt   = column[LocalDateTime]    ("updated_at",  O.TsCurrent)
    /* @7 */ def createdAt   = column[LocalDateTime]    ("created_at",  O.Ts)

    type TableElementTuple = (
      Option[Id], TodoCategory.Id, String, String, Status, LocalDateTime, LocalDateTime
      )

    def * = (id.?,categoryId,title,body,state,updatedAt,createdAt) <> (
      // Tuple(table) => Model
      (t: TableElementTuple) => Todo(
        t._1, t._2, t._3, t._4, t._5, t._6, t._7
      ),
      // Model => Tuple(table)
      (v: TableElementType) => Todo.unapply(v).map{t =>
        (
          t._1, t._2, t._3, t._4, t._5, NOW, t._7
        )}
      )
  }
}
