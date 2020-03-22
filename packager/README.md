## JPPM
> **JPHP Package Manager**

JPPM is a packager manager for jphp like `npm` (js) or `composer` (php). 
JPPM will help you to build and run jphp applications.

> **IMPORTANT**: JPPM and JPHP requires Java 8 or 9+. Download here: https://java.com/download/.

### 0. How to install jppm?
**For Windows Users**
- Download the installer (\*.exe) of the last jppm version here: https://github.com/jphp-compiler/jphp/releases
- Run the downloaded installer (`jppm-setup-{version}.exe`).

**For Linux Users**
- Use the linux install instructions from the last jppm version here: https://github.com/jphp-compiler/jphp/releases

That's all! Try to check jppm in console.

```
jppm version
```

### 1. How to build and install jppm from sources?
- Clone the jphp repository from `https://github.com/jphp-compiler/jphp.git`.
- Open repo directory in your console and run:

On Linux (it will create link of jppm to `/usr/bin/jppm`, use `sudo` if needed):
```
sudo ./gradlew packager:install --no-daemon
```

On Windows:
```
gradlew packager:install --no-daemon
```

- [Only if Windows] Add the jppm bin path to your system properties, use `%UserProfile%\.jppm\dist` for bin path.

- Restart your console!

After all of this the `jppm` command will available in your console. Try to get version of jppm:

```
jppm version
```

It should print a version information about jphp.

### 2. How to create project (package)?

Run and select options:

```
jppm init
```

- If you choose `add AppPlugin (yes)` so you can run the created package as jphp application, use `start`:

```
jppm start
```

It will println `Hello World` in your console. The php source of the package see in `src/index.php`.


### 3. How to run and build JPHP apps?

> If you didn't choose the `add AppPlugin` option as `no`, use this manual.

- Before, add `AppPlugin` to your `package.php.yml` (see `plugins` sections), e.g.:

```yaml
name: test

plugins: 
  - AppPlugin # include app plugin
  
# ...  
```

- Now, the command `start`, `build`, `clean` will be availble.
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
  
includes:
  - 'index.php' # include php files at start up from sources directories.
```

- Now, you can run `jppm start` to run you app.
- And you can run `jppm build` to build your app with launch scripts for windows and linux!
