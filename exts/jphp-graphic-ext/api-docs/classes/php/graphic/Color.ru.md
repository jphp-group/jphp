# Color

- **класс** `Color` (`php\graphic\Color`)
- **пакет** `graphic`
- **исходники** `php/graphic/Color.php`

**Описание**

Class Color

---

#### Свойства

- `->`[`red`](#prop-red) : `int`
- `->`[`green`](#prop-green) : `int`
- `->`[`blue`](#prop-blue) : `int`
- `->`[`alpha`](#prop-alpha) : `int`
- `->`[`rgb`](#prop-rgb) : `int`

---

#### Статичные Методы

- `Color ::`[`ofRGB()`](#method-ofrgb)

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _Color constructor._
- `->`[`brighter()`](#method-brighter)
- `->`[`darker()`](#method-darker)
- `->`[`toHexString()`](#method-tohexstring)

---
# Статичные Методы

<a name="method-ofrgb"></a>

### ofRGB()
```php
Color::ofRGB(int $r, int $g, int $b, float $alpha): php\graphic\Color
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string|int $value): void
```
Color constructor.

---

<a name="method-brighter"></a>

### brighter()
```php
brighter(): php\graphic\Color
```

---

<a name="method-darker"></a>

### darker()
```php
darker(): php\graphic\Color
```

---

<a name="method-tohexstring"></a>

### toHexString()
```php
toHexString(): string
```