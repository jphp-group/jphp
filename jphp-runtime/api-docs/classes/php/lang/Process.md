# Process

- **class** `Process` (`php\lang\Process`)
- **package** `std`
- **source** `php/lang/Process.php`

**Description**

Class Process

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`start()`](#method-start)
- `->`[`startAndWait()`](#method-startandwait) - _Causes the current thread to wait, if necessary, until the_
- `->`[`getExitValue()`](#method-getexitvalue) - _Returns the exit value for the subprocess._
- `->`[`destroy()`](#method-destroy) - _Kills the subprocess. The subprocess represented by this_
- `->`[`getInput()`](#method-getinput) - _Returns the input stream connected to the normal output of the_
- `->`[`getOutput()`](#method-getoutput) - _Returns the output stream connected to the normal input of the_
- `->`[`getError()`](#method-geterror) - _Returns the input stream connected to the error output of the_
- `->`[`inheritIO()`](#method-inheritio)
- `->`[`redirectOutputToFile()`](#method-redirectoutputtofile)
- `->`[`redirectOutputToInherit()`](#method-redirectoutputtoinherit)
- `->`[`redirectOutputToPipe()`](#method-redirectoutputtopipe)
- `->`[`redirectErrorToFile()`](#method-redirecterrortofile)
- `->`[`redirectErrorToInherit()`](#method-redirecterrortoinherit)
- `->`[`redirectErrorToPipe()`](#method-redirecterrortopipe)
- `->`[`redirectInputFromFile()`](#method-redirectinputfromfile)
- `->`[`redirectInputFromInherit()`](#method-redirectinputfrominherit)
- `->`[`redirectInputFromPipe()`](#method-redirectinputfrompipe)
- `->`[`isAlive()`](#method-isalive) - _Tests whether the subprocess represented by this {@code Process} is_
- `->`[`waitFor()`](#method-waitfor) - _Causes the current thread to wait, if necessary, until the_

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(array $commands, null|string|File $directory, array $environment): void
```

---

<a name="method-start"></a>

### start()
```php
start(): Process
```

---

<a name="method-startandwait"></a>

### startAndWait()
```php
startAndWait(): php\lang\Process
```
Causes the current thread to wait, if necessary, until the
process represented by this `Process` object has
terminated.  This method returns immediately if the subprocess
has already terminated.  If the subprocess has not yet
terminated, the calling thread will be blocked until the
subprocess exits.

---

<a name="method-getexitvalue"></a>

### getExitValue()
```php
getExitValue(): int|null
```
Returns the exit value for the subprocess.

---

<a name="method-destroy"></a>

### destroy()
```php
destroy(bool $force): void
```
Kills the subprocess. The subprocess represented by this
`Process` object is forcibly terminated.

---

<a name="method-getinput"></a>

### getInput()
```php
getInput(): Stream
```
Returns the input stream connected to the normal output of the
subprocess.  The stream obtains data piped from the standard
output of the process represented by this `Process` object.

---

<a name="method-getoutput"></a>

### getOutput()
```php
getOutput(): Stream
```
Returns the output stream connected to the normal input of the
subprocess.  Output to the stream is piped into the standard
input of the process represented by this `Process` object.

---

<a name="method-geterror"></a>

### getError()
```php
getError(): Stream
```
Returns the input stream connected to the error output of the
subprocess.  The stream obtains data piped from the error output
of the process represented by this `Process` object.

---

<a name="method-inheritio"></a>

### inheritIO()
```php
inheritIO(): php\lang\Process
```

---

<a name="method-redirectoutputtofile"></a>

### redirectOutputToFile()
```php
redirectOutputToFile(mixed $file): php\lang\Process
```

---

<a name="method-redirectoutputtoinherit"></a>

### redirectOutputToInherit()
```php
redirectOutputToInherit(): php\lang\Process
```

---

<a name="method-redirectoutputtopipe"></a>

### redirectOutputToPipe()
```php
redirectOutputToPipe(): php\lang\Process
```

---

<a name="method-redirecterrortofile"></a>

### redirectErrorToFile()
```php
redirectErrorToFile(mixed $file): php\lang\Process
```

---

<a name="method-redirecterrortoinherit"></a>

### redirectErrorToInherit()
```php
redirectErrorToInherit(): php\lang\Process
```

---

<a name="method-redirecterrortopipe"></a>

### redirectErrorToPipe()
```php
redirectErrorToPipe(): php\lang\Process
```

---

<a name="method-redirectinputfromfile"></a>

### redirectInputFromFile()
```php
redirectInputFromFile(mixed $file): php\lang\Process
```

---

<a name="method-redirectinputfrominherit"></a>

### redirectInputFromInherit()
```php
redirectInputFromInherit(): php\lang\Process
```

---

<a name="method-redirectinputfrompipe"></a>

### redirectInputFromPipe()
```php
redirectInputFromPipe(): php\lang\Process
```

---

<a name="method-isalive"></a>

### isAlive()
```php
isAlive(): bool
```
Tests whether the subprocess represented by this {@code Process} is
alive.

---

<a name="method-waitfor"></a>

### waitFor()
```php
waitFor(): int
```
Causes the current thread to wait, if necessary, until the
process represented by this {@code Process} object has
terminated.