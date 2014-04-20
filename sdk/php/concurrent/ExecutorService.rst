.. php:class:: php\concurrent\ExecutorService
	..php:method:: __construct()

		null

	..php:method:: isScheduled()

		

		:returns: bool l
	..php:method:: isShutdown()

		

		:returns: bool l
	..php:method:: isTerminated()

		

		:returns: bool l
	..php:method:: execute($runnable, $env = null)

		

		:param callable $runnable: 
		:param php\lang\Environment $env: 
	..php:method:: submit($runnable, $env = null)

		

		:param callable $runnable: 
		:param php\lang\Environment $env: 
		:returns: Future e
	..php:method:: schedule($runnable, $delay, $env = null)

		

		:param callable $runnable: 
		:param $delay: 
		:param php\lang\Environment $env: 
		:returns: Future e
	..php:method:: shutdown()

		null

	..php:method:: shutdownNow()

		null

	..php:method:: awaitTermination($timeout)

		Blocks until all tasks have completed execution after a shutdown
request, or the timeout occurs, or the current thread is
interrupted, whichever happens first.

		:param $timeout: 
		:returns: bool l
	..php:method:: newFixedThreadPool($max)

		

		:param $max: 
		:returns: ExecutorService e
	..php:method:: newCachedThreadPool()

		

		:returns: ExecutorService e
	..php:method:: newSingleThreadExecutor()

		Creates an Executor that uses a single worker thread operating
off an unbounded queue.

		:returns: ExecutorService e
	..php:method:: newScheduledThreadPool($corePoolSize)

		Creates a thread pool that can schedule commands to run after a
given delay, or to execute periodically.

		:param $corePoolSize: 
		:returns: ExecutorService e
