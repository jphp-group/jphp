# SystemTray

- **класс** `SystemTray` (`php\desktop\SystemTray`)
- **исходники** `php/desktop/SystemTray.php`

**Описание**

Class SystemTray

---

#### Статичные Методы

- `SystemTray ::`[`isSupported()`](#method-issupported)
- `SystemTray ::`[`add()`](#method-add)
- `SystemTray ::`[`remove()`](#method-remove)
- `SystemTray ::`[`getTrayIcons()`](#method-gettrayicons)
- `SystemTray ::`[`getTrayIconSize()`](#method-gettrayiconsize)

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

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
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```