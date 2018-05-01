# SSH

- **class** `SSH` (`ssh\SSH`)
- **package** `ssh`
- **source** `ssh/SSH.php`

**Description**

Class SSH

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`getSession()`](#method-getsession)
- `->`[`openSession()`](#method-opensession)
- `->`[`setKnownHosts()`](#method-setknownhosts)
- `->`[`addIdentity()`](#method-addidentity)
- `->`[`removeAllIdentity()`](#method-removeallidentity)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```

---

<a name="method-getsession"></a>

### getSession()
```php
getSession(string $username, string $host, int $port): ssh\SSHSession
```

---

<a name="method-opensession"></a>

### openSession()
```php
openSession(string $host, int $port): ssh\SSHSession
```

---

<a name="method-setknownhosts"></a>

### setKnownHosts()
```php
setKnownHosts(Stream|File|string $source): void
```

---

<a name="method-addidentity"></a>

### addIdentity()
```php
addIdentity(string $prvkey, string $passphrase): void
```

---

<a name="method-removeallidentity"></a>

### removeAllIdentity()
```php
removeAllIdentity(): void
```