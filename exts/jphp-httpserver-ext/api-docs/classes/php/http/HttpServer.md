# HttpServer

- **class** `HttpServer` (`php\http\HttpServer`)
- **package** `http`
- **source** `php/http/HttpServer.php`

**Description**

Class HttpServer

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _Server constructor._
- `->`[`stopAtShutdown()`](#method-stopatshutdown) - _If true, this server instance will be explicitly stopped when the_
- `->`[`minThreads()`](#method-minthreads)
- `->`[`maxThreads()`](#method-maxthreads)
- `->`[`threadIdleTimeout()`](#method-threadidletimeout)
- `->`[`listen()`](#method-listen) - _Add connector to server:_
- `->`[`unlisten()`](#method-unlisten)
- `->`[`connectors()`](#method-connectors) - _All connectors (host+port)_
- `->`[`run()`](#method-run) - _Run server in current thread._
- `->`[`runInBackground()`](#method-runinbackground) - _Run server in background thread._
- `->`[`shutdown()`](#method-shutdown) - _Stop server._
- `->`[`handlers()`](#method-handlers)
- `->`[`filters()`](#method-filters)
- `->`[`addWebSocket()`](#method-addwebsocket)
- `->`[`addFilter()`](#method-addfilter)
- `->`[`addHandler()`](#method-addhandler)
- `->`[`clearHandlers()`](#method-clearhandlers) - _Remove all handlers._
- `->`[`setRequestLogHandler()`](#method-setrequestloghandler)
- `->`[`setErrorHandler()`](#method-seterrorhandler)
- `->`[`isRunning()`](#method-isrunning)
- `->`[`isFailed()`](#method-isfailed)
- `->`[`isStopped()`](#method-isstopped)
- `->`[`isStopping()`](#method-isstopping)
- `->`[`isStarting()`](#method-isstarting)
- `->`[`filtrate()`](#method-filtrate)
- `->`[`route()`](#method-route) - _Route a handler by method and path._
- `->`[`any()`](#method-any) - _Route any methods._
- `->`[`get()`](#method-get) - _Route a handler by GET method + path._
- `->`[`post()`](#method-post) - _Route a handler by POST method + path._
- `->`[`put()`](#method-put) - _Route a handler by PUT method + path._
- `->`[`delete()`](#method-delete) - _Route a handler by DELETE method + path._
- `->`[`options()`](#method-options) - _Route a handler by OPTIONS method + path._
- `->`[`patch()`](#method-patch) - _Route a handler by PATCH method + path._
- `->`[`head()`](#method-head) - _Route a handler by HEAD method + path._

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $port, string $host): void
```
Server constructor.

---

<a name="method-stopatshutdown"></a>

### stopAtShutdown()
```php
stopAtShutdown(bool $value): bool
```
If true, this server instance will be explicitly stopped when the
JVM is shutdown. Otherwise the JVM is stopped with the server running.

---

<a name="method-minthreads"></a>

### minThreads()
```php
minThreads(int $min): int
```

---

<a name="method-maxthreads"></a>

### maxThreads()
```php
maxThreads(int $max): int
```

---

<a name="method-threadidletimeout"></a>

### threadIdleTimeout()
```php
threadIdleTimeout(int $timeout): int
```

---

<a name="method-listen"></a>

### listen()
```php
listen(string $portOrHostPort): void
```
Add connector to server:
127.0.0.1:8080
8080, 127.0.0.1

---

<a name="method-unlisten"></a>

### unlisten()
```php
unlisten(string $portOrHostPort): void
```

---

<a name="method-connectors"></a>

### connectors()
```php
connectors(): string[]
```
All connectors (host+port)

---

<a name="method-run"></a>

### run()
```php
run(): void
```
Run server in current thread.

---

<a name="method-runinbackground"></a>

### runInBackground()
```php
runInBackground(): void
```
Run server in background thread.

---

<a name="method-shutdown"></a>

### shutdown()
```php
shutdown(): void
```
Stop server.

---

<a name="method-handlers"></a>

### handlers()
```php
handlers(): callable[]
```

---

<a name="method-filters"></a>

### filters()
```php
filters(): callable[]
```

---

<a name="method-addwebsocket"></a>

### addWebSocket()
```php
addWebSocket(string $path, array $handlers): void
```

---

<a name="method-addfilter"></a>

### addFilter()
```php
addFilter(callable $callback): void
```

---

<a name="method-addhandler"></a>

### addHandler()
```php
addHandler(callable $callback): void
```

---

<a name="method-clearhandlers"></a>

### clearHandlers()
```php
clearHandlers(): void
```
Remove all handlers.

---

<a name="method-setrequestloghandler"></a>

### setRequestLogHandler()
```php
setRequestLogHandler([ callable|null $handler): void
```

---

<a name="method-seterrorhandler"></a>

### setErrorHandler()
```php
setErrorHandler([ callable|null $handler): void
```

---

<a name="method-isrunning"></a>

### isRunning()
```php
isRunning(): bool
```

---

<a name="method-isfailed"></a>

### isFailed()
```php
isFailed(): bool
```

---

<a name="method-isstopped"></a>

### isStopped()
```php
isStopped(): bool
```

---

<a name="method-isstopping"></a>

### isStopping()
```php
isStopping(): bool
```

---

<a name="method-isstarting"></a>

### isStarting()
```php
isStarting(): bool
```

---

<a name="method-filtrate"></a>

### filtrate()
```php
filtrate(string|array $methods, string $path, callable $filter): php\http\HttpRouteFilter
```

---

<a name="method-route"></a>

### route()
```php
route(string|array $methods, string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by method and path.

---

<a name="method-any"></a>

### any()
```php
any(string $path, callable $handler): php\http\HttpRouteHandler
```
Route any methods.

---

<a name="method-get"></a>

### get()
```php
get(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by GET method + path.

---

<a name="method-post"></a>

### post()
```php
post(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by POST method + path.

---

<a name="method-put"></a>

### put()
```php
put(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by PUT method + path.

---

<a name="method-delete"></a>

### delete()
```php
delete(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by DELETE method + path.

---

<a name="method-options"></a>

### options()
```php
options(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by OPTIONS method + path.

---

<a name="method-patch"></a>

### patch()
```php
patch(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by PATCH method + path.

---

<a name="method-head"></a>

### head()
```php
head(string $path, callable $handler): php\http\HttpRouteHandler
```
Route a handler by HEAD method + path.