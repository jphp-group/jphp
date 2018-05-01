# ConnectionResponse

- **класс** `ConnectionResponse` (`php\jsoup\ConnectionResponse`)
- **исходники** `php/jsoup/ConnectionResponse.php`

**Описание**

Class ConnectionResponse

---

#### Методы

- `->`[`headers()`](#method-headers)
- `->`[`cookies()`](#method-cookies)
- `->`[`statusCode()`](#method-statuscode) - _Get the status code of the response._
- `->`[`statusMessage()`](#method-statusmessage) - _Get the status message of the response._
- `->`[`charset()`](#method-charset)
- `->`[`body()`](#method-body)
- `->`[`bodyAsBytes()`](#method-bodyasbytes)
- `->`[`contentType()`](#method-contenttype) - _Get the response content type (e.g. "text/html");_
- `->`[`parse()`](#method-parse)

---
# Методы

<a name="method-headers"></a>

### headers()
```php
headers(): array
```

---

<a name="method-cookies"></a>

### cookies()
```php
cookies(): array
```

---

<a name="method-statuscode"></a>

### statusCode()
```php
statusCode(): int
```
Get the status code of the response.

---

<a name="method-statusmessage"></a>

### statusMessage()
```php
statusMessage(): string
```
Get the status message of the response.

---

<a name="method-charset"></a>

### charset()
```php
charset(): string
```

---

<a name="method-body"></a>

### body()
```php
body(): string
```

---

<a name="method-bodyasbytes"></a>

### bodyAsBytes()
```php
bodyAsBytes(): string
```

---

<a name="method-contenttype"></a>

### contentType()
```php
contentType(): string
```
Get the response content type (e.g. "text/html");

---

<a name="method-parse"></a>

### parse()
```php
parse(): Document
```