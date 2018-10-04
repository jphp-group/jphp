JPHP - an implementation of PHP
===============================

[![Build Status](https://travis-ci.org/jphp-group/jphp.svg?branch=master)](https://travis-ci.org/jphp-group/jphp)

JPHP is a new implementation for PHP which uses the Java VM. It supports many features of the PHP language (7.1+).

How does it work? JPHP is a compiler like `javac`, it compiles php sources to JVM bytecode and then
can execute the result on the Java VM.

- Official Site: **[jphp.develnext.org](http://jphp.develnext.org/)**
- [Awesome JPHP](https://github.com/jphp-group/awesome-jphp)

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
+ JIT (~2.5x faster PHP 5.6, ~1.1x faster PHP 7.0, ~1.5x slower PHP 7.1).
+ Using java libraries and classes in PHP code.
+ Unicode for strings (UTF-16, like in Java)
+ [Threading](jphp-runtime/api-docs/classes/php/lang/Thread.md), [Sockets](jphp-runtime/api-docs/classes/php/net/Socket.md), [Environment](jphp-runtime/api-docs/classes/php/lang/Environment.md) architecture (like sandbox objects in the runkit zend extension).
+ GUI ([JavaFX](exts/jphp-gui-ext/api-docs) or [SWT](https://github.com/jphp-group/jphp-swt-ext))
+ Embedded cache system for classes and functions
+ Optional Hot Reloading for classes and functions
+ Ability to use on **Android** OS : [jphp-android](https://github.com/VenityStudio/jphp-android)

**What JPHP supports from PHP 7.0, 7.1?**
+ All features except anonymous classes.

### Own Extensions
- [Standard Library](jphp-runtime/api-docs) - own runtime standard library
- [Http Server](exts/jphp-httpserver-ext) (+Web Sockets +Multithread)
- GUI ([JavaFX](exts/jphp-gui-ext/api-docs) or [SWT](https://github.com/jphp-compiler/jphp-swt-ext))
- [Git](exts/jphp-git-ext/api-docs) (based on JGit)
- [JSoup](exts/jphp-jsoup-ext/api-docs) - for parsing html in jQuery style.
- [SQL](exts/jphp-sql-ext/api-docs) (supports [MySQL](exts/jphp-sql-ext/api-docs), [PostgreSQL](exts/jphp-pgsql-ext/api-docs), [SQLite](exts/jphp-sqlite-ext/api-docs), [Firebird](exts/jphp-firebirdsql-ext/api-docs)).
- [SSH](exts/jphp-ssh-ext/api-docs) - for working with the ssh protocol.
- [Yaml](exts/jphp-yaml-ext/) - for parsing and formating yaml.
- [Compress](exts/jphp-compress-ext) - for working with tar, gz, bz2, lz4, zip archives.
- [HttpClient](exts/jphp-httpclient-ext/api-docs) - http client with promises.
- [SemVer](exts/jphp-semver-ext/api-docs) - for parsing versions in the SemVer standard.
- [Mail](exts/jphp-mail-ext/api-docs) - for sending emails via SMTP servers.
- [MongoDB](exts/jphp-mongo-ext/api-docs) - a driver for Mongo DB 3.0+.

### Documentation

- You can find the latest api documentation here: [jphp-runtime/api-docs](jphp-runtime/api-docs)
- Wiki here: [read](http://jphp.develnext.org/wiki/)

### Getting started (Hello World)

1. Install jphp package manager (jppm), [how to install](packager/#0-how-to-install-jppm).
2. Init new project (jppm package) with default values:
```bash
jppm init
```
3. Run in console `jppm start`.

You will see `Hello World` in your console, the sources of this program will be in `src/index.php`.

### How to run benchmarks?

```
// via jphp
./gradlew bench

// via php
php -f bench/src/bench.php
```

### Build SNAPSHOT from sources

Use `gradle install` to build and install the jphp modules and libraries into the mavel local repository. After this, you can use jphp in your projects as a maven dependency.

`org.develnext.jphp:jphp-<module>:<version>-SNAPSHOT`
