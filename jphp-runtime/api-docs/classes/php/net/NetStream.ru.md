# NetStream

- **класс** `NetStream` (`php\net\NetStream`) **унаследован от** [`Stream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.ru.md)
- **пакет** `std`
- **исходники** `php/net/NetStream.php`

**Описание**

http, ftp protocols

Class NetStream

---

#### Методы

- `->`[`read()`](#method-read) - _{@inheritdoc}_
- `->`[`readFully()`](#method-readfully)
- `->`[`readFullyWithCallback()`](#method-readfullywithcallback)
- `->`[`write()`](#method-write) - _{@inheritdoc}_
- `->`[`eof()`](#method-eof) - _{@inheritdoc}_
- `->`[`seek()`](#method-seek) - _{@inheritdoc}_
- `->`[`getPosition()`](#method-getposition) - _{@inheritdoc}_
- `->`[`close()`](#method-close) - _{@inheritdoc}_
- `->`[`getUrl()`](#method-geturl)
- `->`[`setProxy()`](#method-setproxy)
- `->`[`getProxy()`](#method-getproxy)
- `->`[`getUrlConnection()`](#method-geturlconnection)
- См. также в родительском классе [Stream](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.ru.md)

---
# Методы

<a name="method-read"></a>

### read()
```php
read(mixed $length): void
```
{@inheritdoc}

---

<a name="method-readfully"></a>

### readFully()
```php
readFully(int $bufferSize): mixed|void
```

---

<a name="method-readfullywithcallback"></a>

### readFullyWithCallback()
```php
readFullyWithCallback(int $bufferSize, callable $callback): void
```

---

<a name="method-write"></a>

### write()
```php
write(mixed $value, mixed $length): void
```
{@inheritdoc}

---

<a name="method-eof"></a>

### eof()
```php
eof(): void
```
{@inheritdoc}

---

<a name="method-seek"></a>

### seek()
```php
seek(mixed $position): void
```
{@inheritdoc}

---

<a name="method-getposition"></a>

### getPosition()
```php
getPosition(): void
```
{@inheritdoc}

---

<a name="method-close"></a>

### close()
```php
close(): void
```
{@inheritdoc}

---

<a name="method-geturl"></a>

### getUrl()
```php
getUrl(): URL
```

---

<a name="method-setproxy"></a>

### setProxy()
```php
setProxy(php\net\Proxy $proxy): void
```

---

<a name="method-getproxy"></a>

### getProxy()
```php
getProxy(): Proxy
```

---

<a name="method-geturlconnection"></a>

### getUrlConnection()
```php
getUrlConnection(): URLConnection
```