# EmailBackend

- **class** `EmailBackend` (`php\mail\EmailBackend`)
- **source** `php/mail/EmailBackend.php`

**Description**

Class EmailBackend

---

#### Properties

- `->`[`hostName`](#prop-hostname) : `string` - _The host name of the SMTP server._
- `->`[`smtpPort`](#prop-smtpport) : `string` - _The listening port of the SMTP server._
- `->`[`sslSmtpPort`](#prop-sslsmtpport) : `string` - _The current SSL port used by the SMTP transport._
- `->`[`sendPartial`](#prop-sendpartial) : `bool` - _Sending partial email._
- `->`[`socketTimeout`](#prop-sockettimeout) : `int` - _The socket I/O timeout value in milliseconds._
- `->`[`socketConnectionTimeout`](#prop-socketconnectiontimeout) : `int` - _The socket connection timeout value in milliseconds._
- `->`[`sslOnConnect`](#prop-sslonconnect) : `bool` - _Whether SSL/TLS encryption for the transport is currently enabled (SMTPS/POPS)._
- `->`[`sslCheckServerIdentity`](#prop-sslcheckserveridentity) : `bool` - _Whether the server identity is checked as specified by RFC 2595._

---

#### Methods

- `->`[`getHostName()`](#method-gethostname)
- `->`[`setHostName()`](#method-sethostname)
- `->`[`getSmtpPort()`](#method-getsmtpport)
- `->`[`setSmtpPort()`](#method-setsmtpport)
- `->`[`getSslSmtpPort()`](#method-getsslsmtpport)
- `->`[`setSslSmtpPort()`](#method-setsslsmtpport)
- `->`[`isSendPartial()`](#method-issendpartial)
- `->`[`setSendPartial()`](#method-setsendpartial)
- `->`[`getSocketTimeout()`](#method-getsockettimeout)
- `->`[`setSocketTimeout()`](#method-setsockettimeout)
- `->`[`getSocketConnectionTimeout()`](#method-getsocketconnectiontimeout)
- `->`[`setSocketConnectionTimeout()`](#method-setsocketconnectiontimeout)
- `->`[`isSslOnConnect()`](#method-issslonconnect)
- `->`[`setSslOnConnect()`](#method-setsslonconnect)
- `->`[`isSslCheckServerIdentity()`](#method-issslcheckserveridentity)
- `->`[`setSslCheckServerIdentity()`](#method-setsslcheckserveridentity)
- `->`[`setAuthentication()`](#method-setauthentication) - _Sets the userName and password if authentication is needed. If this_
- `->`[`clearAuthentication()`](#method-clearauthentication) - _Disable authorization._

---
# Methods

<a name="method-gethostname"></a>

### getHostName()
```php
getHostName(): string
```

---

<a name="method-sethostname"></a>

### setHostName()
```php
setHostName(string $hostName): void
```

---

<a name="method-getsmtpport"></a>

### getSmtpPort()
```php
getSmtpPort(): string
```

---

<a name="method-setsmtpport"></a>

### setSmtpPort()
```php
setSmtpPort(string $smtpPort): void
```

---

<a name="method-getsslsmtpport"></a>

### getSslSmtpPort()
```php
getSslSmtpPort(): string
```

---

<a name="method-setsslsmtpport"></a>

### setSslSmtpPort()
```php
setSslSmtpPort(string $sslSmtpPort): void
```

---

<a name="method-issendpartial"></a>

### isSendPartial()
```php
isSendPartial(): boolean
```

---

<a name="method-setsendpartial"></a>

### setSendPartial()
```php
setSendPartial(boolean $sendPartial): void
```

---

<a name="method-getsockettimeout"></a>

### getSocketTimeout()
```php
getSocketTimeout(): int
```

---

<a name="method-setsockettimeout"></a>

### setSocketTimeout()
```php
setSocketTimeout(int $socketTimeout): void
```

---

<a name="method-getsocketconnectiontimeout"></a>

### getSocketConnectionTimeout()
```php
getSocketConnectionTimeout(): int
```

---

<a name="method-setsocketconnectiontimeout"></a>

### setSocketConnectionTimeout()
```php
setSocketConnectionTimeout(int $socketConnectionTimeout): void
```

---

<a name="method-issslonconnect"></a>

### isSslOnConnect()
```php
isSslOnConnect(): boolean
```

---

<a name="method-setsslonconnect"></a>

### setSslOnConnect()
```php
setSslOnConnect(boolean $sslOnConnect): void
```

---

<a name="method-issslcheckserveridentity"></a>

### isSslCheckServerIdentity()
```php
isSslCheckServerIdentity(): boolean
```

---

<a name="method-setsslcheckserveridentity"></a>

### setSslCheckServerIdentity()
```php
setSslCheckServerIdentity(boolean $sslCheckServerIdentity): void
```

---

<a name="method-setauthentication"></a>

### setAuthentication()
```php
setAuthentication(string $login, string $password): void
```
Sets the userName and password if authentication is needed. If this
method is not used, no authentication will be performed.

---

<a name="method-clearauthentication"></a>

### clearAuthentication()
```php
clearAuthentication(): void
```
Disable authorization.