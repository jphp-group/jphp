# reflect

- **class** `reflect` (`php\lib\reflect`)
- **package** `std`
- **source** [`php/lib/reflect.php`](./src/main/resources/JPHP-INF/sdk/php/lib/reflect.php)

**Description**

Class reflect

---

#### Static Methods

- `reflect ::`[`typeOf()`](#method-typeof)
- `reflect ::`[`typeModule()`](#method-typemodule)
- `reflect ::`[`functionModule()`](#method-functionmodule)
- `reflect ::`[`newInstance()`](#method-newinstance)

---

#### Methods

- `->`[`__construct()`](#method-__construct)

---
# Static Methods

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
# Methods

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```