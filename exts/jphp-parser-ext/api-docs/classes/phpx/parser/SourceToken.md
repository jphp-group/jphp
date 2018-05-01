# SourceToken

- **class** `SourceToken` (`phpx\parser\SourceToken`)
- **source** `phpx/parser/SourceToken.php`

**Description**

Class SourceToken

---

#### Properties

- `->`[`type`](#prop-type) : `string` - _ClassStmt, FunctionStmt, Name, ..._
- `->`[`word`](#prop-word) : `string`
- `->`[`line`](#prop-line) : `int`
- `->`[`position`](#prop-position) : `int`
- `->`[`endLine`](#prop-endline) : `int`
- `->`[`endPosition`](#prop-endposition) : `int`

---

#### Methods

- `->`[`isNamedToken()`](#method-isnamedtoken)
- `->`[`isStatementToken()`](#method-isstatementtoken)
- `->`[`isExpressionToken()`](#method-isexpressiontoken)
- `->`[`isOperatorToken()`](#method-isoperatortoken)
- `->`[`getMeta()`](#method-getmeta)

---
# Methods

<a name="method-isnamedtoken"></a>

### isNamedToken()
```php
isNamedToken(): bool
```

---

<a name="method-isstatementtoken"></a>

### isStatementToken()
```php
isStatementToken(): bool
```

---

<a name="method-isexpressiontoken"></a>

### isExpressionToken()
```php
isExpressionToken(): bool
```

---

<a name="method-isoperatortoken"></a>

### isOperatorToken()
```php
isOperatorToken(): bool
```

---

<a name="method-getmeta"></a>

### getMeta()
```php
getMeta(): array
```