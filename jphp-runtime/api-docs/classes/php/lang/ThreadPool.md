# ThreadPool

- **class** `ThreadPool` (`php\lang\ThreadPool`)
- **package** `std`
- **source** `php/lang/ThreadPool.php`

**Description**

Class ThreadPool

---

#### Static Methods

- `ThreadPool ::`[`create()`](#method-create)
- `ThreadPool ::`[`createFixed()`](#method-createfixed)
- `ThreadPool ::`[`createCached()`](#method-createcached)
- `ThreadPool ::`[`createSingle()`](#method-createsingle) - _Creates an Executor that uses a single worker thread operating_
- `ThreadPool ::`[`createScheduled()`](#method-createscheduled) - _Creates a thread pool that can schedule commands to run after a_

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _internal_
- `->`[`isScheduled()`](#method-isscheduled) - _Is Scheduled ?_
- `->`[`isShutdown()`](#method-isshutdown) - _Is Shutdown?_
- `->`[`isTerminated()`](#method-isterminated)
- `->`[`execute()`](#method-execute) - _Execute some $runnable via the Executor Service_
- `->`[`submit()`](#method-submit)
- `->`[`schedule()`](#method-schedule)
- `->`[`shutdown()`](#method-shutdown) - _Initiates an orderly shutdown in which previously submitted_
- `->`[`shutdownNow()`](#method-shutdownnow) - _Attempts to stop all actively executing tasks, halts the_
- `->`[`awaitTermination()`](#method-awaittermination) - _Blocks until all tasks have completed execution after a shutdown_
- `->`[`getActiveCount()`](#method-getactivecount) - _Returns the approximate number of threads that are actively_
- `->`[`getTaskCount()`](#method-gettaskcount) - _Returns the approximate total number of tasks that have ever been_

---
# Static Methods

<a name="method-create"></a>

### create()
```php
ThreadPool::create(int $coreSize, int $maxSize, int $keepAliveTime): ThreadPool
```

---

<a name="method-createfixed"></a>

### createFixed()
```php
ThreadPool::createFixed(int $max): ThreadPool
```

---

<a name="method-createcached"></a>

### createCached()
```php
ThreadPool::createCached(): ThreadPool
```

---

<a name="method-createsingle"></a>

### createSingle()
```php
ThreadPool::createSingle(): ThreadPool
```
Creates an Executor that uses a single worker thread operating
off an unbounded queue.

---

<a name="method-createscheduled"></a>

### createScheduled()
```php
ThreadPool::createScheduled(int $corePoolSize): ThreadPool
```
Creates a thread pool that can schedule commands to run after a
given delay, or to execute periodically.

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
internal

---

<a name="method-isscheduled"></a>

### isScheduled()
```php
isScheduled(): bool
```
Is Scheduled ?

---

<a name="method-isshutdown"></a>

### isShutdown()
```php
isShutdown(): bool
```
Is Shutdown?

---

<a name="method-isterminated"></a>

### isTerminated()
```php
isTerminated(): bool
```

---

<a name="method-execute"></a>

### execute()
```php
execute(callable $runnable, php\lang\Environment $env): void
```
Execute some $runnable via the Executor Service

---

<a name="method-submit"></a>

### submit()
```php
submit(callable $runnable, php\lang\Environment $env): Future
```

---

<a name="method-schedule"></a>

### schedule()
```php
schedule(callable $runnable, int $delay, php\lang\Environment $env): Future
```

---

<a name="method-shutdown"></a>

### shutdown()
```php
shutdown(): void
```
Initiates an orderly shutdown in which previously submitted
tasks are executed, but no new tasks will be accepted.

---

<a name="method-shutdownnow"></a>

### shutdownNow()
```php
shutdownNow(): void
```
Attempts to stop all actively executing tasks, halts the
processing of waiting tasks

---

<a name="method-awaittermination"></a>

### awaitTermination()
```php
awaitTermination(int $timeout): bool
```
Blocks until all tasks have completed execution after a shutdown
request, or the timeout occurs, or the current thread is
interrupted, whichever happens first.

---

<a name="method-getactivecount"></a>

### getActiveCount()
```php
getActiveCount(): int
```
Returns the approximate number of threads that are actively
executing tasks.

---

<a name="method-gettaskcount"></a>

### getTaskCount()
```php
getTaskCount(): int|null
```
Returns the approximate total number of tasks that have ever been
scheduled for execution. Because the states of tasks and
threads may change dynamically during computation, the returned
value is only an approximation.