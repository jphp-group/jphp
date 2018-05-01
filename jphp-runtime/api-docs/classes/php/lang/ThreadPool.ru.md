# ThreadPool

- **класс** `ThreadPool` (`php\lang\ThreadPool`)
- **пакет** `std`
- **исходники** `php/lang/ThreadPool.php`

**Описание**

Class ThreadPool

---

#### Статичные Методы

- `ThreadPool ::`[`create()`](#method-create)
- `ThreadPool ::`[`createFixed()`](#method-createfixed)
- `ThreadPool ::`[`createCached()`](#method-createcached)
- `ThreadPool ::`[`createSingle()`](#method-createsingle) - _Создает Executor, который будет все обрабатывать в один поток_
- `ThreadPool ::`[`createScheduled()`](#method-createscheduled) - _Создает пулл потоков, который сможет планировать задания к запуску_

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _internal_
- `->`[`isScheduled()`](#method-isscheduled) - _Поставлен в расписание?_
- `->`[`isShutdown()`](#method-isshutdown) - _Завершен?_
- `->`[`isTerminated()`](#method-isterminated)
- `->`[`execute()`](#method-execute) - _Выполнить некоторый $runnable через данный сервис_
- `->`[`submit()`](#method-submit)
- `->`[`schedule()`](#method-schedule)
- `->`[`shutdown()`](#method-shutdown) - _Начинает попорядку завершать пердыдущие засабмиченные завершенные задания,_
- `->`[`shutdownNow()`](#method-shutdownnow) - _Пытается остановить все активные выполняющиеся задания, обрывает_
- `->`[`awaitTermination()`](#method-awaittermination) - _Блокирует до тех пор пока все задания не будут выполнены после запроса shutdown_
- `->`[`getActiveCount()`](#method-getactivecount) - _Returns the approximate number of threads that are actively_
- `->`[`getTaskCount()`](#method-gettaskcount) - _Returns the approximate total number of tasks that have ever been_

---
# Статичные Методы

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
Создает Executor, который будет все обрабатывать в один поток

---

<a name="method-createscheduled"></a>

### createScheduled()
```php
ThreadPool::createScheduled(int $corePoolSize): ThreadPool
```
Создает пулл потоков, который сможет планировать задания к запуску
после определенной задержки или для переодического их запуска.

---
# Методы

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
Поставлен в расписание?

---

<a name="method-isshutdown"></a>

### isShutdown()
```php
isShutdown(): bool
```
Завершен?

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
Выполнить некоторый $runnable через данный сервис

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
Начинает попорядку завершать пердыдущие засабмиченные завершенные задания,
но не новые задания

---

<a name="method-shutdownnow"></a>

### shutdownNow()
```php
shutdownNow(): void
```
Пытается остановить все активные выполняющиеся задания, обрывает
обработку ожидания заданий

---

<a name="method-awaittermination"></a>

### awaitTermination()
```php
awaitTermination(int $timeout): bool
```
Блокирует до тех пор пока все задания не будут выполнены после запроса shutdown
или пока не случится timeout, или текущий поток не будет оборван.

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