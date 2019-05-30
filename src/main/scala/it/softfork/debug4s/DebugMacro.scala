package it.softfork.debug4s

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.language.existentials

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

    val fileName = stringExpr(c)(param.tree.pos.source.file.name, Some(fansi.Color.LightBlue))
    val lineNumber = stringExpr(c)(param.tree.pos.line.toString(), Some(fansi.Color.Yellow))
    val paramType = stringExpr(c)(param.tree.tpe.toString(), Some(fansi.Color.Cyan))

    val messageTree = param.tree match {
      case c.universe.Literal(c.universe.Constant(_)) =>
        q"""$fileName + ":" + $lineNumber + "\n> " + pprint.apply($param)"""

      case _ => {
        val paramRepExpr = stringExpr(c)(treeSource(c)(param.tree), Some(fansi.Color.LightGray))
        q"""$fileName + ":" + $lineNumber + "\n> " + $paramRepExpr + " : " + ${paramType} + " = " + pprint.apply($param)"""
      }
    }

    c.Expr[String](messageTree)
  }

  private def treeSource(c: blackbox.Context)(tree: c.universe.Tree): String = {
    // Inspiration from:
    // https://github.com/lihaoyi/sourcecode/blob/23d11b3b6d4b65d4e66aec6473f510862c81117f/sourcecode/shared/src/main/scala-2.x/sourcecode/Macros.scala#L125
    val sourceContent = new String(tree.pos.source.content)
    val start = tree.collect {
      case treeVal => treeVal.pos match {
        case c.universe.NoPosition ⇒ Int.MaxValue
        case p ⇒ {
          if (p.isRange) p.start else p.point
        }
      }
    }.min
    val g = c.asInstanceOf[reflect.macros.runtime.Context].global
    val parser = g.newUnitParser(sourceContent.drop(start))
    parser.expr()
    val end = parser.in.lastOffset
    sourceContent.slice(start, start + end)
  }

  private def stringExpr[String](c: blackbox.Context)(str: fansi.Str, colorMaybe: Option[fansi.Attr] = None): c.Expr[String] = {
    val withColorMaybe = colorMaybe.map(_.apply(str).toString()).getOrElse(str)
    c.Expr[String](c.universe.Literal(c.universe.Constant(withColorMaybe)))
  }
}