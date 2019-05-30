package it.softfork.debug4s

import org.scalatest.{FlatSpec, Matchers}
import DebugMacro._

class DebugMacroSpec extends FlatSpec with Matchers {
  "DebugMacro.debug" should "print the variable being debugged" in {
    val x = 100
    assert(removeAnsiColor(debugMessage(x, 200)) ==
      """DebugMacroSpec.scala:9
        |1) x : Int = 100
        |2) 200""".stripMargin
    )

    val y = 200
    assert(removeAnsiColor(debugMessage(x + y)) ==
      """DebugMacroSpec.scala:16
        |1) x + y : Int = 300""".stripMargin
    )

    assert(removeAnsiColor(debugMessage("100")) ==
      """DebugMacroSpec.scala:21
        |1) "100"""".stripMargin
    )

    assert(removeAnsiColor(debugMessage({
      val square = (y: Int) => y*y
      square(100)
    })) ==
      """DebugMacroSpec.scala:26
        |1) {
        |      val square = (y: Int) => y*y
        |      square(100)
        |    } : Int = 10000""".stripMargin
    )

    val z = 100
    val fooMatrix = List.fill(10)("foo").map(List.fill(10)(_))
    debug(fooMatrix, z, "i am here to stay")
  }

  private def removeAnsiColor(message: String): String = {
    println(message)
    message.replaceAll("\u001B\\[[;\\d]*m", "")
  }
}