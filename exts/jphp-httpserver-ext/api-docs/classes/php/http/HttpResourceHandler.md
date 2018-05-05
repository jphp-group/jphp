# HttpResourceHandler

- **class** `HttpResourceHandler` (`php\http\HttpResourceHandler`) **extends** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)
- **package** `http`
- **source** `php/http/HttpResourceHandler.php`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpFileDirectoryHandler constructor._
- `->`[`file()`](#method-file)
- `->`[`etags()`](#method-etags)
- `->`[`acceptRanges()`](#method-acceptranges)
- `->`[`dirAllowed()`](#method-dirallowed)
- `->`[`directoriesListed()`](#method-directorieslisted)
- `->`[`pathInfoOnly()`](#method-pathinfoonly)
- `->`[`redirectWelcome()`](#method-redirectwelcome)
- `->`[`cacheControl()`](#method-cachecontrol)
- `->`[`__invoke()`](#method-__invoke)
- See also in the parent class [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|File $file): void
```
HttpFileDirectoryHandler constructor.

---

<a name="method-file"></a>

### file()
```php
file(string|File $file): string
```

---

<a name="method-etags"></a>

### etags()
```php
etags(bool $value): bool
```

---

<a name="method-acceptranges"></a>

### acceptRanges()
```php
acceptRanges(bool $value): bool
```

---

<a name="method-dirallowed"></a>

### dirAllowed()
```php
dirAllowed(bool $value): bool
```

---

<a name="method-directorieslisted"></a>

### directoriesListed()
```php
directoriesListed(bool $value): bool
```

---

<a name="method-pathinfoonly"></a>

### pathInfoOnly()
```php
pathInfoOnly(bool $value): bool
```

---

<a name="method-redirectwelcome"></a>

### redirectWelcome()
```php
redirectWelcome(bool $value): bool
```

---

<a name="method-cachecontrol"></a>

### cacheControl()
```php
cacheControl(string $value): string
```

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): void
```