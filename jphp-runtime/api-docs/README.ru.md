#### [English](README.md) / **Русский**

---

## jphp-runtime
> версия 1.0.0, создано с помощью JPPM v0.1.15

Runtime for JPHP + Standard library.

### Установка
```
jppm add jphp-runtime@1.0.0
```

### АПИ
**Классы**
- [`php\concurrent\Future`](api-docs/classes/php/concurrent/Future.ru.md)- _Class Future_
- [`php\concurrent\Promise`](api-docs/classes/php/concurrent/Promise.ru.md)- _Class Promise_
- [`php\concurrent\TimeoutException`](api-docs/classes/php/concurrent/TimeoutException.ru.md)- _Class TimeoutException_
- [`php\format\IniProcessor`](api-docs/classes/php/format/IniProcessor.ru.md)- _Class IniProcessor_
- [`php\format\Processor`](api-docs/classes/php/format/Processor.ru.md)- _Class Processor_
- [`php\format\ProcessorException`](api-docs/classes/php/format/ProcessorException.ru.md)- _Class ProcessorException_
- [`php\io\File`](api-docs/classes/php/io/File.ru.md)- _Class File_
- [`php\io\FileStream`](api-docs/classes/php/io/FileStream.ru.md)- _Class FileStream_
- [`php\io\IOException`](api-docs/classes/php/io/IOException.ru.md)- _Class IOException_
- [`php\io\MemoryStream`](api-docs/classes/php/io/MemoryStream.ru.md)- _Class MemoryStream_
- [`php\io\MiscStream`](api-docs/classes/php/io/MiscStream.ru.md)- _Class MiscStream_
- [`php\io\ResourceStream`](api-docs/classes/php/io/ResourceStream.ru.md)- _Class ResourceStream_
- [`php\io\Stream`](api-docs/classes/php/io/Stream.ru.md)- _Class Stream_
- [`php\lang\ClassLoader`](api-docs/classes/php/lang/ClassLoader.ru.md)- _Class ClassLoader_
- [`php\lang\Environment`](api-docs/classes/php/lang/Environment.ru.md)- _Class Environment_
- [`php\lang\IllegalArgumentException`](api-docs/classes/php/lang/IllegalArgumentException.ru.md)- _Class IllegalArgumentException_
- [`php\lang\IllegalStateException`](api-docs/classes/php/lang/IllegalStateException.ru.md)- _Class IllegalStateException_
- [`php\lang\InterruptedException`](api-docs/classes/php/lang/InterruptedException.ru.md)- _Class InterruptedException_
- [`php\lang\Invoker`](api-docs/classes/php/lang/Invoker.ru.md)- _Класс для вызова методов/функций/и т.д._
- [`php\lang\JavaClass`](api-docs/classes/php/lang/JavaClass.ru.md)- _Class JavaClass_
- [`php\lang\JavaException`](api-docs/classes/php/lang/JavaException.ru.md)- _Class JavaException_
- [`php\lang\JavaField`](api-docs/classes/php/lang/JavaField.ru.md)
- [`php\lang\JavaMethod`](api-docs/classes/php/lang/JavaMethod.ru.md)
- [`php\lang\JavaObject`](api-docs/classes/php/lang/JavaObject.ru.md)- _Class JavaObject_
- [`php\lang\JavaReflection`](api-docs/classes/php/lang/JavaReflection.ru.md)
- [`php\lang\Module`](api-docs/classes/php/lang/Module.ru.md)- _Class Module_
- [`php\lang\NotImplementedException`](api-docs/classes/php/lang/NotImplementedException.ru.md)- _Class NotImplementedException_
- [`php\lang\NumberFormatException`](api-docs/classes/php/lang/NumberFormatException.ru.md)- _Class NumberFormatException_
- [`php\lang\Package`](api-docs/classes/php/lang/Package.ru.md)- _Class Package_
- [`php\lang\PackageLoader`](api-docs/classes/php/lang/PackageLoader.ru.md)
- [`php\lang\Process`](api-docs/classes/php/lang/Process.ru.md)- _Class Process_
- [`php\lang\SourceMap`](api-docs/classes/php/lang/SourceMap.ru.md)- _Class SourceMap_
- [`php\lang\System`](api-docs/classes/php/lang/System.ru.md)- _Class System_
- [`php\lang\Thread`](api-docs/classes/php/lang/Thread.ru.md)- _Class Thread_
- [`php\lang\ThreadGroup`](api-docs/classes/php/lang/ThreadGroup.ru.md)- _Class ThreadGroup_
- [`php\lang\ThreadLocal`](api-docs/classes/php/lang/ThreadLocal.ru.md)- _Class ThreadLocal_
- [`php\lang\ThreadPool`](api-docs/classes/php/lang/ThreadPool.ru.md)- _Class ThreadPool_
- [`php\lib\arr`](api-docs/classes/php/lib/arr.ru.md)- _Библиотека для работы с коллекциями - массивы, итераторы и т.д._
- [`php\lib\bin`](api-docs/classes/php/lib/bin.ru.md)- _Class for working with binary strings_
- [`php\lib\binary`](api-docs/classes/php/lib/binary.ru.md)- _Class for working with binary strings_
- [`php\lib\char`](api-docs/classes/php/lib/char.ru.md)- _Char Utils for working with unicode chars_
- [`php\lib\fs`](api-docs/classes/php/lib/fs.ru.md)- _File System class._
- [`php\lib\items`](api-docs/classes/php/lib/items.ru.md)- _Class items_
- [`php\lib\Mirror`](api-docs/classes/php/lib/Mirror.ru.md)- _Reflection Lib._
- [`php\lib\num`](api-docs/classes/php/lib/num.ru.md)- _Utils for numbers_
- [`php\lib\number`](api-docs/classes/php/lib/number.ru.md)- _Utils for numbers_
- [`php\lib\reflect`](api-docs/classes/php/lib/reflect.ru.md)- _Class reflect_
- [`php\lib\rx`](api-docs/classes/php/lib/rx.ru.md)- _Class rx_
- [`php\lib\str`](api-docs/classes/php/lib/str.ru.md)- _Класс для работы со строками._
- [`php\net\NetAddress`](api-docs/classes/php/net/NetAddress.ru.md)- _Class NetAddress_
- [`php\net\NetStream`](api-docs/classes/php/net/NetStream.ru.md)- _http, ftp protocols_
- [`php\net\Proxy`](api-docs/classes/php/net/Proxy.ru.md)- _Class Proxy_
- [`php\net\ServerSocket`](api-docs/classes/php/net/ServerSocket.ru.md)- _Class SocketServer_
- [`php\net\Socket`](api-docs/classes/php/net/Socket.ru.md)- _Class Socket_
- [`php\net\SocketException`](api-docs/classes/php/net/SocketException.ru.md)- _Class SocketException_
- [`php\net\URL`](api-docs/classes/php/net/URL.ru.md)- _Class URL_
- [`php\net\URLConnection`](api-docs/classes/php/net/URLConnection.ru.md)- _Class URLConnection_
- [`php\time\Time`](api-docs/classes/php/time/Time.ru.md)- _Class Time, Immutable_
- [`php\time\TimeFormat`](api-docs/classes/php/time/TimeFormat.ru.md)- _Class TimeFormat, Immutable_
- [`php\time\Timer`](api-docs/classes/php/time/Timer.ru.md)- _Class Timer_
- [`php\time\TimeZone`](api-docs/classes/php/time/TimeZone.ru.md)- _Class TimeZone, Immutable_
- [`php\util\Configuration`](api-docs/classes/php/util/Configuration.ru.md)- _Class Configuration_
- [`php\util\Flow`](api-docs/classes/php/util/Flow.ru.md)- _A special class to work with arrays and iterators under flows._
- [`php\util\LauncherClassLoader`](api-docs/classes/php/util/LauncherClassLoader.ru.md)- _Class LauncherClassLoader_
- [`php\util\Locale`](api-docs/classes/php/util/Locale.ru.md)- _Class Locale, Immutable_
- [`php\util\Regex`](api-docs/classes/php/util/Regex.ru.md)- _http://www.regular-expressions.info/java.html_
- [`php\util\RegexException`](api-docs/classes/php/util/RegexException.ru.md)- _Class RegexException_
- [`php\util\Scanner`](api-docs/classes/php/util/Scanner.ru.md)- _A simple text scanner which can parse primitive types and strings using_
- [`php\util\Shared`](api-docs/classes/php/util/Shared.ru.md)- _Class to work with shared memory of Environments_
- [`php\util\SharedCollection`](api-docs/classes/php/util/SharedCollection.ru.md)- _Class SharedCollection_
- [`php\util\SharedMap`](api-docs/classes/php/util/SharedMap.ru.md)- _Class SharedMap_
- [`php\util\SharedMemory`](api-docs/classes/php/util/SharedMemory.ru.md)- _Class SharedMemory_
- [`php\util\SharedQueue`](api-docs/classes/php/util/SharedQueue.ru.md)- _Class SharedQueue_
- [`php\util\SharedStack`](api-docs/classes/php/util/SharedStack.ru.md)- _Class SharedStack_
- [`php\util\SharedValue`](api-docs/classes/php/util/SharedValue.ru.md)- _Class SharedValue_