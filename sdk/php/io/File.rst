.. php:class:: php\io\File
	..php:method:: __construct($path, $child = NULL)

		

		:param $path: 
		:param $child: 
	..php:method:: exists()

		

		:returns: bool l
	..php:method:: canExecute()

		

		:returns: bool l
	..php:method:: canWrite()

		

		:returns: bool l
	..php:method:: canRead()

		

		:returns: bool l
	..php:method:: getName()

		

		:returns: string g
	..php:method:: getAbsolutePath()

		

		:returns: string g
	..php:method:: getCanonicalPath()

		

		:returns: string g
	..php:method:: getParent()

		

		:returns: string g
	..php:method:: getPath()

		

		:returns: string g
	..php:method:: getAbsoluteFile()

		

		:returns: File e
	..php:method:: getCanonicalFile()

		

		:returns: File e
	..php:method:: getParentFile()

		

		:returns: File e
	..php:method:: mkdir()

		

		:returns: bool l
	..php:method:: mkdirs()

		

		:returns: bool l
	..php:method:: isFile()

		

		:returns: bool l
	..php:method:: isDirectory()

		

		:returns: bool l
	..php:method:: isAbsolute()

		

		:returns: bool l
	..php:method:: isHidden()

		

		:returns: bool l
	..php:method:: delete()

		

		:returns: bool l
	..php:method:: deleteOnExit()

		

		:returns: void d
	..php:method:: createNewFile()

		

		:returns: bool l
	..php:method:: lastModified()

		

		:returns: int t
	..php:method:: length()

		

		:returns: int t
	..php:method:: renameTo($newName)

		

		:param $newName: 
		:returns: bool l
	..php:method:: setExecutable($value, $ownerOnly = true)

		

		:param $value: 
		:param $ownerOnly: 
		:returns: bool l
	..php:method:: setWritable($value, $ownerOnly = true)

		

		:param $value: 
		:param $ownerOnly: 
		:returns: bool l
	..php:method:: setReadable($value, $ownerOnly = true)

		

		:param $value: 
		:param $ownerOnly: 
		:returns: bool l
	..php:method:: setReadOnly()

		

		:returns: bool l
	..php:method:: setLastModified($time)

		

		:param $time: 
		:returns: bool l
	..php:method:: compareTo($file)

		

		:param $file: 
		:returns: int t
	..php:method:: find($filter = null)

		

		:param callable $filter: 
		:returns: string[] ]
	..php:method:: findFiles($filter = null)

		

		:param callable $filter: 
		:returns: File[] ]
	..php:method:: createTemp($prefix, $suffix, $directory = null)

		

		:param $prefix: 
		:param $suffix: 
		:param $directory: 
		:returns: File e
	..php:method:: listRoots()

		List the available filesystem roots.

		:returns: File[]
An array of {@code File} objects denoting the available
filesystem roots, or empty array if the set of roots could not
be determined.  The array will be empty if there are no
filesystem roots.
