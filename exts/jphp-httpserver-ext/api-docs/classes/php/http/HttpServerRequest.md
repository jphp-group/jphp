# HttpServerRequest

- **class** `HttpServerRequest` (`php\http\HttpServerRequest`)
- **package** `http`
- **source** `php/http/HttpServerRequest.php`

**Description**

Class HttpServerRequest

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`end()`](#method-end) - _Stop handle requests._
- `->`[`httpVersion()`](#method-httpversion)
- `->`[`protocol()`](#method-protocol) - _http, https_
- `->`[`header()`](#method-header)
- `->`[`headers()`](#method-headers)
- `->`[`cookie()`](#method-cookie)
- `->`[`cookies()`](#method-cookies)
- `->`[`param()`](#method-param)
- `->`[`attribute()`](#method-attribute)
- `->`[`query()`](#method-query)
- `->`[`queryEncoding()`](#method-queryencoding)
- `->`[`queryParameters()`](#method-queryparameters)
- `->`[`path()`](#method-path)
- `->`[`method()`](#method-method)
- `->`[`sessionId()`](#method-sessionid)
- `->`[`localAddress()`](#method-localaddress)
- `->`[`localPort()`](#method-localport)
- `->`[`localName()`](#method-localname)
- `->`[`remoteAddress()`](#method-remoteaddress)
- `->`[`remoteUser()`](#method-remoteuser)
- `->`[`remoteHost()`](#method-remotehost)
- `->`[`remotePort()`](#method-remoteport)
- `->`[`locale()`](#method-locale)
- `->`[`locales()`](#method-locales)
- `->`[`bodyStream()`](#method-bodystream)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-end"></a>

### end()
```php
end(): void
```
Stop handle requests.

---

<a name="method-httpversion"></a>

### httpVersion()
```php
httpVersion(): string
```

---

<a name="method-protocol"></a>

### protocol()
```php
protocol(): string
```
http, https

---

<a name="method-header"></a>

### header()
```php
header(string $name): string
```

---

<a name="method-headers"></a>

### headers()
```php
headers(bool $lowerKeys): array
```

---

<a name="method-cookie"></a>

### cookie()
```php
cookie(string $name): array
```

---

<a name="method-cookies"></a>

### cookies()
```php
cookies(): array
```

---

<a name="method-param"></a>

### param()
```php
param(string $name): string
```

---

<a name="method-attribute"></a>

### attribute()
```php
attribute(string $name, mixed $value): mixed
```

---

<a name="method-query"></a>

### query()
```php
query(string $name): string
```

---

<a name="method-queryencoding"></a>

### queryEncoding()
```php
queryEncoding(): string
```

---

<a name="method-queryparameters"></a>

### queryParameters()
```php
queryParameters(): array
```

---

<a name="method-path"></a>

### path()
```php
path(): string
```

---

<a name="method-method"></a>

### method()
```php
method(): string
```

---

<a name="method-sessionid"></a>

### sessionId()
```php
sessionId(): string
```

---

<a name="method-localaddress"></a>

### localAddress()
```php
localAddress(): string
```

---

<a name="method-localport"></a>

### localPort()
```php
localPort(): int
```

---

<a name="method-localname"></a>

### localName()
```php
localName(): string
```

---

<a name="method-remoteaddress"></a>

### remoteAddress()
```php
remoteAddress(): string
```

---

<a name="method-remoteuser"></a>

### remoteUser()
```php
remoteUser(): string
```

---

<a name="method-remotehost"></a>

### remoteHost()
```php
remoteHost(): string
```

---

<a name="method-remoteport"></a>

### remotePort()
```php
remotePort(): string
```

---

<a name="method-locale"></a>

### locale()
```php
locale(): php\util\Locale
```

---

<a name="method-locales"></a>

### locales()
```php
locales(): Locale[]
```

---

<a name="method-bodystream"></a>

### bodyStream()
```php
bodyStream(): php\io\Stream
```