## JPPM
> **Менеджер пакетов JPHP**

JPPM - это менеджер пакетов для jphp похожий на `npm` (js) или` composer` (php).
JPPM поможет вам создавать и запускать приложения jphp.

> **ВАЖНО**: JPPM и JPHP требуют Java 8 или 9+. Загрузите здесь: https://java.com/download/.

### 0. Как установить jppm?

- Перейдите в https://github.com/jphp-compiler/php/releases и найдите последнюю версию jppm.
- Загрузите архив «tar.gz» или «zip» (только окна) и распакуйте его "path/to/jppm" (папку выберите сами).
- Добавьте путь к jppm bin `/path/to/jppm` в свои системные свойства, в переменную PATH пользователя ОС.
- Перезагрузите консоль!

После этого команда `jppm` будет доступна на вашей консоли. Попробуйте получить версию jppm:

```
jppm version
```

### 1. Как создать и установить jppm из источников?
- Клонировать репозиторий jphp из `https: // github.com / jphp-compiler / jphp.git`.
- Откройте каталог репо в консоли и запустите:

В Linux:
```
./gradlew packager:install
```

В Windows:
```
gradlew packager:install
```

- Добавьте путь bin jppm к вашим системным свойствам:
  - В Linux используйте `$HOME/.jppm/dist` для пути к bin.
  - В Windows используйте `%UserProfile%\.jppm\dist` для пути к bin.

- Перезагрузите консоль!

После этого команда `jppm` будет доступна на вашей консоли. Попробуйте получить версию jppm:

```
jppm version
```

Он должен печатать информацию о версии jppm.

### 2. Как создать проект (пакет)?

Запустите и выберите опции:

```
jppm init
```

- Если вы выберете `add AppPlugin (yes)`, чтобы вы могли запустить созданный пакет как приложение jphp, используйте `app:run`:

```
jppm app:run
```

Он будет печатать «Hello World» в вашей консоли. Исходнки php см. в `src/index.php`.


### 3. Как запускать и создавать приложения JPHP?

> Если вы не выбрали опцию `add AppPlugin` как `no`, используйте это руководство.

- До того, добавьте `AppPlugin` в свой `package.php.yml` (см. секцию `plugins`), например:

```yaml
name: test

plugins:
  - AppPlugin # include app plugin
  
# ...
`` `

- Теперь команды `app:run`,` app:build`, `app:clean` будут доступны.
- Добавьте зависимость компилятора jphp к вашему `package.php.yml`:

```yaml
name: test

plugins:
  - AppPlugin # include app plugin
  
deps:
  jphp-core: '*'
  jphp-zend-ext: '*'
  jphp-httpserver-ext: '*' # добавить расширение http-сервера
```

- Добавьте в приложение сценарий начальной загрузки, например `src / index.php`:

```php
<? php echo "Hello World";
```

- Добавьте путь к сценарию начальной загрузки в package.php.yml:

```yaml
name: test

plugins:
  - AppPlugin # include app plugin
  
deps:
  jphp-core: '*'
  jphp-zend-ext: '*'
  jphp-httpserver-ext: '*' # добавить расширение http-сервера
  
sources:
  - 'src' # добавить 'src' dir в качестве исходного каталога (для загрузчика классов тоже).
  
app:
  bootstrap: 'index.php'
`` `

- Теперь вы можете запустить `jppm app:run`, чтобы запустить приложение.
- И вы можете запустить `jppm app:build`, чтобы создать приложение в один исполняемый файл jar!
