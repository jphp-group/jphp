# ServerSocket

- **класс** `ServerSocket` (`php\net\ServerSocket`)
- **пакет** `std`
- **исходники** `php/net/ServerSocket.php`

**Описание**

Class SocketServer

---

#### Статичные Методы

- `ServerSocket ::`[`findAvailableLocalPort()`](#method-findavailablelocalport)
- `ServerSocket ::`[`isAvailableLocalPort()`](#method-isavailablelocalport)

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`accept()`](#method-accept)
- `->`[`bind()`](#method-bind)
- `->`[`close()`](#method-close)
- `->`[`isClosed()`](#method-isclosed)
- `->`[`isBound()`](#method-isbound) - _Returns the binding state of the ServerSocket._
- `->`[`setSoTimeout()`](#method-setsotimeout) - _Enable/disable SO_TIMEOUT with the specified timeout, in_
- `->`[`setReuseAddress()`](#method-setreuseaddress) - _Enable/disable the SO_REUSEADDR socket option._
- `->`[`setReceiveBufferSize()`](#method-setreceivebuffersize)
- `->`[`setPerformancePreferences()`](#method-setperformancepreferences) - _Sets performance preferences for this ServerSocket._

---
# Статичные Методы

<a name="method-findavailablelocalport"></a>

### findAvailableLocalPort()
```php
ServerSocket::findAvailableLocalPort(): int
```

---

<a name="method-isavailablelocalport"></a>

### isAvailableLocalPort()
```php
ServerSocket::isAvailableLocalPort(int $port): bool
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $port, int $backLog): void
```

---

<a name="method-accept"></a>

### accept()
```php
accept(): Socket
```

---

<a name="method-bind"></a>

### bind()
```php
bind(string $hostname, int $port, int $backLog): void
```

---

<a name="method-close"></a>

### close()
```php
close(): void
```

---

<a name="method-isclosed"></a>

### isClosed()
```php
isClosed(): bool
```

---

<a name="method-isbound"></a>

### isBound()
```php
isBound(): bool
```
Returns the binding state of the ServerSocket.

---

<a name="method-setsotimeout"></a>

### setSoTimeout()
```php
setSoTimeout(int $timeout): void
```
Enable/disable SO_TIMEOUT with the specified timeout, in
milliseconds.

---

<a name="method-setreuseaddress"></a>

### setReuseAddress()
```php
setReuseAddress(bool $on): void
```
Enable/disable the SO_REUSEADDR socket option.

---

<a name="method-setreceivebuffersize"></a>

### setReceiveBufferSize()
```php
setReceiveBufferSize(int $size): void
```

---

<a name="method-setperformancepreferences"></a>

### setPerformancePreferences()
```php
setPerformancePreferences(int $connectTime, int $latency, int $bandWidth): void
```
Sets performance preferences for this ServerSocket.

! Not implemented yet for TCP/IP