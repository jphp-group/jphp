.. php:class:: php\net\ServerSocket
	..php:method:: __construct($port = null, $backLog = 50)

		

		:param $port: 
		:param $backLog: 
	..php:method:: accept()

		

		:returns: Socket t
	..php:method:: bind($hostname, $port, $backLog = 50)

		

		:param $hostname: 
		:param $port: 
		:param $backLog: 
	..php:method:: close()

		

	..php:method:: isClosed()

		

		:returns: bool l
	..php:method:: isBound()

		Returns the binding state of the ServerSocket.

		:returns: bool l
	..php:method:: setSoTimeout($timeout)

		Enable/disable SO_TIMEOUT with the specified timeout, in
milliseconds.

		:param $timeout: 
	..php:method:: setReuseAddress($on)

		Enable/disable the SO_REUSEADDR socket option.

		:param $on: 
	..php:method:: setReceiveBufferSize($size)

		

		:param $size: 
	..php:method:: setPerformancePreferences($connectTime, $latency, $bandWidth)

		Sets performance preferences for this ServerSocket.
! Not implemented yet for TCP/IP

		:param $connectTime: 
		:param $latency: 
		:param $bandWidth: 
