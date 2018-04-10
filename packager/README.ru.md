## JPPM
> **Менеджер пакетов JPHP**

JPPM - это менеджер пакетов для jphp похожий на `npm` (js) или` composer` (php).
JPPM поможет вам создавать и запускать приложения jphp.

> **ВАЖНО**: JPPM и JPHP требуют Java 8 или 9+. Загрузите здесь: https://java.com/download/.

### 1. Как собрать и установить jppm из исходников?
- Клонировать репозиторий jphp из `https://github.com/jphp-compiler/jphp.git`.
- Откройте каталог репозитория в консоли и запустите:

В Linux (добавляет symlink команду jppm `/usr/bin/jppm`, используйте `sudo` если не хватает прав):
```
sudo ./gradlew packager:install --no-daemon
```

В Windows:
```
gradlew packager:install --no-daemon
```

- [Если Windows] Пропишите путь `%UserProfile%\.jppm\dist` в PATH переменной пользователя.

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

- Если вы выберете `add AppPlugin (yes)`, то сможете запустить созданный пакет как приложение jphp, используйте команду `app:run` для этого:

```
jppm app:run
```

Команда напечатает «Hello World» в вашей консоли. Исходнки php проекта см. в `src/index.php`, это точка входа.


### 3. Как запускать и создавать приложения JPHP?

> Если вы выбрали опцию `add AppPlugin` как `no` во время использования `jppm init`, то используйте это руководство.

- До того, добавьте `AppPlugin` в свой `package.php.yml` (см. секцию `plugins`), например:

```yaml
name: test

plugins:
  - AppPlugin # include app plugin
  
# ...
```

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

- Добавьте в приложение сценарий начальной загрузки, например `src/index.php`:

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
```

- Теперь вы можете запустить `jppm app:run`, чтобы запустить приложение.
- И вы можете запустить `jppm app:build`, чтобы создать приложение в один исполняемый файл jar!
