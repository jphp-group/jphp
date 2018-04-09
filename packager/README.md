## JPPM
> **JPHP Package Manager**

JPPM is a packager manager for jphp like `npm` (js) or `composer` (php). 
JPPM will help you to build and run jphp applications.

### 1. How to install jppm?

- Clone the jphp repository from `https://github.com/jphp-compiler/jphp.git`.
- Open repo directory in your console and run:

On Linux:
```
./gradlew packager:install
```

On Windows:
```
gradlew packager:install
```

- Add the jppm bin path to your system properties:
  - On Linux use `$HOME/.jppm/dist` for bin path.
  - On Windows use `%UserProfile%\.jppm\dist` for bin path.

- Restart your console!

After all of this the `jppm` commend will available in your console.

### 2. How to create project (package)?

Run and select options:

```
jppm init
```

- If you choose `add AppPlugin (yes)` so you can run the created package as jphp application, use `app:run`:

```
jppm app:run
```

It will println `Hello World` in your console.


### 3. How to run and build JPHP apps?

- Before, add `AppPlugin` to your `package.php.yml` (see `plugins` sections), e.g.:

```yaml
name: test

plugins: 
  - AppPlugin # include app plugin
  
# ...  
```

- Now, the command `app:run`, `app:build`, `app:clean` will be availble.
- Add jphp compiler dependency to your `package.php.yml`:

```yaml
name: test

plugins: 
  - AppPlugin # include app plugin
  
deps:
  jphp-core: '*'
  jphp-zend-ext: '*'
  jphp-httpserver-ext: '*' # add http server extension  
```

- Add a bootstrap script to your app, e.g `src/index.php`: 

```php
<?php echo "Hello World";
```

- Add the path of the bootstrap script in package.php.yml:

```yaml
name: test

plugins: 
  - AppPlugin # include app plugin
  
deps:
  jphp-core: '*'
  jphp-zend-ext: '*'
  jphp-httpserver-ext: '*' # add http server extension  
  
sources:
  - 'src' # add 'src' dir as source directory (for class loader too).
  
app:
  bootstrap: 'index.php'
```

- Now, you can run `jppm app:run` to run you app.
- And you can run `jppm app:build` to build your app to one executable jar file!
