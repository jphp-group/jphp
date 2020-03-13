# JPHP Benchmark

A set of synthetic performance tests of various parts of the PHP language.

JPHP (1.2.8) Bench Results:

```
 -> fibonacci: 27.0 ms
 -> loop: 187.0 ms
 -> condition: 27.0 ms
 -> math: 122.0 ms
 -> fetch constants: 164.0 ms
 -> constant call: 164.0 ms
 -> simple func call: 1182.0 ms
 -> simple method call: 958.0 ms
 -> type hinting: 130.0 ms
 -> new object: 340.0 ms
 -> object property: 609.0 ms
 -> array: 525.0 ms
 -> string: 268.0 ms
 -> closure: 143.0 ms
 -> undefined: 19.0 ms
 -> singleton pattern: 82.0 ms
 -> array access object: 156.0 ms
 -> getter + setter: 308.0 ms
 -> iterator: 353.0 ms
 -> service container: 137.0 ms
 -> n-body: 367.0 ms

-------- total: 6.27 s.
```

PHP 7.4.3

```
 -> fibonacci: 14 ms
 -> loop: 933 ms
 -> condition: 88 ms
 -> math: 300 ms
 -> fetch constants: 122 ms
 -> constant call: 158 ms
 -> simple func call: 514 ms
 -> simple method call: 420 ms
 -> type hinting: 118 ms
 -> new object: 289 ms
 -> object property: 213 ms
 -> array: 578 ms
 -> string: 405 ms
 -> closure: 198 ms
 -> undefined: 121 ms
 -> singleton pattern: 64 ms
 -> array access object: 146 ms
 -> getter + setter: 214 ms
 -> iterator: 126 ms
 -> service container: 224 ms
 -> n-body: 248 ms

-------- total: 5.49 s.
```
