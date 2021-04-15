package model

import play.api.http.Status

case class ViewValueError (
  title:            String        = "エラーページ",
  cssSrc:           Seq[String]   = Seq("main.css"),
  jsSrc:            Seq[String]   = Seq("main.js"),
  message:          String,
  throwable:        Option[Throwable] = None,
) extends ViewValueCommon
