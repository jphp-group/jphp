
## Contributing

1. Install JDK 8+
2. Install Gradle 4+ or use gradle wrapper (`gradlew`).
3. Build and Install JPPM from sources:
```bash
gradle packager:install
```

### `Optional` Publish jphp packages to local repo.

```bash
gradle localPublish
```

### `Optional` Publish jphp packages to hub repo (develnext.org).

1. You need to login:
```
jppm hub:login
``` 

2. Publish All!
```
gradle hubPublish
```

or only one:

2. Publish package:
```
gradle hubPublish -p exts/jphp-mysql-ext
```

### `Optional` Build api docs for packages
```
gradle docBuild # for all
gradle docBuild -p jphp-runtime # for one!
gradle docBuild -p exts/jphp-mysql-ext # for extension!
```

> API docs will be in the `api-docs` directory.

### `Optional` Use sandbox gradle project to test jphp!

```
gradle sandbox:run
```

> The `sandbox/src` directory contains the all php sources.