# UXDialog

- **класс** `UXDialog` (`php\gui\UXDialog`)
- **пакет** `gui`
- **исходники** `php/gui/UXDialog.php`

**Описание**

Class UXDialog

---

#### Статичные Методы

- `UXDialog ::`[`show()`](#method-show)
- `UXDialog ::`[`showAndWait()`](#method-showandwait)
- `UXDialog ::`[`showExpanded()`](#method-showexpanded)
- `UXDialog ::`[`confirm()`](#method-confirm)
- `UXDialog ::`[`input()`](#method-input)

---
# Статичные Методы

<a name="method-show"></a>

### show()
```php
UXDialog::show(mixed $text, string $type, php\gui\UXWindow $owner): null|string
```

---

<a name="method-showandwait"></a>

### showAndWait()
```php
UXDialog::showAndWait(mixed $text, string $type, php\gui\UXWindow $owner): void
```

---

<a name="method-showexpanded"></a>

### showExpanded()
```php
UXDialog::showExpanded(mixed $text, php\gui\UXNode $content, mixed $expanded, mixed $type): void
```

---

<a name="method-confirm"></a>

### confirm()
```php
UXDialog::confirm(mixed $text, php\gui\UXWindow $owner): bool
```

---

<a name="method-input"></a>

### input()
```php
UXDialog::input(mixed $text, string $default, php\gui\UXWindow $owner): null|string
```