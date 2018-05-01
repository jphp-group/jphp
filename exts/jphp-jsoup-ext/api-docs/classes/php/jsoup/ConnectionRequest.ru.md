# ConnectionRequest

- **класс** `ConnectionRequest` (`php\jsoup\ConnectionRequest`)
- **исходники** `php/jsoup/ConnectionRequest.php`

**Описание**

Class ConnectionRequest

---

#### Методы

- `->`[`timeout()`](#method-timeout) - _Setter and getter for timeout._
- `->`[`maxBodySize()`](#method-maxbodysize) - _Setter and getter for max of body size._
- `->`[`followRedirects()`](#method-followredirects) - _Setter and getter._
- `->`[`ignoreHttpErrors()`](#method-ignorehttperrors)
- `->`[`ignoreContentType()`](#method-ignorecontenttype)

---
# Методы

<a name="method-timeout"></a>

### timeout()
```php
timeout(int $millis): int|ConnectionRequest
```
Setter and getter for timeout.

---

<a name="method-maxbodysize"></a>

### maxBodySize()
```php
maxBodySize(int $bytes): int|ConnectionRequest
```
Setter and getter for max of body size.

---

<a name="method-followredirects"></a>

### followRedirects()
```php
followRedirects(bool $enable): bool|ConnectionRequest
```
Setter and getter.

---

<a name="method-ignorehttperrors"></a>

### ignoreHttpErrors()
```php
ignoreHttpErrors(bool $enable): bool|ConnectionRequest
```

---

<a name="method-ignorecontenttype"></a>

### ignoreContentType()
```php
ignoreContentType(bool $enable): bool|ConnectionRequest
```