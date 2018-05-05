# HttpRedirectHandler

- **class** `HttpRedirectHandler` (`php\http\HttpRedirectHandler`) **extends** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)
- **package** `http`
- **source** `php/http/HttpRedirectHandler.php`

**Description**

Class HttpDownloadFileHandler

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpDownloadFileHandler constructor._
- `->`[`url()`](#method-url)
- `->`[`permanently()`](#method-permanently)
- `->`[`reset()`](#method-reset)
- `->`[`__invoke()`](#method-__invoke)
- See also in the parent class [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)

---
# Methods

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