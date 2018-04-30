# reflect

- **класс** `reflect` (`php\lib\reflect`)
- **пакет** `std`
- **исходники** [`php/lib/reflect.php`](./src/main/resources/JPHP-INF/sdk/php/lib/reflect.php)

**Описание**

Class reflect

---

#### Статичные Методы

- `reflect ::`[`typeOf()`](#method-typeof)
- `reflect ::`[`typeModule()`](#method-typemodule)
- `reflect ::`[`functionModule()`](#method-functionmodule)
- `reflect ::`[`newInstance()`](#method-newinstance)

---

#### Методы

- `->`[`__construct()`](#method-__construct)

---
# Статичные Методы

<a name="method-typeof"></a>

### typeOf()
```php
reflect::typeOf(object $object, bool $isLowerCase): false|string
```

---

<a name="method-typemodule"></a>

### typeModule()
```php
reflect::typeModule(string $typeName): Module|null
```

---

<a name="method-functionmodule"></a>

### functionModule()
```php
reflect::functionModule(string $funcName): Module|null
```

---

<a name="method-newinstance"></a>

### newInstance()
```php
reflect::newInstance(string $className, array $args, bool $withConstruct): object
```

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```