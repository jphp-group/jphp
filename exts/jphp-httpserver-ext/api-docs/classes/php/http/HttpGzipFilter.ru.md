# HttpGzipFilter

- **класс** `HttpGzipFilter` (`php\http\HttpGzipFilter`) **унаследован от** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.ru.md)
- **пакет** `http`
- **исходники** `php/http/HttpGzipFilter.php`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _HttpGzipFilter constructor._
- `->`[`includeMethods()`](#method-includemethods)
- `->`[`excludeMethods()`](#method-excludemethods)
- `->`[`includeMimeTypes()`](#method-includemimetypes)
- `->`[`excludeMimeTypes()`](#method-excludemimetypes)
- `->`[`minGzipSize()`](#method-mingzipsize)
- `->`[`compressLevel()`](#method-compresslevel)
- `->`[`__invoke()`](#method-__invoke)
- См. также в родительском классе [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.ru.md)

---
# Методы

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