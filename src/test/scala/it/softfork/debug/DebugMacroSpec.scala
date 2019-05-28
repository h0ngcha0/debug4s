package it.softfork.debug

import org.scalatest.{FlatSpec, Matchers}

class DebugMacroSpec extends FlatSpec with Matchers {
  "DebugMacro.debug" should "debug stuff" in {
    val x = 100
    val y = "200"
    DebugMacro.debug(x)
    1
  }
}
