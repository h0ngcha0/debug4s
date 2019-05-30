package it.softfork.debug4s

import org.scalatest.{FlatSpec, Matchers}
import DebugMacro._

class DebugMacroSpec extends FlatSpec with Matchers {
  "DebugMacro.debug" should "print the variable being debugged" in {
    val x = 100
    val debugXMessage = debugMessage(x)

    assert(removeAnsiColor(debugXMessage) ==
      """DebugMacroSpec.scala:9
        |> x : Int = 100""".stripMargin
    )

    val y = 200
    val debugXPlusYMessage = debugMessage(x + y)
    assert(removeAnsiColor(debugXPlusYMessage) ==
      """DebugMacroSpec.scala:17
        |> x + y : Int = 300""".stripMargin
    )


    val debugConstantMessage = debugMessage("100")
    assert(removeAnsiColor(debugConstantMessage) ==
      """DebugMacroSpec.scala:24
        |> "100"""".stripMargin
    )
  }

  private def removeAnsiColor(message: String): String = {
    println(message)
    message.replaceAll("\u001B\\[[;\\d]*m", "")
  }
}