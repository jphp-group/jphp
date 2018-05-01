# URL

- **класс** `URL` (`php\net\URL`)
- **пакет** `std`
- **исходники** `php/net/URL.php`

**Описание**

Class URL

---

#### Статичные Методы

- `URL ::`[`encode()`](#method-encode)
- `URL ::`[`decode()`](#method-decode)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`openConnection()`](#method-openconnection)
- `->`[`getAuthority()`](#method-getauthority) - _Gets the authority part of this ``URL``_
- `->`[`getPort()`](#method-getport)
- `->`[`getDefaultPort()`](#method-getdefaultport) - _Gets the default port number of the protocol associated_
- `->`[`getProtocol()`](#method-getprotocol) - _Gets the protocol name of this ``URL``_
- `->`[`getHost()`](#method-gethost)
- `->`[`getFile()`](#method-getfile) - _Gets the file name of this <code>URL</code>._
- `->`[`getPath()`](#method-getpath)
- `->`[`getQuery()`](#method-getquery)
- `->`[`getRef()`](#method-getref) - _Gets the anchor (also known as the "reference") of this URL_
- `->`[`sameFile()`](#method-samefile) - _Compares two URLs, excluding the fragment component._
- `->`[`toString()`](#method-tostring)
- `->`[`toExternalForm()`](#method-toexternalform) - _Constructs a string representation of this URL. The_
- `->`[`openStream()`](#method-openstream)
- `->`[`__toString()`](#method-__tostring)

---
# Статичные Методы

<a name="method-encode"></a>

### encode()
```php
URL::encode(string $text, string $encoding): string
```

---

<a name="method-decode"></a>

### decode()
```php
URL::decode(string $text, string $encoding): string
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $uri): void
```

---

<a name="method-openconnection"></a>

### openConnection()
```php
openConnection(php\net\Proxy $proxy): URLConnection
```

---

<a name="method-getauthority"></a>

### getAuthority()
```php
getAuthority(): string
```
Gets the authority part of this ``URL``

---

<a name="method-getport"></a>

### getPort()
```php
getPort(): int
```

---

<a name="method-getdefaultport"></a>

### getDefaultPort()
```php
getDefaultPort(): int
```
Gets the default port number of the protocol associated
with this ``URL``. If the URL scheme or the URLStreamHandler
for the URL do not define a default port number,
then -1 is returned.

---

<a name="method-getprotocol"></a>

### getProtocol()
```php
getProtocol(): string
```
Gets the protocol name of this ``URL``

---

<a name="method-gethost"></a>

### getHost()
```php
getHost(): string
```

---

<a name="method-getfile"></a>

### getFile()
```php
getFile(): string
```
Gets the file name of this <code>URL</code>.
The returned file portion will be
the same as ``getPath()``, plus the concatenation of
the value of ``getQuery()``, if any. If there is
no query portion, this method and ``getPath()`` will
return identical results.

---

<a name="method-getpath"></a>

### getPath()
```php
getPath(): string
```

---

<a name="method-getquery"></a>

### getQuery()
```php
getQuery(): string
```

---

<a name="method-getref"></a>

### getRef()
```php
getRef(): string
```
Gets the anchor (also known as the "reference") of this URL

---

<a name="method-samefile"></a>

### sameFile()
```php
sameFile(php\net\URL $url): bool
```
Compares two URLs, excluding the fragment component.

---

<a name="method-tostring"></a>

### toString()
```php
toString(): string
```

---

<a name="method-toexternalform"></a>

### toExternalForm()
```php
toExternalForm(): string
```
Constructs a string representation of this URL. The
string is created by calling the toExternalForm
method of the stream protocol handler for this object.

---

<a name="method-openstream"></a>

### openStream()
```php
openStream(): Stream
```

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```