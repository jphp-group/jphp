# HttpClient

- **class** `HttpClient` (`httpclient\HttpClient`)
- **source** `httpclient/HttpClient.php`

**Description**

Class HttpClient

---

#### Properties

- `->`[`baseUrl`](#prop-baseurl) : `string`
- `->`[`userAgent`](#prop-useragent) : `string`
- `->`[`referrer`](#prop-referrer) : `string`
- `->`[`followRedirects`](#prop-followredirects) : `bool`
- `->`[`connectTimeout`](#prop-connecttimeout) : `string`
- `->`[`readTimeout`](#prop-readtimeout) : `string`
- `->`[`proxyType`](#prop-proxytype) : `string` - _HTTP, SOCKS,_
- `->`[`proxy`](#prop-proxy) : `string`
- `->`[`requestType`](#prop-requesttype) : `string` - _URLENCODE, MULTIPART, JSON, TEXT, XML, RAW_
- `->`[`responseType`](#prop-responsetype) : `string` - _JSON, TEXT, XML, JSOUP_
- `->`[`body`](#prop-body) : `array|mixed`
- `->`[`encoding`](#prop-encoding) : `string`
- `->`[`cookies`](#prop-cookies) : `array`
- `->`[`headers`](#prop-headers) : `array`
- `->`[`bodyParser`](#prop-bodyparser) : `null|callable`
- `->`[`handlers`](#prop-handlers) : `callable[]`
- `->`[`_boundary`](#prop-_boundary) : `string`
- `->`[`_threadPool`](#prop-_threadpool) : `ThreadPool`

---

#### Methods

- `->`[`__construct()`](#method-__construct) - _HttpClient constructor._
- `->`[`setThreadPool()`](#method-setthreadpool)
- `->`[`get()`](#method-get)
- `->`[`post()`](#method-post)
- `->`[`put()`](#method-put)
- `->`[`patch()`](#method-patch)
- `->`[`delete()`](#method-delete)
- `->`[`options()`](#method-options)
- `->`[`head()`](#method-head)
- `->`[`getAsync()`](#method-getasync)
- `->`[`postAsync()`](#method-postasync)
- `->`[`putAsync()`](#method-putasync)
- `->`[`patchAsync()`](#method-patchasync)
- `->`[`deleteAsync()`](#method-deleteasync)
- `->`[`optionsAsync()`](#method-optionsasync)
- `->`[`headAsync()`](#method-headasync)
- `->`[`sendAsync()`](#method-sendasync)
- `->`[`executeAsync()`](#method-executeasync)
- `->`[`send()`](#method-send)
- `->`[`execute()`](#method-execute)
- `->`[`connect()`](#method-connect)
- `->`[`formatUrlencode()`](#method-formaturlencode)
- `->`[`formatMultipart()`](#method-formatmultipart)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $baseUrl): void
```
HttpClient constructor.

---

<a name="method-setthreadpool"></a>

### setThreadPool()
```php
setThreadPool([ php\lang\ThreadPool $pool): void
```

---

<a name="method-get"></a>

### get()
```php
get(string $url, array $args): httpclient\HttpResponse
```

---

<a name="method-post"></a>

### post()
```php
post(string $url, mixed $body): httpclient\HttpResponse
```

---

<a name="method-put"></a>

### put()
```php
put(string $url, mixed $body): httpclient\HttpResponse
```

---

<a name="method-patch"></a>

### patch()
```php
patch(string $url, mixed $body): httpclient\HttpResponse
```

---

<a name="method-delete"></a>

### delete()
```php
delete(string $url, array $args): httpclient\HttpResponse
```

---

<a name="method-options"></a>

### options()
```php
options(string $url, array $args): httpclient\HttpResponse
```

---

<a name="method-head"></a>

### head()
```php
head(string $url, array $args): httpclient\HttpResponse
```

---

<a name="method-getasync"></a>

### getAsync()
```php
getAsync(string $url, array $args): php\concurrent\Promise
```

---

<a name="method-postasync"></a>

### postAsync()
```php
postAsync(string $url, mixed $body): php\concurrent\Promise
```

---

<a name="method-putasync"></a>

### putAsync()
```php
putAsync(string $url, mixed $body): php\concurrent\Promise
```

---

<a name="method-patchasync"></a>

### patchAsync()
```php
patchAsync(string $url, mixed $body): php\concurrent\Promise
```

---

<a name="method-deleteasync"></a>

### deleteAsync()
```php
deleteAsync(string $url, array $args): php\concurrent\Promise
```

---

<a name="method-optionsasync"></a>

### optionsAsync()
```php
optionsAsync(string $url, array $args): php\concurrent\Promise
```

---

<a name="method-headasync"></a>

### headAsync()
```php
headAsync(string $url, array $args): php\concurrent\Promise
```

---

<a name="method-sendasync"></a>

### sendAsync()
```php
sendAsync(httpclient\HttpRequest $request, [ php\lang\ThreadPool $threadPool): php\concurrent\Promise
```

---

<a name="method-executeasync"></a>

### executeAsync()
```php
executeAsync(string $method, string $url, null $body): php\concurrent\Promise
```

---

<a name="method-send"></a>

### send()
```php
send(httpclient\HttpRequest $request): httpclient\HttpResponse
```

---

<a name="method-execute"></a>

### execute()
```php
execute(string $method, string $url, mixed $body): httpclient\HttpResponse
```

---

<a name="method-connect"></a>

### connect()
```php
connect(httpclient\HttpRequest $request, mixed $fullUrl, php\net\URLConnection $connection, mixed $body): httpclient\HttpResponse
```

---

<a name="method-formaturlencode"></a>

### formatUrlencode()
```php
formatUrlencode(array $data, string $prefix): string
```

---

<a name="method-formatmultipart"></a>

### formatMultipart()
```php
formatMultipart(array $data, mixed $prefix): void
```