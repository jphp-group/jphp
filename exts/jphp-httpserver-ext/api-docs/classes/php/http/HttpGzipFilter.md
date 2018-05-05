# HttpGzipFilter

- **class** `HttpGzipFilter` (`php\http\HttpGzipFilter`) **extends** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)
- **package** `http`
- **source** `php/http/HttpGzipFilter.php`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpGzipFilter constructor._
- `->`[`includeMethods()`](#method-includemethods)
- `->`[`excludeMethods()`](#method-excludemethods)
- `->`[`includeMimeTypes()`](#method-includemimetypes)
- `->`[`excludeMimeTypes()`](#method-excludemimetypes)
- `->`[`minGzipSize()`](#method-mingzipsize)
- `->`[`compressLevel()`](#method-compresslevel)
- `->`[`__invoke()`](#method-__invoke)
- See also in the parent class [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.md)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
HttpGzipFilter constructor.

---

<a name="method-includemethods"></a>

### includeMethods()
```php
includeMethods(array $methods): void
```

---

<a name="method-excludemethods"></a>

### excludeMethods()
```php
excludeMethods(array $methods): void
```

---

<a name="method-includemimetypes"></a>

### includeMimeTypes()
```php
includeMimeTypes(array $mimeTypes): void
```

---

<a name="method-excludemimetypes"></a>

### excludeMimeTypes()
```php
excludeMimeTypes(array $mimeTypes): void
```

---

<a name="method-mingzipsize"></a>

### minGzipSize()
```php
minGzipSize(int $size): void
```

---

<a name="method-compresslevel"></a>

### compressLevel()
```php
compressLevel(int $level): void
```

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): bool
```