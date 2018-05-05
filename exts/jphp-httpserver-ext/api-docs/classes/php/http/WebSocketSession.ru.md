# WebSocketSession

- **класс** `WebSocketSession` (`php\http\WebSocketSession`)
- **пакет** `http`
- **исходники** `php/http/WebSocketSession.php`

---

#### Методы

- `->`[`idleTimeout()`](#method-idletimeout)
- `->`[`remoteAddress()`](#method-remoteaddress)
- `->`[`sendText()`](#method-sendtext)
- `->`[`sendPartialText()`](#method-sendpartialtext)
- `->`[`close()`](#method-close)
- `->`[`disconnect()`](#method-disconnect)
- `->`[`isOpen()`](#method-isopen)
- `->`[`isSecure()`](#method-issecure)
- `->`[`protocolVersion()`](#method-protocolversion)
- `->`[`policy()`](#method-policy)
- `->`[`batchMode()`](#method-batchmode)
- `->`[`flush()`](#method-flush)

---
# Методы

<a name="method-idletimeout"></a>

### idleTimeout()
```php
idleTimeout(): int
```

---

<a name="method-remoteaddress"></a>

### remoteAddress()
```php
remoteAddress(): string
```

---

<a name="method-sendtext"></a>

### sendText()
```php
sendText(string $text, [ callable|null $callback): void
```

---

<a name="method-sendpartialtext"></a>

### sendPartialText()
```php
sendPartialText(string $text, bool $isLast): void
```

---

<a name="method-close"></a>

### close()
```php
close(int $status, string|null $reason): void
```

---

<a name="method-disconnect"></a>

### disconnect()
```php
disconnect(): void
```

---

<a name="method-isopen"></a>

### isOpen()
```php
isOpen(): bool
```

---

<a name="method-issecure"></a>

### isSecure()
```php
isSecure(): bool
```

---

<a name="method-protocolversion"></a>

### protocolVersion()
```php
protocolVersion(): string
```

---

<a name="method-policy"></a>

### policy()
```php
policy(): array
```

---

<a name="method-batchmode"></a>

### batchMode()
```php
batchMode(string $mode): string
```

---

<a name="method-flush"></a>

### flush()
```php
flush(): void
```