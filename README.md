PHP Compiler for JVM
====================

[![Build Status](https://travis-ci.org/dim-s/jvm-php.png?branch=master)](https://travis-ci.org/dim-s/jvm-php)


### How build and run?

Use build.xml for ANT project and install ivy for ANT. 
Resolve all dependencies and run ru.regenix.jphp.Main class.


### Are you seriously?

Yes, we know about another implementation of PHP for JVM: http://quercus.caucho.com/,
but our project is experemental and maybe something will come from this. However, quercus 
is opensource under GPL license and compercial (in GPL version it not uses JIT). Our JVM compiler
under Apache License 2.0 and absolutely free for use.

I already implemented compilers and parsers for PHP, you can see these here: 
http://code.google.com/p/orionphp/. This was a few years ago and now I decided to
start a new project of implementation for the PHP language. 

### JIT for PHP

Our implementation uses JIT of JVM totally, it is not a translator to Java code. 
