package it.softfork.debug4s

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.language.existentials

object DebugMacro {
  def debug(params: Any*): Unit = macro DebugMacroImpl.debugImpl

  private[debug4s] def debugMessage(params: Any*): String = macro DebugMacroImpl.debugMessageImpl
}

object DebugMacroImpl {

  def debugImpl(c: blackbox.Context)(params: c.Expr[Any]*): c.Expr[Unit] = {
    import c.universe._
    val messageExpr: c.Expr[String] = debugMessageImpl(c)(params:_*)

    c.Expr[Unit](
      q"""println($messageExpr)"""
    )
  }

  def debugMessageImpl(c: blackbox.Context)(params: c.Expr[Any]*): c.Expr[String] = {
    import c.universe._

    val fileName = stringExpr(c)(c.enclosingPosition.source.file.name, Some(fansi.Color.Blue))
    val lineNumber = stringExpr(c)(c.enclosingPosition.line.toString(), Some(fansi.Color.Yellow))

    c.Expr[String](
      q"""$fileName + ":" + $lineNumber + ${paramsMessageTree(c)(params.zipWithIndex)}"""
    )
  }

  private def paramsMessageTree(c: blackbox.Context)(paramsWithIndex: Seq[(c.Expr[Any], Int)]): c.universe.Tree = {
    import c.universe._

    paramsWithIndex match {
      case (firstParam, index) +: restOfParams => {
        val paramType = stringExpr(c)(firstParam.tree.tpe.toString(), Some(fansi.Color.Cyan))
        val paramIndex = stringExpr(c)(s"${index+1}) ", Some(fansi.Color.LightBlue))
        val restOfTree = paramsMessageTree(c)(restOfParams)

        firstParam.tree match {
          case c.universe.Literal(c.universe.Constant(_)) =>
            q""""\n" + ${paramIndex} + pprint.apply($firstParam) + $restOfTree """

          case _ => {
            val paramRepExpr = stringExpr(c)(treeSource(c)(firstParam.tree), Some(fansi.Color.LightGray))
            q""""\n" + ${paramIndex} + $paramRepExpr + " : " + ${paramType} + " = " + pprint.apply($firstParam) + $restOfTree """
          }
        }
      }
      case Nil =>
        q""""""""
    }
  }

  private def treeSource(c: blackbox.Context)(tree: c.universe.Tree): String = {
    // Inspiration from:
    // https://github.com/lihaoyi/sourcecode/blob/23d11b3b6d4b65d4e66aec6473f510862c81117f/sourcecode/shared/src/main/scala-2.x/sourcecode/Macros.scala#L125
    val sourceContent = new String(tree.pos.source.content)
    val start = tree.collect {
      case treeVal => treeVal.pos match {
        case c.universe.NoPosition => Int.MaxValue
        case p => {
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

  private def stringExpr(c: blackbox.Context)(str: fansi.Str, colorMaybe: Option[fansi.Attr]): c.Expr[String] = {
    val withColorMaybe = colorMaybe.map(_.apply(str).toString()).getOrElse(str)
    c.Expr[String](
      c.universe.Literal(c.universe.Constant(withColorMaybe))
    )
  }
}