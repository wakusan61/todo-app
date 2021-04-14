package model

case class ViewValueTodoList(
  title:            String,
  cssSrc:           Seq[String],
  jsSrc:            Seq[String],
  todoList:         Seq[ViewValueTodo]
) extends ViewValueCommon
