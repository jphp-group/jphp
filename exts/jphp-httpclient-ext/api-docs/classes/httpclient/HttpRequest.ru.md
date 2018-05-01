# HttpRequest

- **класс** `HttpRequest` (`httpclient\HttpRequest`)
- **исходники** `httpclient/HttpRequest.php`

**Описание**

Class HttpRequest

---

#### Свойства

- `->`[`method`](#prop-method) : `string`
- `->`[`type`](#prop-type) : `string|null`
- `->`[`responseType`](#prop-responsetype) : `string|null`
- `->`[`url`](#prop-url) : `string`
- `->`[`body`](#prop-body) : `mixed`
- `->`[`headers`](#prop-headers) : `array`
- `->`[`cookies`](#prop-cookies) : `array`
- `->`[`userAgent`](#prop-useragent) : `null|string`
- `->`[`bodyParser`](#prop-bodyparser) : `null|callable`
- `->`[`absoluteUrl`](#prop-absoluteurl) : `bool`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _HttpRequest constructor._
- `->`[`method()`](#method-method)
- `->`[`absoluteUrl()`](#method-absoluteurl)
- `->`[`url()`](#method-url)
- `->`[`body()`](#method-body)
- `->`[`userAgent()`](#method-useragent)
- `->`[`type()`](#method-type) - _NONE, XML, JSON, TEXT, URLENCODE, MULTIPART, STREAM_
- `->`[`responseType()`](#method-responsetype) - _XML, JSON, TEXT, STREAM_
- `->`[`cookies()`](#method-cookies)
- `->`[`headers()`](#method-headers)
- `->`[`contentType()`](#method-contenttype)
- `->`[`bodyParser()`](#method-bodyparser)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $method, string $url, array $headers, null $body): void
```
HttpRequest constructor.

---

<a name="method-method"></a>

### method()
```php
method([ null|string $method): string
```

---

<a name="method-absoluteurl"></a>

### absoluteUrl()
```php
absoluteUrl([ bool|null $value): bool|null
```

---

<a name="method-url"></a>

### url()
```php
url([ null|string $url): null|string
```

---

<a name="method-body"></a>

### body()
```php
body(null|mixed $data): mixed
```

---

<a name="method-useragent"></a>

### userAgent()
```php
userAgent([ null|string $userAgent): null|string
```

---

<a name="method-type"></a>

### type()
```php
type([ null|string $type): string
```
NONE, XML, JSON, TEXT, URLENCODE, MULTIPART, STREAM

---

<a name="method-responsetype"></a>

### responseType()
```php
responseType([ null|string $type): string
```
XML, JSON, TEXT, STREAM

---

<a name="method-cookies"></a>

### cookies()
```php
cookies([ array|null $cookies): array|null
```

---

<a name="method-headers"></a>

### headers()
```php
headers([ array|null $headers): array|null
```

---

<a name="method-contenttype"></a>

### contentType()
```php
contentType([ null|string $contentType): null|string
```

---

<a name="method-bodyparser"></a>

### bodyParser()
```php
bodyParser([ callable|null $callback): callable|null
```