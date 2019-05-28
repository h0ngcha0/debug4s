package it.softfork.debug

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DebugMacro {
  def debug(param: Any): Unit = macro DebugMacroImpl.debugImpl
}

object DebugMacroImpl {
  def debugImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    val tree = param.tree match {
      case c.universe.Literal(c.universe.Constant(_)) =>
        q"""println(implicitly[sourcecode.File].value + ":" + implicitly[sourcecode.Line].value + "\n> " + $param)"""

      case _ => {
        val paramRepTree = Literal(Constant(show(param.tree)))
        val paramRepExpr = c.Expr[String](paramRepTree)
        q"""println(implicitly[sourcecode.File].value + ":" + implicitly[sourcecode.Line].value + "\n> " + $paramRepExpr + " = " + $param)"""
      }
    }

    c.Expr[Unit](tree)
  }
}