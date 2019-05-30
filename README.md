debug4s
=======
[![Build Status](https://travis-ci.org/liuhongchao/debug4s.svg?branch=master)](https://travis-ci.org/liuhongchao/debug4s)
[![Coverage Status](https://coveralls.io/repos/github/liuhongchao/debug4s/badge.svg?branch=master)](https://coveralls.io/github/liuhongchao/debug4s?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Small Scala utility to make print line debugging easier

## Setup

Add the following to your `build.sbt` file:

```scala
resolvers += Resolver.bintrayRepo("liuhongchao", "maven")

libraryDependencies += "it.softfork" %% "debug4s" % "0.0.3"

// Needed for macro annotations
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
```

## Features

In the following example:

```scala
val sudoku = Seq(
  Seq(7, 6, 0, 8, 3, 4, 2, 1, 9, 5),
  Seq(9, 1, 2, 5, 4, 6, 0, 8, 3, 7),
  Seq(2, 4, 3, 7, 5, 8, 1, 0, 6, 9),
  Seq(8, 0, 9, 1, 6, 2, 3, 5, 7, 4),
  Seq(4, 5, 1, 2, 0, 3, 7, 9, 8, 6),
  Seq(3, 8, 7, 6, 9, 0, 4, 2, 5, 1),
  Seq(6, 7, 4, 9, 2, 1, 5, 3, 0, 8),
  Seq(1, 3, 5, 0, 8, 9, 6, 7, 4, 2),
  Seq(5, 9, 6, 3, 1, 7, 8, 4, 2, 0),
  Seq(0, 2, 8, 4, 7, 5, 9, 6, 1, 3)
)

debug(sudoku)  // This is line 45 in DebugMacroSpec.scala
```

`debug` macro will print the following in the console:

![Alt text](/images/sudoku.png?raw=true)