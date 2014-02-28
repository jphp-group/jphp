PHP Compiler for JVM
====================

[![Build Status](https://travis-ci.org/dim-s/jphp.png?branch=master)](https://travis-ci.org/dim-s/jphp)

JPHP is a new implementation for PHP which uses Java VM. It supports many features of the PHP language (5.3+).
How it works? JPHP is a compiler like `javac`, it compiles php sources to jvm bytecode and than
can execute the result on Java VM.

Supports: JDK 1.6+ (OpenJDK, Oracle)


### Features

+ JIT
+ Speed up performance of PHP (2x - 10x faster)
+ Optimizer (constant expressions, inline functions, etc.)
+ Using java libraries and classes in PHP code.
+ Unicode for strings
+ Threading, Sockets
+ Environment architecture (like sandbox objects in runkit zend extension).

### Using with Maven

```
<dependency>
    <groupId>ru.regenix</groupId>
    <artifactId>jphp</artifactId>
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

### Building JAR with JPHP

Use `php.runtime.launch.Launcher` class as the Main class for your jar. By default, the launcher
loads `JPHP-INF/launcher.con` configuration from a resource directory, inside this file you can
specify the name of a bootstrap php file:

```
bootstrap = bootstrap.php
```

More information here: https://github.com/dim-s/jphp-swing-demo, there you can find a simple example project
with JPHP.

---

### JPHP - альтернативный движок для PHP

Это компилятор и движок для языка PHP под Java VM. Он полностью работает на Java, исходный код php компилируется в байткод JVM, который подвергается оптимизациям и JIT. Если вы знакомы с проектами JRuby, Jython и т.д., то JPHP это тоже самое, только для PHP. Поддерживаются все фичи PHP 5.3+ и некоторые из PHP 5.4 и PHP 5.5.

Основное отличие от PHP это отказ от использования несогласованных runtime библиотек и расширений. Язык тот же, а библиотеки для него другие, с использованием ООП и т.д.


Больше информации о разработке вы найдете по следующим ссылкам:

- http://develnet.ru/tag/jphp/
- http://community.develstudio.ru/showthread.php/9411-JPHP-Блог-разработки

На данный момент вы можете собрать JAR файл с исходниками php, который будет выполняться с помощью JPHP. Пример такого проекта можно найти здесь: https://github.com/dim-s/jphp-swing-demo (gui)
