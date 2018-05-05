JPHP - an implementation of PHP
===============================

[![Build Status](https://travis-ci.org/jphp-compiler/jphp.svg?branch=master)](https://travis-ci.org/jphp-compiler/jphp)

JPHP is a new implementation for PHP which uses the Java VM. It supports many features of the PHP language (7.1+).

How does it work? JPHP is a compiler like `javac`, it compiles php sources to JVM bytecode and then
can execute the result on the Java VM.

- Official Site: **[jphp.develnext.org](http://jphp.develnext.org/)**

### In Production

We develop a new IDE for beginners like `Game Maker` or `Scirra Construct`. It's based on JPHP, JavaFX, Java 8, Gradle and allows to create desktop games and apps for Linux, Windows and Mac (maybe Android and other platforms in future). The project name is `DevelNext` (https://github.com/jphp-compiler/develnext), the current status and version of the project is BETA. The project has not yet been localized in English. 

### Goals

JPHP is not a replacement for the Zend PHP engine or Facebook HHVM. We don’t plan to implement the zend runtime libraries (e.g. Curl, PRCE, etc.) for JPHP.

Our project started in October 2013. There were a few reasons for that:

1. Ability to use java libraries in PHP
2. Upgrading performance via JIT and JVM
3. Replacing the ugly runtime library of Zend PHP with a better runtime library.
4. Using the PHP language not only on the web
5. Also: unicode for strings and threads

### Features

+ PHP 5.6+ (and many language features from PHP 7.0 and 7.1).
+ JIT (~2.5 faster PHP 5.6, ~1.1 faster PHP 7.0).
+ Using java libraries and classes in PHP code.
+ Unicode for strings (UTF-16, like in Java)
+ Threading, Sockets, Environment architecture (like sandbox objects in the runkit zend extension).
+ GUI ([JavaFX](exts/jphp-gui-ext/api-docs) or [SWT](https://github.com/jphp-compiler/jphp-swt-ext))
+ Embedded cache system for classes and functions
+ Optional Hot Reloading for classes and functions
+ ~~Ability to use on **Android** OS~~ (not yet)

**What JPHP supports from PHP 7.0, 7.1?**
+ All features except anonymous classes.

### Documentation

- Wiki here: [read](http://jphp.develnext.org/wiki/)
- You can find the latest api documentation here: [jphp-runtime/api-docs](jphp-runtime/api-docs)

### Getting started (Hello World)

[http://jphp.develnext.org/wiki/Getting-started](http://jphp.develnext.org/wiki/Getting-started)

### Hot to run benchmarks?

```
// via jphp
./gradlew bench

// via php
php -f bench/src/bench.php
```

### Build SNAPSHOT from sources

Use `gradle install` to build and install the jphp modules and libraries into the mavel local repository. After this, you can use jphp in your projects as a maven dependency.

`org.develnext.jphp:jphp-<module>:<version>-SNAPSHOT`
