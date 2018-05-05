# HttpRouteFilter

- **class** `HttpRouteFilter` (`php\http\HttpRouteFilter`) **extends** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)
- **package** `http`
- **source** `php/http/HttpRouteFilter.php`

**Child Classes**

> [HttpRouteHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpRouteHandler.md)

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpRouteHandler constructor._
- `->`[`handler()`](#method-handler)
- `->`[`methods()`](#method-methods)
- `->`[`path()`](#method-path)
- `->`[`__invoke()`](#method-__invoke)
- See also in the parent class [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|array $methods, string $path, callable $handler): void
```
HttpRouteHandler constructor.

---

<a name="method-handler"></a>

### handler()
```php
handler(): callable
```

---

<a name="method-methods"></a>

### methods()
```php
methods(): array
```

---

<a name="method-path"></a>

### path()
```php
path(): string
```

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): bool
```