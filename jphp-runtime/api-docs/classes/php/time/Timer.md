# Timer

- **class** `Timer` (`php\time\Timer`)
- **package** `std`
- **source** [`php/time/Timer.php`](./src/main/resources/JPHP-INF/sdk/php/time/Timer.php)

**Description**

Class Timer

---

#### Static Methods

- `Timer ::`[`after()`](#method-after) - _Run once after the period in format:_
- `Timer ::`[`every()`](#method-every) - _Run every time based on the period as in after() method._
- `Timer ::`[`trigger()`](#method-trigger) - _Create trigger timer which once call when trigger callback will return true._
- `Timer ::`[`setTimeout()`](#method-settimeout) - _Like in JS._
- `Timer ::`[`setInterval()`](#method-setinterval) - _Like in JS._
- `Timer ::`[`sleep()`](#method-sleep) - _Sleep period time._
- `Timer ::`[`cancelAll()`](#method-cancelall) - _Call all timer tasks which scheduled._
- `Timer ::`[`shutdownAll()`](#method-shutdownall) - _Shutdown all timers and timer system!_
- `Timer ::`[`parsePeriod()`](#method-parseperiod) - _Converts a string period to amount of millis._

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Timer constructor._
- `->`[`cancel()`](#method-cancel) - _Cancel timer task._
- `->`[`run()`](#method-run) - _Run timer task._
- `->`[`scheduledTime()`](#method-scheduledtime) - _Returns the scheduled execution time of the most recent_

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
Timer constructor.

---

<a name="method-cancel"></a>

### cancel()
```php
cancel(): void
```
Cancel timer task.

---

<a name="method-run"></a>

### run()
```php
run(): void
```
Run timer task.

---

<a name="method-scheduledtime"></a>

### scheduledTime()
```php
scheduledTime(): void
```
Returns the scheduled execution time of the most recent
actual execution of this task.  (If this method is invoked
while task execution is in progress, the return value is the scheduled
execution time of the ongoing task execution.)

---
