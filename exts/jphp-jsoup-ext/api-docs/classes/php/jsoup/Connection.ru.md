# Connection

- **класс** `Connection` (`php\jsoup\Connection`)
- **исходники** `php/jsoup/Connection.php`

**Описание**

Class Connection

---

#### Методы

- `->`[`data()`](#method-data)
- `->`[`cookies()`](#method-cookies)
- `->`[`headers()`](#method-headers)
- `->`[`header()`](#method-header)
- `->`[`url()`](#method-url)
- `->`[`method()`](#method-method)
- `->`[`proxy()`](#method-proxy)
- `->`[`userAgent()`](#method-useragent)
- `->`[`maxBodySize()`](#method-maxbodysize)
- `->`[`timeout()`](#method-timeout)
- `->`[`referrer()`](#method-referrer)
- `->`[`followRedirects()`](#method-followredirects)
- `->`[`ignoreHttpErrors()`](#method-ignorehttperrors)
- `->`[`ignoreContentType()`](#method-ignorecontenttype)
- `->`[`execute()`](#method-execute)
- `->`[`get()`](#method-get)
- `->`[`post()`](#method-post)
- `->`[`request()`](#method-request)
- `->`[`response()`](#method-response)

---
# Методы

<a name="method-data"></a>

### data()
```php
data(array $data): Connection
```

---

<a name="method-cookies"></a>

### cookies()
```php
cookies(array $data): Connection
```

---

<a name="method-headers"></a>

### headers()
```php
headers(array $data): Connection
```

---

<a name="method-header"></a>

### header()
```php
header(string $name, string $value): Connection
```

---

<a name="method-url"></a>

### url()
```php
url(string $url): Connection
```

---

<a name="method-method"></a>

### method()
```php
method(string $method): Connection
```

---

<a name="method-proxy"></a>

### proxy()
```php
proxy(php\net\Proxy $proxy): Connection
```

---

<a name="method-useragent"></a>

### userAgent()
```php
userAgent(string $userAgent): Connection
```

---

<a name="method-maxbodysize"></a>

### maxBodySize()
```php
maxBodySize(int $bytes): Connection
```

---

<a name="method-timeout"></a>

### timeout()
```php
timeout(int $millis): Connection
```

---

<a name="method-referrer"></a>

### referrer()
```php
referrer(string $referrer): Connection
```

---

<a name="method-followredirects"></a>

### followRedirects()
```php
followRedirects(bool $enable): Connection
```

---

<a name="method-ignorehttperrors"></a>

### ignoreHttpErrors()
```php
ignoreHttpErrors(bool $enable): Connection
```

---

<a name="method-ignorecontenttype"></a>

### ignoreContentType()
```php
ignoreContentType(bool $enable): Connection
```

---

<a name="method-execute"></a>

### execute()
```php
execute(): ConnectionResponse
```

---

<a name="method-get"></a>

### get()
```php
get(): Document
```

---

<a name="method-post"></a>

### post()
```php
post(): Document
```

---

<a name="method-request"></a>

### request()
```php
request(): ConnectionRequest
```

---

<a name="method-response"></a>

### response()
```php
response(): ConnectionResponse
```