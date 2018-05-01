# Socket

- **класс** `Socket` (`php\net\Socket`)
- **пакет** `std`
- **исходники** `php/net/Socket.php`

**Описание**

Class Socket

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`getOutput()`](#method-getoutput)
- `->`[`getInput()`](#method-getinput)
- `->`[`getLocalAddress()`](#method-getlocaladdress)
- `->`[`getAddress()`](#method-getaddress)
- `->`[`getLocalPort()`](#method-getlocalport)
- `->`[`getPort()`](#method-getport)
- `->`[`close()`](#method-close)
- `->`[`shutdownInput()`](#method-shutdowninput)
- `->`[`shutdownOutput()`](#method-shutdownoutput)
- `->`[`isConnected()`](#method-isconnected)
- `->`[`isClosed()`](#method-isclosed)
- `->`[`isBound()`](#method-isbound)
- `->`[`isInputShutdown()`](#method-isinputshutdown)
- `->`[`isOutputShutdown()`](#method-isoutputshutdown)
- `->`[`connect()`](#method-connect) - _Connects this socket to the server_
- `->`[`bind()`](#method-bind) - _Binds the socket to a local address._
- `->`[`bindDefault()`](#method-binddefault) - _the system will pick up_
- `->`[`setSoTimeout()`](#method-setsotimeout) - _Enable/disable SO_TIMEOUT with the specified timeout, in_
- `->`[`setSoLinger()`](#method-setsolinger)
- `->`[`setReuseAddress()`](#method-setreuseaddress) - _Enable/disable the SO_REUSEADDR socket option._
- `->`[`setReceiveBufferSize()`](#method-setreceivebuffersize)
- `->`[`setTcpNoDelay()`](#method-settcpnodelay)
- `->`[`setKeepAlive()`](#method-setkeepalive)
- `->`[`setOOBInline()`](#method-setoobinline)
- `->`[`setSendBufferSize()`](#method-setsendbuffersize)
- `->`[`setTrafficClass()`](#method-settrafficclass) - _Sets traffic class or type-of-service octet in the IP_
- `->`[`setPerformancePreferences()`](#method-setperformancepreferences) - _Sets performance preferences for this ServerSocket._
- `->`[`sendUrgentData()`](#method-sendurgentdata) - _Send one byte of urgent data on the socket. The byte to be sent is the lowest eight_

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(null|string $host, null|int $port): void
```

---

<a name="method-getoutput"></a>

### getOutput()
```php
getOutput(): MiscStream
```

---

<a name="method-getinput"></a>

### getInput()
```php
getInput(): MiscStream
```

---

<a name="method-getlocaladdress"></a>

### getLocalAddress()
```php
getLocalAddress(): string
```

---

<a name="method-getaddress"></a>

### getAddress()
```php
getAddress(): string
```

---

<a name="method-getlocalport"></a>

### getLocalPort()
```php
getLocalPort(): int
```

---

<a name="method-getport"></a>

### getPort()
```php
getPort(): int
```

---

<a name="method-close"></a>

### close()
```php
close(): void
```

---

<a name="method-shutdowninput"></a>

### shutdownInput()
```php
shutdownInput(): void
```

---

<a name="method-shutdownoutput"></a>

### shutdownOutput()
```php
shutdownOutput(): void
```

---

<a name="method-isconnected"></a>

### isConnected()
```php
isConnected(): bool
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

---

<a name="method-isinputshutdown"></a>

### isInputShutdown()
```php
isInputShutdown(): bool
```

---

<a name="method-isoutputshutdown"></a>

### isOutputShutdown()
```php
isOutputShutdown(): bool
```

---

<a name="method-connect"></a>

### connect()
```php
connect(string $hostname, int $port, null|int $timeout): void
```
Connects this socket to the server

---

<a name="method-bind"></a>

### bind()
```php
bind(string $hostname, int $port): void
```
Binds the socket to a local address.

---

<a name="method-binddefault"></a>

### bindDefault()
```php
bindDefault(): void
```
the system will pick up
an ephemeral port and a valid local address to bind the socket.

---

<a name="method-setsotimeout"></a>

### setSoTimeout()
```php
setSoTimeout(int $timeout): void
```
Enable/disable SO_TIMEOUT with the specified timeout, in
milliseconds.

---

<a name="method-setsolinger"></a>

### setSoLinger()
```php
setSoLinger(bool $on, int $linger): void
```

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

<a name="method-settcpnodelay"></a>

### setTcpNoDelay()
```php
setTcpNoDelay(bool $on): void
```

---

<a name="method-setkeepalive"></a>

### setKeepAlive()
```php
setKeepAlive(bool $on): void
```

---

<a name="method-setoobinline"></a>

### setOOBInline()
```php
setOOBInline(bool $on): void
```

---

<a name="method-setsendbuffersize"></a>

### setSendBufferSize()
```php
setSendBufferSize(int $size): void
```

---

<a name="method-settrafficclass"></a>

### setTrafficClass()
```php
setTrafficClass(int $tc): void
```
Sets traffic class or type-of-service octet in the IP
header for packets sent from this Socket.

---

<a name="method-setperformancepreferences"></a>

### setPerformancePreferences()
```php
setPerformancePreferences(int $connectTime, int $latency, int $bandWidth): void
```
Sets performance preferences for this ServerSocket.

! Not implemented yet for TCP/IP

---

<a name="method-sendurgentdata"></a>

### sendUrgentData()
```php
sendUrgentData(int $data): void
```
Send one byte of urgent data on the socket. The byte to be sent is the lowest eight
bits of the data parameter.