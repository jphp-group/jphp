# SSHExecChannel

- **class** `SSHExecChannel` (`ssh\SSHExecChannel`) **extends** [`SSHChannel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.md)
- **package** `ssh`
- **source** `ssh/SSHExecChannel.php`

**Description**

Class SSHExecChannel

---

#### Methods

- `->`[`setCommand()`](#method-setcommand)
- `->`[`getErrorStream()`](#method-geterrorstream)
- `->`[`setErrorStream()`](#method-seterrorstream)
- See also in the parent class [SSHChannel](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.md)

---
# Methods

<a name="method-setcommand"></a>

### setCommand()
```php
setCommand(string $command): void
```

---

<a name="method-geterrorstream"></a>

### getErrorStream()
```php
getErrorStream(): php\io\Stream
```

---

<a name="method-seterrorstream"></a>

### setErrorStream()
```php
setErrorStream(php\io\Stream $stream, bool $dontClose): void
```