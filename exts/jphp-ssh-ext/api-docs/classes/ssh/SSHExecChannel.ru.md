# SSHExecChannel

- **класс** `SSHExecChannel` (`ssh\SSHExecChannel`) **унаследован от** [`SSHChannel`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.ru.md)
- **пакет** `ssh`
- **исходники** `ssh/SSHExecChannel.php`

**Описание**

Class SSHExecChannel

---

#### Методы

- `->`[`setCommand()`](#method-setcommand)
- `->`[`getErrorStream()`](#method-geterrorstream)
- `->`[`setErrorStream()`](#method-seterrorstream)
- См. также в родительском классе [SSHChannel](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-ssh-ext/api-docs/classes/ssh/SSHChannel.ru.md)

---
# Методы

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