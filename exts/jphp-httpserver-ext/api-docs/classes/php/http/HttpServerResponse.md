# HttpServerResponse

- **class** `HttpServerResponse` (`php\http\HttpServerResponse`)
- **package** `http`
- **source** `php/http/HttpServerResponse.php`

**Description**

Class HttpServerResponse

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`status()`](#method-status)
- `->`[`contentType()`](#method-contenttype)
- `->`[`contentLength()`](#method-contentlength)
- `->`[`charsetEncoding()`](#method-charsetencoding)
- `->`[`write()`](#method-write)
- `->`[`body()`](#method-body)
- `->`[`header()`](#method-header)
- `->`[`redirect()`](#method-redirect)
- `->`[`addCookie()`](#method-addcookie) - _Cookie as array:_
- `->`[`encodeRedirectUrl()`](#method-encoderedirecturl)
- `->`[`encodeUrl()`](#method-encodeurl)
- `->`[`bodyStream()`](#method-bodystream) - _Output Stream for body._
- `->`[`locale()`](#method-locale)
- `->`[`flush()`](#method-flush) - _Forces any content in the buffer to be written to the client.  A call_

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-status"></a>

### status()
```php
status(int $status, string $message): php\http\HttpServerResponse
```

---

<a name="method-contenttype"></a>

### contentType()
```php
contentType(string $value): php\http\HttpServerResponse
```

---

<a name="method-contentlength"></a>

### contentLength()
```php
contentLength(int $value): php\http\HttpServerResponse
```

---

<a name="method-charsetencoding"></a>

### charsetEncoding()
```php
charsetEncoding(string $encoding): php\http\HttpServerResponse
```

---

<a name="method-write"></a>

### write()
```php
write(mixed $content, string $charset): php\http\HttpServerResponse
```

---

<a name="method-body"></a>

### body()
```php
body(string $content, string $charset): php\http\HttpServerResponse
```

---

<a name="method-header"></a>

### header()
```php
header(string $name, string $value): php\http\HttpServerResponse
```

---

<a name="method-redirect"></a>

### redirect()
```php
redirect(string $location): php\http\HttpServerResponse
```

---

<a name="method-addcookie"></a>

### addCookie()
```php
addCookie(array $cookie): php\http\HttpServerResponse
```
Cookie as array:
[
name => string (required), value => string,
path => string, domain => string,
comment => string, secure => bool, httpOnly => bool, version => int
]

---

<a name="method-encoderedirecturl"></a>

### encodeRedirectUrl()
```php
encodeRedirectUrl(string $url): string
```

---

<a name="method-encodeurl"></a>

### encodeUrl()
```php
encodeUrl(string $url): string
```

---

<a name="method-bodystream"></a>

### bodyStream()
```php
bodyStream(): php\io\Stream
```
Output Stream for body.

---

<a name="method-locale"></a>

### locale()
```php
locale(php\util\Locale $locale): php\http\HttpServerResponse
```

---

<a name="method-flush"></a>

### flush()
```php
flush(): php\http\HttpServerResponse
```
Forces any content in the buffer to be written to the client.  A call
to this method automatically commits the response, meaning the status
code and headers will be written.