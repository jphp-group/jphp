# NetAddress

- **класс** `NetAddress` (`php\net\NetAddress`)
- **пакет** `std`
- **исходники** `php/net/NetAddress.php`

**Описание**

Class NetAddress

---

#### Статичные Методы

- `NetAddress ::`[`getAllByName()`](#method-getallbyname)
- `NetAddress ::`[`getByAddress()`](#method-getbyaddress)
- `NetAddress ::`[`getLoopbackAddress()`](#method-getloopbackaddress) - _Returns the loopback address._
- `NetAddress ::`[`getLocalHost()`](#method-getlocalhost) - _Returns the address of the local host. This is achieved by retrieving_

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _Determines the IP address of a host, given the host's name._
- `->`[`hostName()`](#method-hostname) - _Gets the host name for this IP address._
- `->`[`hostAddress()`](#method-hostaddress) - _Returns the IP address string in textual presentation._
- `->`[`address()`](#method-address) - _Returns the raw IP address of this NetAddress_
- `->`[`canonicalHostName()`](#method-canonicalhostname) - _Gets the fully qualified domain name for this IP address._
- `->`[`__toString()`](#method-__tostring)
- `->`[`isReachable()`](#method-isreachable) - _Test whether that address is reachable. Best effort is made by the_
- `->`[`isSiteLocalAddress()`](#method-issitelocaladdress)
- `->`[`isMulticastAddress()`](#method-ismulticastaddress)
- `->`[`isAnyLocalAddress()`](#method-isanylocaladdress)
- `->`[`isLoopbackAddress()`](#method-isloopbackaddress)
- `->`[`isLinkLocalAddress()`](#method-islinklocaladdress)
- `->`[`isMCGlobal()`](#method-ismcglobal)
- `->`[`isMCNodeLocal()`](#method-ismcnodelocal)
- `->`[`isMCLinkLocal()`](#method-ismclinklocal)
- `->`[`isMCSiteLocal()`](#method-ismcsitelocal)
- `->`[`isMCOrgLocal()`](#method-ismcorglocal)

---
# Статичные Методы

<a name="method-getallbyname"></a>

### getAllByName()
```php
NetAddress::getAllByName(string $host): NetAddress[]
```

---

<a name="method-getbyaddress"></a>

### getByAddress()
```php
NetAddress::getByAddress(int[] $address): NetAddress
```

---

<a name="method-getloopbackaddress"></a>

### getLoopbackAddress()
```php
NetAddress::getLoopbackAddress(): NetAddress
```
Returns the loopback address.

The NetAddress returned will represent the IPv4
loopback address, 127.0.0.1, or the IPv6 loopback
address, ::1. The IPv4 loopback address returned
is only one of many in the form 127.*.*.*

---

<a name="method-getlocalhost"></a>

### getLocalHost()
```php
NetAddress::getLocalHost(): NetAddress
```
Returns the address of the local host. This is achieved by retrieving
the name of the host from the system, then resolving that name into
an NetAddress.

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $host): void
```
Determines the IP address of a host, given the host's name.

---

<a name="method-hostname"></a>

### hostName()
```php
hostName(): string
```
Gets the host name for this IP address.

---

<a name="method-hostaddress"></a>

### hostAddress()
```php
hostAddress(): string
```
Returns the IP address string in textual presentation.

---

<a name="method-address"></a>

### address()
```php
address(): int[]
```
Returns the raw IP address of this NetAddress
object. The result is in network byte order: the highest order
byte of the address is in address()[0].

---

<a name="method-canonicalhostname"></a>

### canonicalHostName()
```php
canonicalHostName(): string
```
Gets the fully qualified domain name for this IP address.
Best effort method, meaning we may not be able to return
the FQDN depending on the underlying system configuration.

---

<a name="method-__tostring"></a>

### __toString()
```php
__toString(): string
```

---

<a name="method-isreachable"></a>

### isReachable()
```php
isReachable(int $timeout): bool
```
Test whether that address is reachable. Best effort is made by the
implementation to try to reach the host, but firewalls and server
configuration may block requests resulting in a unreachable status
while some specific ports may be accessible.
A typical implementation will use ICMP ECHO REQUESTs if the
privilege can be obtained, otherwise it will try to establish
a TCP connection on port 7 (Echo) of the destination host.

---

<a name="method-issitelocaladdress"></a>

### isSiteLocalAddress()
```php
isSiteLocalAddress(): bool
```

---

<a name="method-ismulticastaddress"></a>

### isMulticastAddress()
```php
isMulticastAddress(): bool
```

---

<a name="method-isanylocaladdress"></a>

### isAnyLocalAddress()
```php
isAnyLocalAddress(): bool
```

---

<a name="method-isloopbackaddress"></a>

### isLoopbackAddress()
```php
isLoopbackAddress(): bool
```

---

<a name="method-islinklocaladdress"></a>

### isLinkLocalAddress()
```php
isLinkLocalAddress(): bool
```

---

<a name="method-ismcglobal"></a>

### isMCGlobal()
```php
isMCGlobal(): bool
```

---

<a name="method-ismcnodelocal"></a>

### isMCNodeLocal()
```php
isMCNodeLocal(): bool
```

---

<a name="method-ismclinklocal"></a>

### isMCLinkLocal()
```php
isMCLinkLocal(): bool
```

---

<a name="method-ismcsitelocal"></a>

### isMCSiteLocal()
```php
isMCSiteLocal(): bool
```

---

<a name="method-ismcorglocal"></a>

### isMCOrgLocal()
```php
isMCOrgLocal(): bool
```