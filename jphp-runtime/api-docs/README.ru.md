#### [English](README.md) / **Русский**

---

## jphp-runtime
> версия 1.0.0, создано с помощью JPPM v0.1.16

Runtime for JPHP + Standard library.

### Установка
```
jppm add jphp-runtime@1.0.0
```

### АПИ
**Классы**
- [`php\concurrent\Future`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/Future.ru.md)- _Class Future_
- [`php\concurrent\Promise`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/Promise.ru.md)- _Class Promise_
- [`php\concurrent\TimeoutException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/TimeoutException.ru.md)- _Class TimeoutException_
- [`php\format\IniProcessor`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/IniProcessor.ru.md)- _Class IniProcessor_
- [`php\format\Processor`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/Processor.ru.md)- _Class Processor_
- [`php\format\ProcessorException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/ProcessorException.ru.md)- _Class ProcessorException_
- [`php\io\File`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/File.ru.md)- _Class File_
- [`php\io\FileStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/FileStream.ru.md)- _Class FileStream_
- [`php\io\IOException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/IOException.ru.md)- _Class IOException_
- [`php\io\MemoryStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MemoryStream.ru.md)- _Class MemoryStream_
- [`php\io\MiscStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MiscStream.ru.md)- _Class MiscStream_
- [`php\io\ResourceStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/ResourceStream.ru.md)- _Class ResourceStream_
- [`php\io\Stream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.ru.md)- _Class Stream_
- [`php\lang\ClassLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ClassLoader.ru.md)- _Class ClassLoader_
- [`php\lang\Environment`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Environment.ru.md)- _Class Environment_
- [`php\lang\IllegalArgumentException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalArgumentException.ru.md)- _Class IllegalArgumentException_
- [`php\lang\IllegalStateException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalStateException.ru.md)- _Class IllegalStateException_
- [`php\lang\InterruptedException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/InterruptedException.ru.md)- _Class InterruptedException_
- [`php\lang\Invoker`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Invoker.ru.md)- _Класс для вызова методов/функций/и т.д._
- [`php\lang\JavaClass`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaClass.ru.md)- _Class JavaClass_
- [`php\lang\JavaException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaException.ru.md)- _Class JavaException_
- [`php\lang\JavaField`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaField.ru.md)
- [`php\lang\JavaMethod`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaMethod.ru.md)
- [`php\lang\JavaObject`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaObject.ru.md)- _Class JavaObject_
- [`php\lang\JavaReflection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaReflection.ru.md)
- [`php\lang\Module`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Module.ru.md)- _Class Module_
- [`php\lang\NotImplementedException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NotImplementedException.ru.md)- _Class NotImplementedException_
- [`php\lang\NumberFormatException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NumberFormatException.ru.md)- _Class NumberFormatException_
- [`php\lang\Package`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Package.ru.md)- _Class Package_
- [`php\lang\PackageLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/PackageLoader.ru.md)
- [`php\lang\Process`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Process.ru.md)- _Class Process_
- [`php\lang\SourceMap`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/SourceMap.ru.md)- _Class SourceMap_
- [`php\lang\System`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/System.ru.md)- _Class System_
- [`php\lang\Thread`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Thread.ru.md)- _Class Thread_
- [`php\lang\ThreadGroup`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadGroup.ru.md)- _Class ThreadGroup_
- [`php\lang\ThreadLocal`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadLocal.ru.md)- _Class ThreadLocal_
- [`php\lang\ThreadPool`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadPool.ru.md)- _Class ThreadPool_
- [`php\lib\arr`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/arr.ru.md)- _Библиотека для работы с коллекциями - массивы, итераторы и т.д._
- [`php\lib\bin`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/bin.ru.md)- _Class for working with binary strings_
- [`php\lib\binary`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/binary.ru.md)- _Class for working with binary strings_
- [`php\lib\char`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/char.ru.md)- _Char Utils for working with unicode chars_
- [`php\lib\fs`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/fs.ru.md)- _File System class._
- [`php\lib\items`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/items.ru.md)- _Class items_
- [`php\lib\Mirror`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/Mirror.ru.md)- _Reflection Lib._
- [`php\lib\num`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/num.ru.md)- _Utils for numbers_
- [`php\lib\number`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/number.ru.md)- _Utils for numbers_
- [`php\lib\reflect`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/reflect.ru.md)- _Class reflect_
- [`php\lib\rx`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/rx.ru.md)- _Class rx_
- [`php\lib\str`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/str.ru.md)- _Класс для работы со строками._
- [`php\net\NetAddress`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/NetAddress.ru.md)- _Class NetAddress_
- [`php\net\NetStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/NetStream.ru.md)- _http, ftp protocols_
- [`php\net\Proxy`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/Proxy.ru.md)- _Class Proxy_
- [`php\net\ServerSocket`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/ServerSocket.ru.md)- _Class SocketServer_
- [`php\net\Socket`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/Socket.ru.md)- _Class Socket_
- [`php\net\SocketException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/SocketException.ru.md)- _Class SocketException_
- [`php\net\URL`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URL.ru.md)- _Class URL_
- [`php\net\URLConnection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URLConnection.ru.md)- _Class URLConnection_
- [`php\time\Time`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/Time.ru.md)- _Class Time, Immutable_
- [`php\time\TimeFormat`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/TimeFormat.ru.md)- _Class TimeFormat, Immutable_
- [`php\time\Timer`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/Timer.ru.md)- _Class Timer_
- [`php\time\TimeZone`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/TimeZone.ru.md)- _Class TimeZone, Immutable_
- [`php\util\Configuration`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Configuration.ru.md)- _Class Configuration_
- [`php\util\Flow`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Flow.ru.md)- _A special class to work with arrays and iterators under flows._
- [`php\util\LauncherClassLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/LauncherClassLoader.ru.md)- _Class LauncherClassLoader_
- [`php\util\Locale`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Locale.ru.md)- _Class Locale, Immutable_
- [`php\util\Regex`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Regex.ru.md)- _http://www.regular-expressions.info/java.html_
- [`php\util\RegexException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/RegexException.ru.md)- _Class RegexException_
- [`php\util\Scanner`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Scanner.ru.md)- _A simple text scanner which can parse primitive types and strings using_
- [`php\util\Shared`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Shared.ru.md)- _Class to work with shared memory of Environments_
- [`php\util\SharedCollection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.ru.md)- _Class SharedCollection_
- [`php\util\SharedMap`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedMap.ru.md)- _Class SharedMap_
- [`php\util\SharedMemory`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedMemory.ru.md)- _Class SharedMemory_
- [`php\util\SharedQueue`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedQueue.ru.md)- _Class SharedQueue_
- [`php\util\SharedStack`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedStack.ru.md)- _Class SharedStack_
- [`php\util\SharedValue`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedValue.ru.md)- _Class SharedValue_