# UXGameScene

- **класс** `UXGameScene` (`php\game\UXGameScene`)
- **пакет** `game`
- **исходники** `php/game/UXGameScene.php`

**Описание**

Class UXGameScene

---

#### Свойства

- `->`[`gravity`](#prop-gravity) : `array [x, y]`
- `->`[`gravityX`](#prop-gravityx) : `float`
- `->`[`gravityY`](#prop-gravityy) : `float`
- `->`[`observedObject`](#prop-observedobject) : `UXGameEntity|null`

---

#### Методы

- `->`[`__construct()`](#method-__construct) - _UXGameScene constructor._
- `->`[`add()`](#method-add)
- `->`[`remove()`](#method-remove)
- `->`[`play()`](#method-play)
- `->`[`pause()`](#method-pause)
- `->`[`clear()`](#method-clear)
- `->`[`setScrollHandler()`](#method-setscrollhandler)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(): void
```
UXGameScene constructor.

---

<a name="method-add"></a>

### add()
```php
add(php\game\UXGameEntity $entity): void
```

---

<a name="method-remove"></a>

### remove()
```php
remove(php\game\UXGameEntity $entity): void
```

---

<a name="method-play"></a>

### play()
```php
play(): void
```

---

<a name="method-pause"></a>

### pause()
```php
pause(): void
```

---

<a name="method-clear"></a>

### clear()
```php
clear(): void
```

---

<a name="method-setscrollhandler"></a>

### setScrollHandler()
```php
setScrollHandler(callable|null $handler): void
```