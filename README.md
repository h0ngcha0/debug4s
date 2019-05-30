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

libraryDependencies += "it.softfork" %% "debug4s" % "0.0.4"

// Needed for macro annotations
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
```

## Features

- Print file and line number of the `debug` statement
- Print the original expression and its type along side the value after it gets evaluated
- Evaluated value is pretty printed using [pprint](https://github.com/lihaoyi/PPrint)
- Support multiple parameters
- Colorized output

In the following example:

```scala
val z = 100
val fooMatrix = List.fill(10)("foo").map(List.fill(10)(_))

debug(fooMatrix, z, "i am here to stay")  // This is line 39 in DebugMacroSpec.scala
```

`debug` macro will print the following in the console:

![Alt text](/images/foomatrix-multi.png?raw=true)