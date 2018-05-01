# UXApplication

- **class** `UXApplication` (`php\gui\UXApplication`)
- **package** `gui`
- **source** `php/gui/UXApplication.php`

**Description**

Class UXApplication

---

#### Static Methods

- `UXApplication ::`[`getPid()`](#method-getpid)
- `UXApplication ::`[`isUiThread()`](#method-isuithread)
- `UXApplication ::`[`getMacAddress()`](#method-getmacaddress)
- `UXApplication ::`[`setTheme()`](#method-settheme)
- `UXApplication ::`[`shutdown()`](#method-shutdown) - _Exit from app._
- `UXApplication ::`[`isShutdown()`](#method-isshutdown)
- `UXApplication ::`[`setImplicitExit()`](#method-setimplicitexit)
- `UXApplication ::`[`isImplicitExit()`](#method-isimplicitexit)
- `UXApplication ::`[`launch()`](#method-launch)
- `UXApplication ::`[`runLater()`](#method-runlater)
- `UXApplication ::`[`runLaterAndWait()`](#method-runlaterandwait)
- `UXApplication ::`[`getSplash()`](#method-getsplash) - _Splash form if it set in launcher.conf via option fx.splash=/path/to/image.png_

---
# Static Methods

<a name="method-getpid"></a>

### getPid()
```php
UXApplication::getPid(): string
```

---

<a name="method-isuithread"></a>

### isUiThread()
```php
UXApplication::isUiThread(): bool
```

---

<a name="method-getmacaddress"></a>

### getMacAddress()
```php
UXApplication::getMacAddress(): string
```

---

<a name="method-settheme"></a>

### setTheme()
```php
UXApplication::setTheme(string|Stream $value): void
```

---

<a name="method-shutdown"></a>

### shutdown()
```php
UXApplication::shutdown(): void
```
Exit from app.

---

<a name="method-isshutdown"></a>

### isShutdown()
```php
UXApplication::isShutdown(): bool
```

---

<a name="method-setimplicitexit"></a>

### setImplicitExit()
```php
UXApplication::setImplicitExit(bool $value): void
```

---

<a name="method-isimplicitexit"></a>

### isImplicitExit()
```php
UXApplication::isImplicitExit(): bool
```

---

<a name="method-launch"></a>

### launch()
```php
UXApplication::launch(callable $onStart): void
```

---

<a name="method-runlater"></a>

### runLater()
```php
UXApplication::runLater(callable $callback): void
```

---

<a name="method-runlaterandwait"></a>

### runLaterAndWait()
```php
UXApplication::runLaterAndWait(callable $callback): mixed
```

---

<a name="method-getsplash"></a>

### getSplash()
```php
UXApplication::getSplash(): UXForm|null
```
Splash form if it set in launcher.conf via option fx.splash=/path/to/image.png