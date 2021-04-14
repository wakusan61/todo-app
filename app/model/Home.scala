/**
 *
 * to do sample project
 *
 */

package model

import ixias.model.Entity
import lib.model.{Todo, TodoCategory}

// Topページのviewvalue
case class ViewValueHome(
  title:            String,
  cssSrc:           Seq[String],
  jsSrc:            Seq[String],
) extends ViewValueCommon

