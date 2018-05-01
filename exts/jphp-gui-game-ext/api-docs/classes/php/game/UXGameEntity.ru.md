# UXGameEntity

- **класс** `UXGameEntity` (`php\game\UXGameEntity`)
- **пакет** `game`
- **исходники** `php/game/UXGameEntity.php`

**Описание**

Class UXGameObject

---

#### Свойства

- `->`[`node`](#prop-node) : `UXNode`
- `->`[`active`](#prop-active) : `bool`
- `->`[`x`](#prop-x) : `float`
- `->`[`y`](#prop-y) : `float`
- `->`[`centerX`](#prop-centerx) : `float`
- `->`[`centerY`](#prop-centery) : `float`
- `->`[`bodyType`](#prop-bodytype) : `string STATIC, DYNAMIC, KINEMATIC`
- `->`[`gravity`](#prop-gravity) : `array|null [x, y]` - _If null, using scene gravity_
- `->`[`gravityX`](#prop-gravityx) : `float`
- `->`[`gravityY`](#prop-gravityy) : `float`
- `->`[`gameScene`](#prop-gamescene) : `UXGameScene`
- `->`[`velocity`](#prop-velocity) : `array`
- `->`[`velocityX`](#prop-velocityx) : `float`
- `->`[`velocityY`](#prop-velocityy) : `float`
- `->`[`angleSpeed`](#prop-anglespeed) : `array alias of velocity property`
- `->`[`speed`](#prop-speed) : `float angle speed`
- `->`[`direction`](#prop-direction) : `float angle for speed, from 0 to 360`
- `->`[`hspeed`](#prop-hspeed) : `float alias of velocityX`
- `->`[`vspeed`](#prop-vspeed) : `float alias of velocityY`
- `->`[`solidType`](#prop-solidtype) : `string NONE, PLATFORM, PHYSIC`
- `->`[`solid`](#prop-solid) : `bool`
- `->`[`platform`](#prop-platform) : `bool`

---

#### Методы

- `->`[`__construct()`](#method-__construct)
- `->`[`setPolygonFixture()`](#method-setpolygonfixture)
- `->`[`setRectangleFixture()`](#method-setrectanglefixture)
- `->`[`setEllipseFixture()`](#method-setellipsefixture)
- `->`[`setCircleFixture()`](#method-setcirclefixture)
- `->`[`setCollisionHandler()`](#method-setcollisionhandler)

---
# Методы

<a name="method-__construct"></a>

### __construct()
```php
__construct(string $entityType, php\gui\UXNode $node): void
```

---

<a name="method-setpolygonfixture"></a>

### setPolygonFixture()
```php
setPolygonFixture(array $points): void
```

---

<a name="method-setrectanglefixture"></a>

### setRectangleFixture()
```php
setRectangleFixture(float $width, float $height): void
```

---

<a name="method-setellipsefixture"></a>

### setEllipseFixture()
```php
setEllipseFixture(float $width, float $height): void
```

---

<a name="method-setcirclefixture"></a>

### setCircleFixture()
```php
setCircleFixture(float $radius): void
```

---

<a name="method-setcollisionhandler"></a>

### setCollisionHandler()
```php
setCollisionHandler(string $entityType, callable|null $handler): void
```