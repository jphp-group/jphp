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
+ Using Java libraries with PHP


### How build and run?

Use maven:

    mvn package

Then you can find the builded jar file in your `target/{version}/` directory. There are
a few files: `jphp.jar`, `jphp.bat` and `jphp`. Add the `target/{version}/` directory to your
PATH env variable and try JPHP, use `jphp` command:

    jphp -f <path/to/file.php>
    jphp -v


### Using with Maven

```
<dependency>
    <groupId>ru.regenix</groupId>
    <artifactId>jphp</artifactId>
    <version>0.2-snapshot</version>
</dependency>
```

And add our maven repository:

```
<repositories>
  <repository>
      <id>JPHP repository</id>
      <url>https://raw.github.com/dim-s/jphp-maven/master/</url>
    </repository>
</repositories>
```
