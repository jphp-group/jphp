# HttpResponse

- **класс** `HttpResponse` (`httpclient\HttpResponse`)
- **исходники** `httpclient/HttpResponse.php`

**Описание**

Class HttpResponse

---

#### Свойства

- `->`[`body`](#prop-body) : `mixed`
- `->`[`responseCode`](#prop-responsecode) : `mixed`
- `->`[`statusMessage`](#prop-statusmessage) : `mixed`
- `->`[`headers`](#prop-headers) : `mixed`
- `->`[`cookies`](#prop-cookies) : `mixed`
- `->`[`time`](#prop-time) : `Time`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _HttpResponse constructor._
- `->`[`body()`](#method-body)
- `->`[`statusCode()`](#method-statuscode)
- `->`[`statusMessage()`](#method-statusmessage)
- `->`[`headers()`](#method-headers)
- `->`[`header()`](#method-header) - _Returns header value._
- `->`[`contentType()`](#method-contenttype) - _Returns Content-Type header value._
- `->`[`contentLength()`](#method-contentlength) - _Content-Length header value, returns -1 if it does not exist._
- `->`[`cookie()`](#method-cookie)
- `->`[`cookies()`](#method-cookies) - _Return array of Set-Cookie header._
- `->`[`isSuccess()`](#method-issuccess) - _Check http code >= 200 and <= 399_
- `->`[`isFail()`](#method-isfail) - _Check http code >= 400_
- `->`[`isError()`](#method-iserror) - _Check http code >= 400_
- `->`[`isBadRequest()`](#method-isbadrequest) - _Check http code is 400._
- `->`[`isAuthRequired()`](#method-isauthrequired) - _Check http code is 401._
- `->`[`isPaymentRequired()`](#method-ispaymentrequired) - _Check http code is 402._
- `->`[`isNotFound()`](#method-isnotfound) - _Check http code is 404_
- `->`[`isAccessDenied()`](#method-isaccessdenied) - _Check http code is 403_
- `->`[`isInvalidMethod()`](#method-isinvalidmethod) - _Check http code is 405_
- `->`[`isServerError()`](#method-isservererror) - _Check http code >= 500_
- `->`[`isServiceUnavailable()`](#method-isserviceunavailable) - _Check http code is 503._
- `->`[`time()`](#method-time) - _Time of creation response._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
HttpResponse constructor.

---

<a name="method-body"></a>

### body()
```php
body(mixed $data): mixed|string|array|Stream
```

---

<a name="method-statuscode"></a>

### statusCode()
```php
statusCode([ int $responseCode): int
```

---

<a name="method-statusmessage"></a>

### statusMessage()
```php
statusMessage([ string $statusMessage): string
```

---

<a name="method-headers"></a>

### headers()
```php
headers([ array $headerFields): array
```

---

<a name="method-header"></a>

### header()
```php
header(string $name): mixed
```
Returns header value.

---

<a name="method-contenttype"></a>

### contentType()
```php
contentType([ string $contentType): string
```
Returns Content-Type header value.

---

<a name="method-contentlength"></a>

### contentLength()
```php
contentLength([ int $size): int
```
Content-Length header value, returns -1 if it does not exist.

---

<a name="method-cookie"></a>

### cookie()
```php
cookie(string $name): string|array
```

---

<a name="method-cookies"></a>

### cookies()
```php
cookies(array $data): array
```
Return array of Set-Cookie header.

---

<a name="method-issuccess"></a>

### isSuccess()
```php
isSuccess(): bool
```
Check http code >= 200 and <= 399

---

<a name="method-isfail"></a>

### isFail()
```php
isFail(): bool
```
Check http code >= 400

---

<a name="method-iserror"></a>

### isError()
```php
isError(): bool
```
Check http code >= 400

---

<a name="method-isbadrequest"></a>

### isBadRequest()
```php
isBadRequest(): bool
```
Check http code is 400.

---

<a name="method-isauthrequired"></a>

### isAuthRequired()
```php
isAuthRequired(): bool
```
Check http code is 401.

---

<a name="method-ispaymentrequired"></a>

### isPaymentRequired()
```php
isPaymentRequired(): bool
```
Check http code is 402.

---

<a name="method-isnotfound"></a>

### isNotFound()
```php
isNotFound(): bool
```
Check http code is 404

---

<a name="method-isaccessdenied"></a>

### isAccessDenied()
```php
isAccessDenied(): bool
```
Check http code is 403

---

<a name="method-isinvalidmethod"></a>

### isInvalidMethod()
```php
isInvalidMethod(): bool
```
Check http code is 405

---

<a name="method-isservererror"></a>

### isServerError()
```php
isServerError(): bool
```
Check http code >= 500

---

<a name="method-isserviceunavailable"></a>

### isServiceUnavailable()
```php
isServiceUnavailable(): bool
```
Check http code is 503.

---

<a name="method-time"></a>

### time()
```php
time(): php\time\Time
```
Time of creation response.