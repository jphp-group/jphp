PHP Compiler for JVM
====================

[![Build Status](http://ci.develnext.org/buildStatus/icon?job=jphp)](http://ci.develnext.org/job/jphp/)

JPHP is a new implementation for PHP which uses the Java VM. It supports many features of the PHP language (5.5+).

How does it work? JPHP is a compiler like `javac`, it compiles php sources to JVM bytecode and then
can execute the result on the Java VM.

+ Supports: JDK 1.6+ (OpenJDK, Oracle)
+ Support Forum: https://groups.google.com/d/forum/jphp-compiler

### Goals

JPHP is not a replacement for the Zend PHP engine or Facebook HHVM. We don’t plan to implement the zend runtime libraries (e.g. Curl, PRCE, etc.) for JPHP.

Our project started in October 2013. There were a few reasons for that:

1. Ability to use java libraries in PHP
2. Upgrading performance via JIT and JVM
3. Replacing the ugly runtime library of Zend PHP with a better runtime library.
4. Using the PHP language not only on the web
5. Also: unicode for strings and threads


### Features

+ JIT (1x - 10x faster PHP 5.5)
+ Optimizer (constant expressions, inline functions, etc.)
+ Using java libraries and classes in PHP code.
+ Unicode for strings (UTF-16, like in Java)
+ Threading, Sockets
+ Environment architecture (like sandbox objects in runkit zend extension).
+ GUI (based on Swing, improved - more flexible layouts)
+ Embedded cache system for classes and functions
+ Optional Hot Reloading for classes and functions
+ Ability to use on **Android** OS


### Language Features
> (without zend runtime libraries)

+ PHP 5.5+ (with OOP)
+ Spl autoloading for classes
+ Iterators, ArrayAccess, Serializable, etc.
+ Reflection classes
+ `__debugInfo` for var_dump (php 5.6)
+ `**` power operator (php 5.6)
+ Constant Scalar Expressions (with constants) (php 5.6)
+ Importing namespaced functions (php 5.6)
+ Dedicated syntax for variadic functions (php 5.6)

**What JPHP does not yet support?**

+ Argument unpacking (php 5.6)

### Documentation

- You can find the latest documentation here: http://jphp-docs.readthedocs.org/
- To contribute to the documentation, you can fork the `docs` project: https://github.com/jphp-compiler/docs

---

### JPHP - альтернативный движок для PHP

Это компилятор и движок для языка PHP под Java VM. Он полностью работает на Java, исходный код php компилируется в байткод JVM, который подвергается оптимизациям и JIT. Если вы знакомы с проектами JRuby, Jython и т.д., то JPHP это то же самое, только для PHP. Поддерживаются все фичи PHP 5.5+ и некоторые из PHP 5.6.

Основное отличие от PHP это отказ от использования несогласованных runtime библиотек и расширений. Язык тот же, а библиотеки для него другие, с использованием ООП и т.д.

Больше информации о разработке вы найдете по следующим ссылкам:

- http://habrahabr.ru/post/216651/
- http://develnet.ru/tag/jphp/
- http://community.develstudio.ru/showthread.php/9411-JPHP-Блог-разработки

На данный момент вы можете собрать JAR файл с исходниками php, который будет выполняться с помощью JPHP. Загляните
в папку `jphp-example-project`, этот проект можно собрать в выполняемый jar файл, который можно запустить по двойному клику в Windows, если у вас установлена Java. Для сборки проекта вам понадобится Gradle. Используйте стандартную команду `gradle jar` или если хотите сразу посмотреть результат `jphp-example-project` выполните `gradle exampleStart`.

К тому же проект для примера настраивается через файл `resources/JPHP-INF/launcher.conf`, где можно поменять `bootstrap.file`
на другой файл (например `bootstrap_gui.php`, `bootstrap_server.php` - это различные независимые примеры программ на JPHP).
