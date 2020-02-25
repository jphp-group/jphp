#### **English** / [Русский](README.ru.md)

---

## jphp-runtime
> version 1.1.5, created by JPPM.

Runtime for JPHP + Standard library.

### Install
```
jppm add jphp-runtime@1.1.5
```

### API
**Classes**

#### `php\concurrent`

- [`Future`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/Future.md)- _Class Future_
- [`Promise`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/Promise.md)- _Class Promise_
- [`TimeoutException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/concurrent/TimeoutException.md)- _Class TimeoutException_

#### `php\format`

- [`IniProcessor`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/IniProcessor.md)- _Class IniProcessor_
- [`Processor`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/Processor.md)- _Class Processor_
- [`ProcessorException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/format/ProcessorException.md)- _Class ProcessorException_

#### `php\io`

- [`File`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/File.md)- _Class File_
- [`FileStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/FileStream.md)- _Class FileStream_
- [`IOException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/IOException.md)- _Class IOException_
- [`MemoryStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MemoryStream.md)- _Class MemoryStream_
- [`MiscStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/MiscStream.md)- _Class MiscStream_
- [`ResourceStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/ResourceStream.md)- _Class ResourceStream_
- [`Stream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/io/Stream.md)- _Class Stream_

#### `php\lang`

- [`ClassLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ClassLoader.md)- _Class ClassLoader_
- [`Environment`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Environment.md)- _Class Environment_
- [`IllegalArgumentException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalArgumentException.md)- _Class IllegalArgumentException_
- [`IllegalStateException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/IllegalStateException.md)- _Class IllegalStateException_
- [`InterruptedException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/InterruptedException.md)- _Class InterruptedException_
- [`Invoker`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Invoker.md)- _Class for calling methods/functions/etc._
- [`JavaClass`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaClass.md)
- [`JavaException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaException.md)- _Class JavaException_
- [`JavaField`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaField.md)
- [`JavaMethod`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaMethod.md)
- [`JavaObject`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaObject.md)- _Class JavaObject_
- [`JavaReflection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/JavaReflection.md)
- [`Module`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Module.md)- _Class Module_
- [`NotImplementedException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NotImplementedException.md)- _Class NotImplementedException_
- [`NumberFormatException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/NumberFormatException.md)- _Class NumberFormatException_
- [`Package`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Package.md)- _Class Package_
- [`PackageLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/PackageLoader.md)
- [`Process`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Process.md)- _Class Process_
- [`SourceMap`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/SourceMap.md)- _Class SourceMap_
- [`System`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/System.md)- _Class System_
- [`Thread`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/Thread.md)- _Class Thread_
- [`ThreadGroup`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadGroup.md)- _Class ThreadGroup_
- [`ThreadLocal`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadLocal.md)- _Class ThreadLocal_
- [`ThreadPool`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lang/ThreadPool.md)- _Class ThreadPool_

#### `php\lib`

- [`arr`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/arr.md)- _Library for working with collections - arrays, iterators, etc._
- [`bin`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/bin.md)- _Class for working with binary strings_
- [`binary`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/binary.md)- _Class for working with binary strings_
- [`char`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/char.md)- _Char Utils for working with unicode chars_
- [`fs`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/fs.md)- _File System class._
- [`items`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/items.md)- _Class items_
- [`Mirror`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/Mirror.md)- _Reflection Lib._
- [`num`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/num.md)- _Utils for numbers_
- [`number`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/number.md)- _Utils for numbers_
- [`reflect`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/reflect.md)- _Class reflect_
- [`rx`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/rx.md)- _Class rx_
- [`str`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/lib/str.md)- _Class str_

#### `php\net`

- [`NetAddress`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/NetAddress.md)- _Class NetAddress_
- [`NetStream`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/NetStream.md)- _http, ftp protocols_
- [`Proxy`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/Proxy.md)- _Class Proxy_
- [`ServerSocket`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/ServerSocket.md)- _Class SocketServer_
- [`Socket`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/Socket.md)- _Class Socket_
- [`SocketException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/SocketException.md)- _Class SocketException_
- [`URL`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URL.md)- _Class URL_
- [`URLConnection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/net/URLConnection.md)- _Class URLConnection_

#### `php\time`

- [`Time`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/Time.md)- _Class Time, Immutable_
- [`TimeFormat`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/TimeFormat.md)- _Class TimeFormat, Immutable_
- [`Timer`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/Timer.md)- _Class Timer_
- [`TimeZone`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/time/TimeZone.md)- _Class TimeZone, Immutable_

#### `php\util`

- [`Configuration`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Configuration.md)- _Class Configuration_
- [`Flow`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Flow.md)- _A special class to work with arrays and iterators under flows._
- [`LauncherClassLoader`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/LauncherClassLoader.md)- _Class LauncherClassLoader_
- [`Locale`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Locale.md)- _Class Locale, Immutable_
- [`Regex`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Regex.md)- _http://www.regular-expressions.info/java.html_
- [`RegexException`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/RegexException.md)- _Class RegexException_
- [`Scanner`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Scanner.md)- _A simple text scanner which can parse primitive types and strings using_
- [`Shared`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/Shared.md)- _Class to work with shared memory of Environments_
- [`SharedCollection`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedCollection.md)- _Class SharedCollection_
- [`SharedMap`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedMap.md)- _Class SharedMap_
- [`SharedMemory`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedMemory.md)- _Class SharedMemory_
- [`SharedQueue`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedQueue.md)- _Class SharedQueue_
- [`SharedStack`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedStack.md)- _Class SharedStack_
- [`SharedValue`](https://github.com/jphp-compiler/jphp/blob/master/jphp-runtime/api-docs/classes/php/util/SharedValue.md)- _Class SharedValue_