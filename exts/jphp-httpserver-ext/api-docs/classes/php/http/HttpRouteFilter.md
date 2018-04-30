# HttpRouteFilter

- **class** `HttpRouteFilter` (`php\http\HttpRouteFilter`) **extends** [`HttpAbstractHandler`](api-docs/classes/php/http/HttpAbstractHandler.md)
- **package** `http`
- **source** [`php/http/HttpRouteFilter.php`](./src/main/resources/JPHP-INF/sdk/php/http/HttpRouteFilter.php)


---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpRouteHandler constructor._
- `->`[`handler()`](#method-handler)
- `->`[`methods()`](#method-methods)
- `->`[`path()`](#method-path)
- `->`[`__invoke()`](#method-__invoke)

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

---
