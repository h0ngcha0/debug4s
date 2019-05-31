package it.softfork.debug4s

import org.scalatest.{Assertion, FlatSpec, Matchers, Succeeded}
import DebugMacro._
import org.scalactic.{Equality, Prettifier}
import org.scalatest.exceptions.TestFailedException

class DebugMacroSpec extends FlatSpec with BetterMatchers {

  "DebugMacro.debug" should "print the variable being debugged" in {
    val a = 100
    assert(removeAnsiColor(debugMessage(a, 200)) ==
      """DebugMacroSpec.scala:12
        |1) a : Int = 100
        |2) 200""".stripMargin
    )

    val b = 200
    assert(removeAnsiColor(debugMessage(a + b)) ==
      """DebugMacroSpec.scala:19
        |1) a + b : Int = 300""".stripMargin
    )

    assert(removeAnsiColor(debugMessage("100")) ==
      """DebugMacroSpec.scala:24
        |1) "100"""".stripMargin
    )

    assert(removeAnsiColor(debugMessage({
      val square = (y: Int) => y*y
      square(100)
    })) ==
      """DebugMacroSpec.scala:29
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

  it should "foo" in {
    val x = 10
    val y = 10
    val barMatrix = List.fill(x)("bar").map(List.fill(y)(_))
    val fooMatrix = List.fill(x)("foo").map(List.fill(y)(_))

    barMatrix shouldEqual fooMatrix
    1 shouldBe 1
  }


}

trait BetterMatchers extends Matchers {
  import org.scalactic.source
  import scala.language.implicitConversions
  import org.scalatest.exceptions

  class AnyShouldWrapperInterceptor[T](
    val leftSideValue: T,
    val pos: source.Position,
    val prettifier: Prettifier
  ) {
    def shouldEqual(right: Any)(implicit equality: Equality[T]): Assertion = {
      if (!equality.areEqual(leftSideValue, right)) {
        val leftSide = removeAnsiColor(pprint.apply(leftSideValue).toString())
        val rightSide = removeAnsiColor(pprint.apply(right).toString())
        val didNotEqual = fansi.Bold.On("\n== did not equal to ==\n")

        val message = s"${leftSide}\n${didNotEqual}\n${rightSide}"
        throw new TestFailedException((sde: exceptions.StackDepthException) => Some(message), None, pos)
      } else {
        Succeeded
      }
    }
  }

  implicit def convertToAnyShouldWrapperInterceptor[T](o: T)(implicit pos: source.Position, prettifier: Prettifier): AnyShouldWrapperInterceptor[T] = {
    new AnyShouldWrapperInterceptor(o, pos, prettifier)
  }

  def removeAnsiColor(message: String): String = {
    message.replaceAll("\u001B\\[[;\\d]*m", "")
  }
}