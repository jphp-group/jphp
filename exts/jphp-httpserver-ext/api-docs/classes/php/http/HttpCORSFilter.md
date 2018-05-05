# HttpCORSFilter

- **class** `HttpCORSFilter` (`php\http\HttpCORSFilter`) **extends** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)
- **package** `http`
- **source** `php/http/HttpCORSFilter.php`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpCORSFilter constructor._
- `->`[`allowDomains()`](#method-allowdomains)
- `->`[`allowMethods()`](#method-allowmethods)
- `->`[`allowHeaders()`](#method-allowheaders)
- `->`[`allowCredentials()`](#method-allowcredentials)
- `->`[`maxAge()`](#method-maxage)
- `->`[`handleOptions()`](#method-handleoptions) - _Handle OPTIONS query._
- `->`[`__invoke()`](#method-__invoke)
- See also in the parent class [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(array $allowedDomains, array $allowedMethods, array $allowedHeaders, bool $allowedCredentials, int $maxAge): void
```
HttpCORSFilter constructor.

---

<a name="method-allowdomains"></a>

### allowDomains()
```php
allowDomains(): array
```

---

<a name="method-allowmethods"></a>

### allowMethods()
```php
allowMethods(): array
```

---

<a name="method-allowheaders"></a>

### allowHeaders()
```php
allowHeaders(): array
```

---

<a name="method-allowcredentials"></a>

### allowCredentials()
```php
allowCredentials(): bool
```

---

<a name="method-maxage"></a>

### maxAge()
```php
maxAge(): int
```

---

<a name="method-handleoptions"></a>

### handleOptions()
```php
handleOptions(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): void
```
Handle OPTIONS query.

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): bool
```