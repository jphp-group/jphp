# SystemTray

- **class** `SystemTray` (`php\desktop\SystemTray`)
- **source** `php/desktop/SystemTray.php`

**Description**

Class SystemTray

---

#### Static Methods

- `SystemTray ::`[`isSupported()`](#method-issupported)
- `SystemTray ::`[`add()`](#method-add)
- `SystemTray ::`[`remove()`](#method-remove)
- `SystemTray ::`[`getTrayIcons()`](#method-gettrayicons)
- `SystemTray ::`[`getTrayIconSize()`](#method-gettrayiconsize)

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

<a name="method-issupported"></a>

### isSupported()
```php
SystemTray::isSupported(): bool
```

---

<a name="method-add"></a>

### add()
```php
SystemTray::add(php\desktop\TrayIcon $icon): void
```

---

<a name="method-remove"></a>

### remove()
```php
SystemTray::remove(php\desktop\TrayIcon $icon): void
```

---

<a name="method-gettrayicons"></a>

### getTrayIcons()
```php
SystemTray::getTrayIcons(): TrayIcon[]
```

---

<a name="method-gettrayiconsize"></a>

### getTrayIconSize()
```php
SystemTray::getTrayIconSize(): double[]
```

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```