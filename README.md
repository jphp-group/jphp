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

### Using with Maven

```
<dependency>
    <groupId>ru.regenix</groupId>
    <artifactId>jphp</artifactId>
    <version>0.4-SNAPSHOT</version>
</dependency>
```

And add our maven repository:

```
<repository>
    <id>DevelNext Repo</id>
    <url>http://maven.develnext.org/repository/internal/</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
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
