# SSHChannel

- **class** `SSHChannel` (`ssh\SSHChannel`)
- **package** `ssh`
- **source** `ssh/SSHChannel.php`

**Child Classes**

> [SSHExecChannel](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHExecChannel.md), [SSHSftpChannel](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHSftpChannel.md)

**Description**

Class SSHChannel

---

#### Properties

- `->`[`id`](#prop-id) : `int`
- `->`[`exitStatus`](#prop-exitstatus) : `int`

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`connect()`](#method-connect)
- `->`[`disconnect()`](#method-disconnect)
- `->`[`isClosed()`](#method-isclosed)
- `->`[`isConnected()`](#method-isconnected)
- `->`[`isEOF()`](#method-iseof)
- `->`[`sendSignal()`](#method-sendsignal)
- `->`[`start()`](#method-start)
- `->`[`getInputStream()`](#method-getinputstream)
- `->`[`setInputStream()`](#method-setinputstream)
- `->`[`getOutputStream()`](#method-getoutputstream)
- `->`[`setOutputStream()`](#method-setoutputstream)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-connect"></a>

### connect()
```php
connect(int $timeout): void
```

---

<a name="method-disconnect"></a>

### disconnect()
```php
disconnect(): void
```

---

<a name="method-isclosed"></a>

### isClosed()
```php
isClosed(): bool
```

---

<a name="method-isconnected"></a>

### isConnected()
```php
isConnected(): bool
```

---

<a name="method-iseof"></a>

### isEOF()
```php
isEOF(): bool
```

---

<a name="method-sendsignal"></a>

### sendSignal()
```php
sendSignal(string $signal): void
```

---

<a name="method-start"></a>

### start()
```php
start(): void
```

---

<a name="method-getinputstream"></a>

### getInputStream()
```php
getInputStream(): php\io\Stream
```

---

<a name="method-setinputstream"></a>

### setInputStream()
```php
setInputStream([ php\io\Stream $stream, bool $dontClose): void
```

---

<a name="method-getoutputstream"></a>

### getOutputStream()
```php
getOutputStream(): php\io\Stream
```

---

<a name="method-setoutputstream"></a>

### setOutputStream()
```php
setOutputStream([ php\io\Stream $stream, bool $dontClose): void
```