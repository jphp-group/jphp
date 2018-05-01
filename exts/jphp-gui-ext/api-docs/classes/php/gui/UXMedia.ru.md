# UXMedia

- **класс** `UXMedia` (`php\gui\UXMedia`)
- **пакет** `gui`
- **исходники** `php/gui/UXMedia.php`

**Описание**

Class UXMedia

---

#### Свойства

- `->`[`duration`](#prop-duration) : `int in millis`
- `->`[`width`](#prop-width) : `int`
- `->`[`height`](#prop-height) : `int`
- `->`[`source`](#prop-source) : `string`

---

#### Статичные Методы

- `UXMedia ::`[`createFromResource()`](#method-createfromresource)
- `UXMedia ::`[`createFromUrl()`](#method-createfromurl)

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

<a name="method-createfromresource"></a>

### createFromResource()
```php
UXMedia::createFromResource(string $path): UXMedia
```

---

<a name="method-createfromurl"></a>

### createFromUrl()
```php
UXMedia::createFromUrl(mixed $url): string
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $source): void
```