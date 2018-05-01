# Processor

- **класс** `Processor` (`php\format\Processor`)
- **пакет** `std`
- **исходники** `php/format/Processor.php`

**Классы наследники**

> [IniProcessor](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/IniProcessor.ru.md)

**Описание**

Class Processor

---

#### Статичные Методы

- `Processor ::`[`register()`](#method-register)
- `Processor ::`[`unregister()`](#method-unregister)
- `Processor ::`[`isRegistered()`](#method-isregistered)

---

#### Методы

- `->`[`format()`](#method-format)
- `->`[`formatTo()`](#method-formatto)
- `->`[`parse()`](#method-parse)

---
# Статичные Методы

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
# Методы

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