.. php:class:: php\util\Flow
	..php:method:: __construct($collection)

		

		:param $collection: 
	..php:method:: withKeys()

		Enables to save keys for the next operation

		:returns: $this s
	..php:method:: append($collection)

		

		:param $collection: 
		:returns: Flow w
	..php:method:: find($filter = null)

		

		:param callable $filter: 
		:returns: Flow w
	..php:method:: findOne($filter = null)

		

		:param callable $filter: 
		:returns: mixed d
	..php:method:: group($callback)

		

		:param callable $callback: 
		:returns: Flow w
	..php:method:: each($callback)

		

		:param callable $callback: 
		:returns: int - iteration count
	..php:method:: eachSlice($sliceSize, $callback, $withKeys = false)

		

		:param $sliceSize: 
		:param callable $callback: 
		:param $withKeys: 
		:returns: int - slice iteration count
	..php:method:: map($callback)

		

		:param callable $callback: 
		:returns: Flow w
	..php:method:: skip($n)

		

		:param $n: 
		:returns: Flow w
	..php:method:: limit($count)

		

		:param $count: 
		:returns: Flow w
	..php:method:: reduce($callback)

		

		:param callable $callback: 
		:returns: int t
	..php:method:: toArray()

		

		:returns: array y
	..php:method:: toString($separator)

		

		:param $separator: 
		:returns: string g
	..php:method:: count()

		

		:returns: int t
	..php:method:: current()

		

		:returns: mixed d
	..php:method:: next()

		

		:returns: void d
	..php:method:: key()

		

		:returns: mixed d
	..php:method:: valid()

		

		:returns: bool l
	..php:method:: rewind()

		

		:returns: void d
	..php:method:: of($collection)

		

		:param $collection: 
		:returns: Flow w
	..php:method:: ofRange($from, $to, $step = 1)

		

		:param $from: 
		:param $to: 
		:param $step: 
		:returns: Flow w
	..php:method:: ofString($string, $chunkSize = 1)

		

		:param $string: 
		:param $chunkSize: 
		:returns: Flow w
