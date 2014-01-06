## PHP Language

> ! Bold text - implemented

### Basic syntax
+ **PHP tags**
+ **Comments**

---

### Types
+ **Booleans**
+ **Integers**
+ **Floating point numbers**
+ **Strings** (UTF-16 Unicode)
+ **Arrays**
+ **Objects**
+ **Resources** ? (like objects, but with a few differents)
+ **NULL**
+ **Callbacks**
+ **Type Juggling**

---

### Variables
+ **Local variables - `$var`**
+ **Static variables** `static $var`
+ **Global scope for variables**
+ **Global statement** - `global $var`
+ **Super Global variables** - `$GLOBALS, $_POST, $_ENV, etc.`

---

### Operators

+ **Arithmetic** `+ - * / %`
+ **Logic** `&& || ! and or ?:`
+ **Bit** `& | ^ ~ << >>`
+ **Assign** `= += -= *= /= %= .= &= |= ^= ++ --`, `++ --` - to do fix!!!
+ **Concat** `.`
+ **Compare** `== != > < >= <=`, undone: `=== !==`
+ for Arrays `+ == === != <> !==`  **(partly, only compare)**
+ **instanceof**
+ **Typed** `(int) (float) (double) (string) (object) (array)`

---

### Control Flow

+ **if else** `if, else, ifelse`
+ **switch** `switch, case, default`.
+ **while** `while(..) { ... }`
+ **do** `do { ... } while(...)`
+ **for** `for(; ;) { }`
+ **break, continue**
+ **return**
+ **require, import** - to do improve
+ **foreach**
+ goto
+ declare
+ **try catch**
+ try finally
+ **HEREDOC, NOWDOC for strings**

---

### Functions

+ **User-defined named functions**
+ **References arguments** - to do improve for ref-to-ref values
+ **Default arguments values** - to do improve: add default for unknown constants
+ **Return references**
+ **Internal built-in functions**
+ **Anonymous functions**

---

### Classes and Objects

+ **Properties**
+ **Constants**
+ Autoloading
+ **Constructors** & **Destructors** (finalize)
+ **Visibility**
+ **Object inheritance**
+ **Scope Resolution Operator (::)**
+ **Static keyword**
+ **Class abstraction**
+ **Interfaces**
+ Traits
+ **Overloading** `__set`, `__get`, `__callStatic`, etc.
+ **Object Iteration**
+ Magic Methods **(partly, __set, __get, __call, __callStatic, __destruct, __construct, __isset, __unset)**
+ **Final Keyword**
+ Object Cloning
+ Comparing Objects
+ Type Hinting **(partly)**
+ **Late Static Bindings**
+ **Objects and references**
+ Object Serialization

---

### Namespaces

+ **Defining namespaces**
+ **Declaring sub-namespaces**
+ **Defining multiple namespaces in the same file**
+ **Using namespaces: Basics**
+ **Multiple values for use statements**
+ **namespace keyword and __NAMESPACE__ constant**
+ **Using namespaces: Aliasing/Importing**
+ **Global space**
+ **Using namespaces: fallback to global function/constant**

---

### Exceptions
+ **Extending Exceptions**

---

### Generators
+ Basic, Generator syntax
+ Comparing generators with Iterator objects

---

### References Explained
+ **Passing by Reference**
+ **Returning References**
+ **Unsetting References**
+ **Spotting References**

---

### Predefined Exceptions
+ **Exception**
+ ErrorException

---

### Predefined Interfaces and Classes
+ **Traversable** — The Traversable interface
+ **Iterator** — The Iterator interface (_todo improve and check_)
+ **IteratorAggregate** — The IteratorAggregate interface
+ ArrayAccess — The ArrayAccess interface
+ Serializable — The Serializable interface
+ **Closure** — The Closure class
+ Generator — The Generator class

---

### Supported Protocols and Wrappers
+ file:// — Accessing local filesystem
+ http:// — Accessing HTTP(s) URLs
+ ftp:// — Accessing FTP(s) URLs
+ php:// — Accessing various I/O streams
+ zlib:// — Compression Streams
+ data:// — Data (RFC 2397)
+ glob:// — Find pathnames matching pattern
+ phar:// — PHP Archive
+ ssh2:// — Secure Shell 2
+ rar:// — RAR
+ ogg:// — Audio streams
+ expect:// — Process Interaction Streams
