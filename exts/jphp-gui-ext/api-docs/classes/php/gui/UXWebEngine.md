# UXWebEngine

- **class** `UXWebEngine` (`php\gui\UXWebEngine`)
- **package** `gui`
- **source** `php/gui/UXWebEngine.php`

**Description**

Class UXWebEngine

---

#### Properties

- `->`[`document`](#prop-document) : `DomDocument`
- `->`[`history`](#prop-history) : [`UXWebHistory`](https://github.com/jphp-compiler/jphp/blob/master/exts/jphp-gui-ext/api-docs/classes/php/gui/UXWebHistory.md)
- `->`[`javaScriptEnabled`](#prop-javascriptenabled) : `bool`
- `->`[`userStyleSheetLocation`](#prop-userstylesheetlocation) : `string`
- `->`[`userDataDirectory`](#prop-userdatadirectory) : `string`
- `->`[`url`](#prop-url) : `string`
- `->`[`userAgent`](#prop-useragent) : `string`
- `->`[`location`](#prop-location) : `string`
- `->`[`title`](#prop-title) : `string`
- `->`[`state`](#prop-state) : `string READY, SCHEDULED, RUNNING, SUCCEEDED, CANCELLED, FAILED`

---

#### Methods

- `->`[`load()`](#method-load)
- `->`[`loadContent()`](#method-loadcontent)
- `->`[`reload()`](#method-reload) - _..._
- `->`[`refresh()`](#method-refresh) - _See reload()._
- `->`[`cancel()`](#method-cancel) - _Break a loading._
- `->`[`executeScript()`](#method-executescript)
- `->`[`callFunction()`](#method-callfunction)
- `->`[`addSimpleBridge()`](#method-addsimplebridge)
- `->`[`watchState()`](#method-watchstate)
- `->`[`print()`](#method-print)
- `->`[`on()`](#method-on)
- `->`[`off()`](#method-off)
- `->`[`trigger()`](#method-trigger)

---
# Methods

<a name="method-load"></a>

### load()
```php
load(string $url): void
```

---

<a name="method-loadcontent"></a>

### loadContent()
```php
loadContent(string $content, string $contentType): void
```

---

<a name="method-reload"></a>

### reload()
```php
reload(): void
```
...

---

<a name="method-refresh"></a>

### refresh()
```php
refresh(): void
```
See reload().

---

<a name="method-cancel"></a>

### cancel()
```php
cancel(): void
```
Break a loading.

---

<a name="method-executescript"></a>

### executeScript()
```php
executeScript(string $script): mixed
```

---

<a name="method-callfunction"></a>

### callFunction()
```php
callFunction(string $name, array $args): mixed
```

---

<a name="method-addsimplebridge"></a>

### addSimpleBridge()
```php
addSimpleBridge(string $name, callable $handler): void
```

---

<a name="method-watchstate"></a>

### watchState()
```php
watchState(callable $handler): void
```

---

<a name="method-print"></a>

### print()
```php
print(php\gui\printing\UXPrinterJob $printerJob): void
```

---

<a name="method-on"></a>

### on()
```php
on(string $event, callable $handler, string $group): void
```

---

<a name="method-off"></a>

### off()
```php
off(string $event, string $group): void
```

---

<a name="method-trigger"></a>

### trigger()
```php
trigger(string $event, php\gui\UXEvent $e): void
```