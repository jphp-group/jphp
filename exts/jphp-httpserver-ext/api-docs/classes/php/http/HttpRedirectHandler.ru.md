# HttpRedirectHandler

- **класс** `HttpRedirectHandler` (`php\http\HttpRedirectHandler`) **унаследован от** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.ru.md)
- **пакет** `http`
- **исходники** `php/http/HttpRedirectHandler.php`

**Описание**

Class HttpDownloadFileHandler

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _HttpDownloadFileHandler constructor._
- `->`[`url()`](#method-url)
- `->`[`permanently()`](#method-permanently)
- `->`[`reset()`](#method-reset)
- `->`[`__invoke()`](#method-__invoke)
- См. также в родительском классе [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $url, bool $permanently): void
```
HttpDownloadFileHandler constructor.

---

<a name="method-url"></a>

### url()
```php
url(): string
```

---

<a name="method-permanently"></a>

### permanently()
```php
permanently(): bool
```

---

<a name="method-reset"></a>

### reset()
```php
reset(string $url, bool $permanently): void
```

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): bool
```