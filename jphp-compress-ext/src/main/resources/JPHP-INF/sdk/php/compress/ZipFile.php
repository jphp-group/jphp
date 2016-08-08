<?php
namespace php\compress;
use php\io\IOException;
use php\io\Stream;

/**
 * Class ZipFile
 * @package php\compress
 */
class ZipFile
{
    /**
     * ZipFile constructor.
     * @param string $filename
     * @param string $charset
     */
    public function __construct($filename, $charset = 'UTF-8')
    {
    }

    /**
     * @return string
     */
    public function getName()
    {
    }

    /**
     * @return string
     */
    public function getComment()
    {
    }

    /**
     * @param string $name
     * @return ZipEntry
     */
    public function getEntry($name)
    {
    }

    /**
     * @param string $name
     * @return string
     */
    public function getEntryContent($name)
    {
    }

    /**
     * @param string $name
     * @return Stream
     */
    public function getEntryStream($name)
    {
    }

    /**
     * @return string[]
     */
    public function getEntryNames()
    {
    }
}