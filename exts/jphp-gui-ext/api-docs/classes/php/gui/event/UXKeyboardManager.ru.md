# UXKeyboardManager

- **класс** `UXKeyboardManager` (`php\gui\event\UXKeyboardManager`)
- **пакет** `gui`
- **исходники** `php/gui/event/UXKeyboardManager.php`

**Описание**

Менеджер для отлова событий клавиатуры.

Class UXKeyboardManager

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`free()`](#method-free) - _Уничтожить менеджер_
- `->`[`onPress()`](#method-onpress)
- `->`[`onDown()`](#method-ondown)
- `->`[`onUp()`](#method-onup)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(php\gui\UXWindow $window): void
```

---

<a name="method-free"></a>

### free()
```php
free(): void
```
Уничтожить менеджер

---

<a name="method-onpress"></a>

### onPress()
```php
onPress(string $keyCombination, callable $handler, string $group): void
```

---

<a name="method-ondown"></a>

### onDown()
```php
onDown(string $keyCombination, callable $handler, string $group): void
```

---

<a name="method-onup"></a>

### onUp()
```php
onUp(string $keyCombination, callable $handler, string $group): void
```