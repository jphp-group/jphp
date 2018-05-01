# URLConnection

- **класс** `URLConnection` (`php\net\URLConnection`)
- **пакет** `std`
- **исходники** `php/net/URLConnection.php`

**Описание**

Class URLConnection

---

#### Свойства

- `->`[`doOutput`](#prop-dooutput) : `bool`
- `->`[`doInput`](#prop-doinput) : `bool`
- `->`[`requestMethod`](#prop-requestmethod) : `string` - _POST, GET, PUT, etc._
- `->`[`connectTimeout`](#prop-connecttimeout) : `int millis` - _that specifies the connect timeout value in milliseconds_
- `->`[`readTimeout`](#prop-readtimeout) : `int millis` - _the read timeout to a specified timeout, in milliseconds._
- `->`[`useCaches`](#prop-usecaches) : `bool`
- `->`[`ifModifiedSince`](#prop-ifmodifiedsince) : `int millis`
- `->`[`followRedirects`](#prop-followredirects) : `bool`
- `->`[`url`](#prop-url) : [`URL`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URL.ru.md)
- `->`[`responseCode`](#prop-responsecode) : `int`
- `->`[`responseMessage`](#prop-responsemessage) : `int`
- `->`[`contentLength`](#prop-contentlength) : `int bytes` - _int the content length of the resource that this connection's URL
references, -1 if the content length is not known,
or if the content length is greater than Integer.MAX_VALUE._
- `->`[`contentType`](#prop-contenttype) : `string`
- `->`[`contentEncoding`](#prop-contentencoding) : `string`
- `->`[`expiration`](#prop-expiration) : `int`
- `->`[`lastModified`](#prop-lastmodified) : `int`
- `->`[`usingProxy`](#prop-usingproxy) : `bool`

---

#### Статичные Методы

- `URLConnection ::`[`guessContentTypeFromStream()`](#method-guesscontenttypefromstream) - _Tries to determine the type of an input stream based on the_
- `URLConnection ::`[`guessContentTypeFromName()`](#method-guesscontenttypefromname)
- `URLConnection ::`[`create()`](#method-create)
- `URLConnection ::`[`enableSSLVerificationForHttps()`](#method-enablesslverificationforhttps) - _Enable checking ssl for https_
- `URLConnection ::`[`disableSSLVerificationForHttps()`](#method-disablesslverificationforhttps) - _Disable checking ssl for https_

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`connect()`](#method-connect) - _Opens a communications link to the resource referenced by this_
- `->`[`getHeaderField()`](#method-getheaderfield)
- `->`[`getHeaderFields()`](#method-getheaderfields)
- `->`[`getInputStream()`](#method-getinputstream)
- `->`[`getErrorStream()`](#method-geterrorstream)
- `->`[`getOutputStream()`](#method-getoutputstream)
- `->`[`setRequestProperty()`](#method-setrequestproperty)
- `->`[`getRequestProperty()`](#method-getrequestproperty)
- `->`[`getRequestProperties()`](#method-getrequestproperties)
- `->`[`disconnect()`](#method-disconnect) - _Indicates that other requests to the server_
- `->`[`setChunkedStreamingMode()`](#method-setchunkedstreamingmode) - _This method is used to enable streaming of a HTTP request body_

---
# Статичные Методы

<a name="method-guesscontenttypefromstream"></a>

### guessContentTypeFromStream()
```php
URLConnection::guessContentTypeFromStream(php\io\Stream $stream): string
```
Tries to determine the type of an input stream based on the
characters at the beginning of the input stream. This method can
be used by subclasses that override the
<code>getContentType</code> method.

---

<a name="method-guesscontenttypefromname"></a>

### guessContentTypeFromName()
```php
URLConnection::guessContentTypeFromName(string $name): string
```

---

<a name="method-create"></a>

### create()
```php
URLConnection::create(string $url, php\net\Proxy $proxy): URLConnection
```

---

<a name="method-enablesslverificationforhttps"></a>

### enableSSLVerificationForHttps()
```php
URLConnection::enableSSLVerificationForHttps(): void
```
Enable checking ssl for https

---

<a name="method-disablesslverificationforhttps"></a>

### disableSSLVerificationForHttps()
```php
URLConnection::disableSSLVerificationForHttps(): void
```
Disable checking ssl for https

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\net\URLConnection $parent): void
```

---

<a name="method-connect"></a>

### connect()
```php
connect(): void
```
Opens a communications link to the resource referenced by this
URL, if such a connection has not already been established.

---

<a name="method-getheaderfield"></a>

### getHeaderField()
```php
getHeaderField(string $name): void
```

---

<a name="method-getheaderfields"></a>

### getHeaderFields()
```php
getHeaderFields(): array
```

---

<a name="method-getinputstream"></a>

### getInputStream()
```php
getInputStream(): Stream
```

---

<a name="method-geterrorstream"></a>

### getErrorStream()
```php
getErrorStream(): Stream
```

---

<a name="method-getoutputstream"></a>

### getOutputStream()
```php
getOutputStream(): Stream
```

---

<a name="method-setrequestproperty"></a>

### setRequestProperty()
```php
setRequestProperty(string $name, string $value): void
```

---

<a name="method-getrequestproperty"></a>

### getRequestProperty()
```php
getRequestProperty(string $name): void
```

---

<a name="method-getrequestproperties"></a>

### getRequestProperties()
```php
getRequestProperties(): array
```

---

<a name="method-disconnect"></a>

### disconnect()
```php
disconnect(): void
```
Indicates that other requests to the server
are unlikely in the near future. Calling disconnect()
should not imply that this HttpURLConnection
instance can be reused for other requests.

---

<a name="method-setchunkedstreamingmode"></a>

### setChunkedStreamingMode()
```php
setChunkedStreamingMode(int $chunklen): void
```
This method is used to enable streaming of a HTTP request body
without internal buffering, when the content length is <b>not</b>
known in advance. In this mode, chunked transfer encoding
is used to send the request body. Note, not all HTTP servers
support this mode.