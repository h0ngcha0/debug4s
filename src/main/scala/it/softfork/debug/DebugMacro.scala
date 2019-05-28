package it.softfork.debug

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object DebugMacro {
  def debug(param: Any): Unit = macro DebugMacroImpl.debugImpl

  def debugMessage(param: Any): String = macro DebugMacroImpl.debugMessageImpl
}

object DebugMacroImpl {

  def debugImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._
    val messageExpr: c.Expr[String] = debugMessageImpl(c)(param)

    c.Expr[Unit](
      q"""println($messageExpr)"""
    )
  }

  def debugMessageImpl(c: blackbox.Context)(param: c.Expr[Any]): c.Expr[String] = {
    import c.universe._

    val tree = param.tree match {
      case c.universe.Literal(c.universe.Constant(_)) =>
        q"""implicitly[sourcecode.File].value + ":" + implicitly[sourcecode.Line].value + "\n> " + pprint.apply($param)"""

      case _ => {
        val paramRepTree = Literal(Constant(show(param.tree)))
        val paramRepExpr = c.Expr[String](paramRepTree)
        q"""implicitly[sourcecode.File].value + ":" + implicitly[sourcecode.Line].value + "\n> " + $paramRepExpr + " = " + pprint.apply($param)"""
      }
    }

    c.Expr[String](tree)
  }
}