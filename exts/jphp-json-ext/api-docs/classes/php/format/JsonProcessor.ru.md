# JsonProcessor

- **класс** `JsonProcessor` (`php\format\JsonProcessor`) **унаследован от** `Processor` (`php\format\Processor`)
- **пакет** `std`
- **исходники** `php/format/JsonProcessor.php`

**Описание**

Class JsonProcessor

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`parse()`](#method-parse)
- `->`[`format()`](#method-format)
- `->`[`formatTo()`](#method-formatto)
- `->`[`onSerialize()`](#method-onserialize)
- `->`[`onClassSerialize()`](#method-onclassserialize)

---
# Методы

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