# Processor

- **class** `Processor` (`php\format\Processor`)
- **package** `std`
- **source** `php/format/Processor.php`

**Child Classes**

> [IniProcessor](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/IniProcessor.md)

**Description**

Class Processor

---

#### Static Methods

- `Processor ::`[`register()`](#method-register)
- `Processor ::`[`unregister()`](#method-unregister)
- `Processor ::`[`isRegistered()`](#method-isregistered)

---

#### Methods

- `->`[`format()`](#method-format)
- `->`[`formatTo()`](#method-formatto)
- `->`[`parse()`](#method-parse)

---
# Static Methods

<a name="method-register"></a>

### register()
```php
Processor::register(string $code, string $processorClass): void
```

---

<a name="method-unregister"></a>

### unregister()
```php
Processor::unregister(string $code): bool
```

---

<a name="method-isregistered"></a>

### isRegistered()
```php
Processor::isRegistered(string $code): bool
```

---
# Methods

<a name="method-format"></a>

### format()
```php
format(mixed $value): string
```

---

<a name="method-formatto"></a>

### formatTo()
```php
formatTo(mixed $value, php\io\Stream $output): mixed
```

---

<a name="method-parse"></a>

### parse()
```php
parse(string|Stream $source): mixed
```