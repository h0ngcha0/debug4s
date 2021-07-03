package it.softfork.debug4s

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import DebugMacro._

class DebugMacroSpec extends AnyFlatSpecLike with Matchers {
  "DebugMacro.debug" should "print the variable being debugged" in {
    val a = 100
    assert(removeAnsiColor(debugMessage(a, 200)) ==
      """DebugMacroSpec.scala:10
        |1) a : Int = 100
        |2) 200""".stripMargin
    )

    val b = 200
    assert(removeAnsiColor(debugMessage(a + b)) ==
      """DebugMacroSpec.scala:17
        |1) a + b : Int = 300""".stripMargin
    )

    assert(removeAnsiColor(debugMessage("100")) ==
      """DebugMacroSpec.scala:22
        |1) "100"""".stripMargin
    )

    assert(removeAnsiColor(debugMessage({
      val square = (y: Int) => y*y
      square(100)
    })) ==
      """DebugMacroSpec.scala:27
        |1) {
        |      val square = (y: Int) => y*y
        |      square(100)
        |    } : Int = 10000""".stripMargin
    )

    val x = 10
    val y = 10
    val fooMatrix = List.fill(x)("foo").map(List.fill(y)(_))
    debug(fooMatrix, x * y, "i am here to stay")
  }

  private def removeAnsiColor(message: String): String = {
    println(message)
    message.replaceAll("\u001B\\[[;\\d]*m", "")
  }
}