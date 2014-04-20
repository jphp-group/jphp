.. php:class:: php\lang\Environment
	..php:method:: __construct($parent = NULL, $flags = 0)

		

		:param php\lang\Environment $parent: 
		:param $flags: 
	..php:method:: execute($runnable)

		

		:param callable $runnable: 
		:returns: mixed d
	..php:method:: importClass($className)

		

		:param $className: 
	..php:method:: exportClass($className)

		

		:param $className: 
	..php:method:: importFunction($functionName)

		

		:param $functionName: 
	..php:method:: exportFunction($functionName)

		

		:param $functionName: 
	..php:method:: defineConstant($name, $value, $caseSensitive = true)

		

		:param $name: 
		:param $value: 
		:param $caseSensitive: 
	..php:method:: onMessage($callback)

		

		:param callable $callback: 
	..php:method:: sendMessage($message)

		

		:param $message: 
		:returns: mixed d
	..php:method:: current()

		Get environment of current execution

		:returns: Environment t
