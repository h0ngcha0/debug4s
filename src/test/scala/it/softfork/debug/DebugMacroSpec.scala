package it.softfork.debug

import org.scalatest.{FlatSpec, Matchers}
import DebugMacro._

class DebugMacroSpec extends FlatSpec with Matchers {
  "DebugMacro.debug" should "print the variable being debugged" in {
    val x = 100
    val debugXMessage = debugMessage(x)

    assert(removeAnsiColor(debugXMessage).contains(
      """debug/src/test/scala/it/softfork/debug/DebugMacroSpec.scala:9
        |> x = 100""".stripMargin
    ))

    val y = 200
    val debugXPlusYMessage = debugMessage(x+y)
    assert(removeAnsiColor(debugXPlusYMessage).contains(
      """debug/src/test/scala/it/softfork/debug/DebugMacroSpec.scala:17
        |> x.+(y) = 300""".stripMargin
    ))

    val foo = Foo(10, Bar(100))
    debug(foo)
  }

  private def removeAnsiColor(message: String): String = {
    message.replaceAll("\u001B\\[[;\\d]*m", "")
  }
}

case class Foo(
  x: Int,
  y: Bar
)

case class Bar(
  z: Int
)
