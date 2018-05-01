# Thread

- **класс** `Thread` (`php\lang\Thread`)
- **пакет** `std`
- **исходники** `php/lang/Thread.php`

**Описание**

Class Thread

---

#### Статичные Методы

- `Thread ::`[`doYield()`](#method-doyield) - _A hint to the scheduler that the current thread is willing to yield_
- `Thread ::`[`sleep()`](#method-sleep) - _Causes the currently executing thread to sleep (temporarily cease_
- `Thread ::`[`getActiveCount()`](#method-getactivecount)
- `Thread ::`[`current()`](#method-current) - _Get current thread_

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`getId()`](#method-getid)
- `->`[`getName()`](#method-getname)
- `->`[`setName()`](#method-setname)
- `->`[`getGroup()`](#method-getgroup)
- `->`[`isDaemon()`](#method-isdaemon)
- `->`[`setDaemon()`](#method-setdaemon)
- `->`[`isInterrupted()`](#method-isinterrupted)
- `->`[`isAlive()`](#method-isalive)
- `->`[`start()`](#method-start) - _start_
- `->`[`run()`](#method-run) - _run_
- `->`[`interrupt()`](#method-interrupt) - _Interrupts this thread._
- `->`[`join()`](#method-join) - _Waits at most $millis milliseconds plus_

---
# Статичные Методы

<a name="method-doyield"></a>

### doYield()
```php
Thread::doYield(): void
```
A hint to the scheduler that the current thread is willing to yield
its current use of a processor. The scheduler is free to ignore this
hint.

---

<a name="method-sleep"></a>

### sleep()
```php
Thread::sleep(int $millis, int $nanos): void
```
Causes the currently executing thread to sleep (temporarily cease
execution)

---

<a name="method-getactivecount"></a>

### getActiveCount()
```php
Thread::getActiveCount(): int
```

---

<a name="method-current"></a>

### current()
```php
Thread::current(): Thread
```
Get current thread

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(callable $runnable, php\lang\Environment $env, php\lang\ThreadGroup $group): void
```

---

<a name="method-getid"></a>

### getId()
```php
getId(): int
```

---

<a name="method-getname"></a>

### getName()
```php
getName(): string
```

---

<a name="method-setname"></a>

### setName()
```php
setName(string $value): void
```

---

<a name="method-getgroup"></a>

### getGroup()
```php
getGroup(): ThreadGroup
```

---

<a name="method-isdaemon"></a>

### isDaemon()
```php
isDaemon(): bool
```

---

<a name="method-setdaemon"></a>

### setDaemon()
```php
setDaemon(bool $value): void
```

---

<a name="method-isinterrupted"></a>

### isInterrupted()
```php
isInterrupted(): bool
```

---

<a name="method-isalive"></a>

### isAlive()
```php
isAlive(): bool
```

---

<a name="method-start"></a>

### start()
```php
start(): void
```
start

---

<a name="method-run"></a>

### run()
```php
run(): void
```
run

---

<a name="method-interrupt"></a>

### interrupt()
```php
interrupt(): void
```
Interrupts this thread.

---

<a name="method-join"></a>

### join()
```php
join(int $millis, int $nanos): void
```
Waits at most $millis milliseconds plus
$nanos nanoseconds for this thread to die.