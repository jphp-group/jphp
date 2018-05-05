# HttpDownloadFileHandler

- **класс** `HttpDownloadFileHandler` (`php\http\HttpDownloadFileHandler`) **унаследован от** [`HttpAbstractHandler`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.ru.md)
- **пакет** `http`
- **исходники** `php/http/HttpDownloadFileHandler.php`

**Описание**

Class HttpDownloadFileHandler

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _HttpDownloadFileHandler constructor._
- `->`[`file()`](#method-file)
- `->`[`fileName()`](#method-filename)
- `->`[`contentType()`](#method-contenttype)
- `->`[`reset()`](#method-reset)
- `->`[`__invoke()`](#method-__invoke)
- См. также в родительском классе [HttpAbstractHandler](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-httpserver-ext/api-docs/classes/php/http/HttpAbstractHandler.ru.md)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|File $file, string|null $fileName, string|null $contentType): void
```
HttpDownloadFileHandler constructor.

---

<a name="method-file"></a>

### file()
```php
file(): php\io\File
```

---

<a name="method-filename"></a>

### fileName()
```php
fileName(): string
```

---

<a name="method-contenttype"></a>

### contentType()
```php
contentType(): string
```

---

<a name="method-reset"></a>

### reset()
```php
reset(string|File $file, string|null $fileName, string|null $contentType): void
```

---

<a name="method-__invoke"></a>

### __invoke()
```php
__invoke(php\http\HttpServerRequest $request, php\http\HttpServerResponse $response): bool
```