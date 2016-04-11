<?php
namespace php\net;
use php\io\Stream;

/**
 * Class HttpFormDataBody
 * @package php\net
 */
class HttpMultipartFormBody extends HttpBody
{
    /**
     * @param array $fields (optional)
     * @param string $encoding
     */
    public function __construct(array $fields, $encoding = 'UTF-8')
    {
    }

    /**
     * @return array
     */
    public function getFields()
    {
    }

    /**
     * @param array $data
     */
    public function setFields(array $data)
    {
    }

    /**
     * @return array of files or streams (Stream).
     */
    public function getFiles()
    {
    }

    /**
     * @param array $files
     */
    public function setFiles(array $files)
    {
    }

    /**
     * @param string $name
     * @param string|Stream $file
     */
    public function addFile($name, $file)
    {
    }

    /**
     * @return string
     */
    public function getEncoding()
    {
    }

    /**
     * @param string $encoding
     */
    public function setEncoding($encoding)
    {
    }

    /**
     * @param URLConnection $connection
     * @return void
     */
    public function apply(URLConnection $connection)
    {
    }
}