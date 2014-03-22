PHP Compiler for JVM
====================

[![Build Status](https://travis-ci.org/dim-s/jphp.png?branch=master)](https://travis-ci.org/dim-s/jphp)

JPHP is a new implementation for PHP which uses Java VM. It supports many features of the PHP language (5.3+).
How it works? JPHP is a compiler like `javac`, it compiles php sources to JVM bytecode and than
can execute the result on Java VM.

Supports: JDK 1.6+ (OpenJDK, Oracle)

### Goals

JPHP is not a replacement for Zend PHP engine or Facebook HHVM. We don't plan to implement the zend runtime libraries (e.g. Curl, PRCE, etc.) for JPHP. Our project has been started at October 2013 year. There was a few reasons for that:

1. Using the all java libraries in PHP.
2. Upgrading performance via JIT and JVM
3. Replacing the ugly runtime library of Zend PHP on better runtime library.
4. Using the PHP language not only in WEB
5. Also: unicode for strings and threads


### Features

+ JIT (2x - 10x faster PHP 5.4)
+ Optimizer (constant expressions, inline functions, etc.)
+ Using java libraries and classes in PHP code.
+ Unicode for strings (UTF-16, like in Java)
+ Threading, Sockets
+ Environment architecture (like sandbox objects in runkit zend extension).
+ GUI (based on Swing, improved - more flexible layouts)
+ Embedded cache system for classes and functions
+ Optional Hot Reloading for classes and functions


### Language Features 
> (without zend runtime libraries)

+ PHP 5.2+ fully support (with OOP)
+ Closures (PHP 5.3), auto-binding `$this` in closures (PHP 5.4)
+ Namespaces fully support (PHP 5.3)
+ Spl autoloading for classes (PHP 5.3)
+ Iterators, ArrayAccess, Serializable
+ Type hinting for classes, arrays, callable (PHP 5.4)
+ Array short syntax (PHP 5.4)
+ Ternary short cut "?:", NOWDOC, `__callStatic`, `__invoke` (PHP 5.3)
+ Late static binding (PHP 5.3)
+ GC for cyclic references (PHP 5.3)
+ `Class::{expr}()`, `(new Foo)->bar()` (PHP 5.4)
+ Try finally (PHP 5.5)
+ Array and string literal dereferencing (PHP 5.5)
+ `__debugInfo` for var_dump (PHP 5.6)
+ Function array dereferencing has been added, e.g. `foo()[0]` (PHP 5.4) 
+ Constants can be declared outside a class using the const keyword. (PHP 5.3)
+ Dynamic access to static methods (PHP 5.3)
+ Exceptions can be nested (PHP 5.3)
+ `::class` system constant (php 5.5)

**What does not yet support?**

+ Traits (php 5.4)
+ Generators (php 5.5)
+ Goto (php 5.3)
+ list() in foreach (php 5.5)
+ Binary number format, e.g. 0b001001101 (php 5.4)

**What non-php features does JPHP support?**

+ In `__toString` method you can use exeptions
+ Type hinting for scalars - int, double, number, string, bool, mixed, scalar (in JPHP mode)


### Using with Maven

```
<dependency>
    <groupId>ru.regenix</groupId>
    <artifactId>jphp-core</artifactId>
    <version>0.4{-SNAPSHOT}</version>
</dependency>
```

And add our maven repository:

```
<repository>
    <id>DevelNext Repo</id>
    <url>http://maven.develnext.org/repository/{internal or snapshots}/</url>
</repository>
```

> **(!)** At present we recomend you to use snapshot versions because the stable version is updated rarely.

### Building JAR with JPHP

Use `php.runtime.launch.Launcher` class as the Main class for your jar. By default, the launcher
loads `JPHP-INF/launcher.conf` configuration from a resource directory, inside this file you can
specify the name of a bootstrap php file (which will be loaded from a resource directory and executed):

```
bootstrap.file = bootstrap.php
```

We have created an example project which is located in the `jphp-example-project` directory. To try it, you should
use Gradle build system and several tasks: `exampleStart` or `jar` to build the project to a jar file which you can
execute by using Java VM. By default, all jar files will be created in `build/libs/`.

---

### GUI for JPHP

You can use our extension `jphp-swing` to write GUI programs for Linux/Windows/MacOS. There is an example of GUI usage in `jphp-example-project`. To try it you should change the value of the `bootstrap.file` option in `JPHP-INF/launcher.conf`:

    bootstrap.file = bootstrap_gui.php

It means that at first the Launcher will load the `bootstrap_gui.php` file from a resource path. In our case, it will be the `jphp-example-project/src/main/php/bootstrap_gui.php`, source of this file is simple and clear:

```
<?php
namespace {

    use php\lang\System;
    use php\lang\Thread;
    use php\swing\SwingUtilities;
    use php\swing\UIForm;
    use php\swing\UIManager;
    use php\swing\UIProgress;

    UIManager::setLookAndFeel(UIManager::getSystemLookAndFeel());

    SwingUtilities::invokeLater(function(){
        $form = new UIForm();
        $form->size = [500, 500];
        $form->moveToCenter();
        $form->visible = true;

        $p = new UIProgress();
        $p->size = [300, 40];
        $p->position = [100, 100];
        $p->value = 50;
        $form->add($p);

        $form->on('windowClosing', function(){
            System::halt(0);
        });
    });
}
```


---

### JPHP - альтернативный движок для PHP

Это компилятор и движок для языка PHP под Java VM. Он полностью работает на Java, исходный код php компилируется в байткод JVM, который подвергается оптимизациям и JIT. Если вы знакомы с проектами JRuby, Jython и т.д., то JPHP это тоже самое, только для PHP. Поддерживаются все фичи PHP 5.3+ и некоторые из PHP 5.4 и PHP 5.5.

Основное отличие от PHP это отказ от использования несогласованных runtime библиотек и расширений. Язык тот же, а библиотеки для него другие, с использованием ООП и т.д.


Больше информации о разработке вы найдете по следующим ссылкам:

- http://develnet.ru/tag/jphp/
- http://community.develstudio.ru/showthread.php/9411-JPHP-Блог-разработки

На данный момент вы можете собрать JAR файл с исходниками php, который будет выполняться с помощью JPHP. Загляните
в папку `jphp-example-project`, этот проект можно собрать в выполняемый jar файл, который можно запустить по двойному клику в Windows
если у вас установлена Java. Для этого вам нужен Gradle, чтобы собрать программу используйте стандартную команду `gradle jar`
или если хотите сразу посмотреть результат `jphp-example-project` выполните `gradle exampleStart`.

К тому же проект для примера настраивается через файл `resources/JPHP-INF/launcher.conf`, где можно поменять `bootstrap.file`
на другой файл (например `bootstrap_gui.php`, `bootstrap_server.php` - это различные независимые примеры программ на JPHP).
