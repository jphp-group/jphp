# JsonProcessor

- **class** `JsonProcessor` (`php\format\JsonProcessor`) **extends** `Processor` (`php\format\Processor`)
- **package** `std`
- **source** `php/format/JsonProcessor.php`

**Description**

Class JsonProcessor

---

#### Methods

- `->`[`__construct()`](#method-__construct)
- `->`[`parse()`](#method-parse)
- `->`[`format()`](#method-format)
- `->`[`formatTo()`](#method-formatto)
- `->`[`onSerialize()`](#method-onserialize)
- `->`[`onClassSerialize()`](#method-onclassserialize)

---
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(int $flags): void
```

---

<a name="method-parse"></a>

### parse()
```php
parse(string|Stream $json): mixed
```

---

<a name="method-format"></a>

### format()
```php
format(mixed $value): string
```

---

<a name="method-formatto"></a>

### formatTo()
```php
formatTo(mixed $value, php\io\Stream $output): void
```

---

<a name="method-onserialize"></a>

### onSerialize()
```php
onSerialize(string $nameOfType, callable $handler): void
```

---

<a name="method-onclassserialize"></a>

### onClassSerialize()
```php
onClassSerialize(string $className, callable $handler): void
```