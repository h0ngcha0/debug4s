package it.softfork.debug

import org.scalatest.{FlatSpec, Matchers}
import DebugMacro._

class DebugMacroSpec extends FlatSpec with Matchers {
  "DebugMacro.debug" should "print the variable being debugged" in {
    val x = 100
    val debugXMessage = debugMessage(x)

    assert(debugXMessage.contains(
      """debug/src/test/scala/it/softfork/debug/DebugMacroSpec.scala:9
        |> x = 100""".stripMargin
    ))

    val y = 200
    val debugXPlusYMessage = debugMessage(x+y)
    assert(debugXPlusYMessage.contains(
      """debug/src/test/scala/it/softfork/debug/DebugMacroSpec.scala:17
        |> x.+(y) = 300""".stripMargin
    ))

    debug(x+y)
  }
}
