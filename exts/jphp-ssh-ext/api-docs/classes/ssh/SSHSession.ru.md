# SSHSession

- **класс** `SSHSession` (`ssh\SSHSession`)
- **пакет** `ssh`
- **исходники** `ssh/SSHSession.php`

**Описание**

Class SSHSession

---

#### Свойства

- `->`[`clientVersion`](#prop-clientversion) : `string`
- `->`[`host`](#prop-host) : `string`
- `->`[`port`](#prop-port) : `int`
- `->`[`timeout`](#prop-timeout) : `int`

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`setConfig()`](#method-setconfig)
- `->`[`getConfig()`](#method-getconfig)
- `->`[`connect()`](#method-connect)
- `->`[`disconnect()`](#method-disconnect)
- `->`[`setUserInfo()`](#method-setuserinfo) - _param is:_
- `->`[`getUserInfo()`](#method-getuserinfo)
- `->`[`setPassword()`](#method-setpassword)
- `->`[`setDaemonThread()`](#method-setdaemonthread)
- `->`[`sendIgnore()`](#method-sendignore)
- `->`[`sendKeepAliveMsg()`](#method-sendkeepalivemsg)
- `->`[`rekey()`](#method-rekey)
- `->`[`exec()`](#method-exec) - _Open exec channel._
- `->`[`sftp()`](#method-sftp) - _Open SFTP channel._

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-setconfig"></a>

### setConfig()
```php
setConfig(string $key, string $value): void
```

---

<a name="method-getconfig"></a>

### getConfig()
```php
getConfig(string $key): string
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

<a name="method-setuserinfo"></a>

### setUserInfo()
```php
setUserInfo([ array $userInfoHandlers): void
```
param is:
[
getPassphrase => callable(): string,
getPassword => callable(): string,
promptPassword => callable(string $msg): bool
promptPassphrase => callable(string $msg): bool
promptYesNo => callable(string $msg): bool
showMessage => callable(string $msg): void
]

---

<a name="method-getuserinfo"></a>

### getUserInfo()
```php
getUserInfo(): array|null
```

---

<a name="method-setpassword"></a>

### setPassword()
```php
setPassword(string $password): void
```

---

<a name="method-setdaemonthread"></a>

### setDaemonThread()
```php
setDaemonThread(bool $enable): void
```

---

<a name="method-sendignore"></a>

### sendIgnore()
```php
sendIgnore(): void
```

---

<a name="method-sendkeepalivemsg"></a>

### sendKeepAliveMsg()
```php
sendKeepAliveMsg(): void
```

---

<a name="method-rekey"></a>

### rekey()
```php
rekey(): void
```

---

<a name="method-exec"></a>

### exec()
```php
exec(): ssh\SSHExecChannel
```
Open exec channel.

---

<a name="method-sftp"></a>

### sftp()
```php
sftp(): ssh\SSHSftpChannel
```
Open SFTP channel.