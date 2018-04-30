#### **English** / [Русский](README.ru.md)

---

## jphp-runtime
> version 1.0.0, created by JPPM v0.1.16

Runtime for JPHP + Standard library.

### Install
```
jppm add jphp-runtime@1.0.0
```

### API
**Classes**
- [`php\concurrent\Future`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/Future.md)- _Class Future_
- [`php\concurrent\Promise`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/Promise.md)- _Class Promise_
- [`php\concurrent\TimeoutException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/TimeoutException.md)- _Class TimeoutException_
- [`php\format\IniProcessor`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/IniProcessor.md)- _Class IniProcessor_
- [`php\format\Processor`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/Processor.md)- _Class Processor_
- [`php\format\ProcessorException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/ProcessorException.md)- _Class ProcessorException_
- [`php\io\File`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/File.md)- _Class File_
- [`php\io\FileStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/FileStream.md)- _Class FileStream_
- [`php\io\IOException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/IOException.md)- _Class IOException_
- [`php\io\MemoryStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MemoryStream.md)- _Class MemoryStream_
- [`php\io\MiscStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MiscStream.md)- _Class MiscStream_
- [`php\io\ResourceStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/ResourceStream.md)- _Class ResourceStream_
- [`php\io\Stream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.md)- _Class Stream_
- [`php\lang\ClassLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ClassLoader.md)- _Class ClassLoader_
- [`php\lang\Environment`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Environment.md)- _Class Environment_
- [`php\lang\IllegalArgumentException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalArgumentException.md)- _Class IllegalArgumentException_
- [`php\lang\IllegalStateException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalStateException.md)- _Class IllegalStateException_
- [`php\lang\InterruptedException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/InterruptedException.md)- _Class InterruptedException_
- [`php\lang\Invoker`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Invoker.md)- _Class for calling methods/functions/etc._
- [`php\lang\JavaClass`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaClass.md)- _Class JavaClass_
- [`php\lang\JavaException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaException.md)- _Class JavaException_
- [`php\lang\JavaField`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaField.md)
- [`php\lang\JavaMethod`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaMethod.md)
- [`php\lang\JavaObject`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaObject.md)- _Class JavaObject_
- [`php\lang\JavaReflection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaReflection.md)
- [`php\lang\Module`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Module.md)- _Class Module_
- [`php\lang\NotImplementedException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NotImplementedException.md)- _Class NotImplementedException_
- [`php\lang\NumberFormatException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NumberFormatException.md)- _Class NumberFormatException_
- [`php\lang\Package`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Package.md)- _Class Package_
- [`php\lang\PackageLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/PackageLoader.md)
- [`php\lang\Process`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Process.md)- _Class Process_
- [`php\lang\SourceMap`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/SourceMap.md)- _Class SourceMap_
- [`php\lang\System`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/System.md)- _Class System_
- [`php\lang\Thread`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Thread.md)- _Class Thread_
- [`php\lang\ThreadGroup`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadGroup.md)- _Class ThreadGroup_
- [`php\lang\ThreadLocal`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadLocal.md)- _Class ThreadLocal_
- [`php\lang\ThreadPool`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadPool.md)- _Class ThreadPool_
- [`php\lib\arr`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/arr.md)- _Library for working with collections - arrays, iterators, etc._
- [`php\lib\bin`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/bin.md)- _Class for working with binary strings_
- [`php\lib\binary`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/binary.md)- _Class for working with binary strings_
- [`php\lib\char`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/char.md)- _Char Utils for working with unicode chars_
- [`php\lib\fs`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/fs.md)- _File System class._
- [`php\lib\items`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/items.md)- _Class items_
- [`php\lib\Mirror`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/Mirror.md)- _Reflection Lib._
- [`php\lib\num`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/num.md)- _Utils for numbers_
- [`php\lib\number`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/number.md)- _Utils for numbers_
- [`php\lib\reflect`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/reflect.md)- _Class reflect_
- [`php\lib\rx`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/rx.md)- _Class rx_
- [`php\lib\str`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/str.md)- _Class str_
- [`php\net\NetAddress`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/NetAddress.md)- _Class NetAddress_
- [`php\net\NetStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/NetStream.md)- _http, ftp protocols_
- [`php\net\Proxy`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/Proxy.md)- _Class Proxy_
- [`php\net\ServerSocket`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/ServerSocket.md)- _Class SocketServer_
- [`php\net\Socket`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/Socket.md)- _Class Socket_
- [`php\net\SocketException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/SocketException.md)- _Class SocketException_
- [`php\net\URL`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URL.md)- _Class URL_
- [`php\net\URLConnection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URLConnection.md)- _Class URLConnection_
- [`php\time\Time`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/Time.md)- _Class Time, Immutable_
- [`php\time\TimeFormat`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/TimeFormat.md)- _Class TimeFormat, Immutable_
- [`php\time\Timer`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/Timer.md)- _Class Timer_
- [`php\time\TimeZone`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/TimeZone.md)- _Class TimeZone, Immutable_
- [`php\util\Configuration`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Configuration.md)- _Class Configuration_
- [`php\util\Flow`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Flow.md)- _A special class to work with arrays and iterators under flows._
- [`php\util\LauncherClassLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/LauncherClassLoader.md)- _Class LauncherClassLoader_
- [`php\util\Locale`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Locale.md)- _Class Locale, Immutable_
- [`php\util\Regex`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Regex.md)- _http://www.regular-expressions.info/java.html_
- [`php\util\RegexException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/RegexException.md)- _Class RegexException_
- [`php\util\Scanner`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Scanner.md)- _A simple text scanner which can parse primitive types and strings using_
- [`php\util\Shared`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Shared.md)- _Class to work with shared memory of Environments_
- [`php\util\SharedCollection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.md)- _Class SharedCollection_
- [`php\util\SharedMap`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedMap.md)- _Class SharedMap_
- [`php\util\SharedMemory`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedMemory.md)- _Class SharedMemory_
- [`php\util\SharedQueue`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedQueue.md)- _Class SharedQueue_
- [`php\util\SharedStack`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedStack.md)- _Class SharedStack_
- [`php\util\SharedValue`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedValue.md)- _Class SharedValue_
