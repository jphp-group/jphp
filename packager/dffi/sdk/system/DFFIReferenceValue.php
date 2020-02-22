<?php
namespace system;


/**
 * Class DFFIReferenceValue
 * @package system
 */
class DFFIReferenceValue
{
	/**
	 * DFFIReferenceValue constructor.
     * @param string $type
     */
    public function __construct($type)
    {
    }
	
	/**
	 * DFFIReferenceValue constructor.
     * @param string $type
     * @param string $value
     */
	public function __construct($type, $value)
    {
    }
    
    /**
	 * Конструктор с выделением памяти
     * @param string $type
     * @param int $size
     */
	public function __construct($type, $size)
    {
    }

    /**
	 * @param string $value
     * @return void
     */
    public function setValue($value)
    {
    }
	
	/**
     * @return any
     */
    public function getValue()
    {
    }
    
    /**
     * @return string
     */
    public function getNativeString()
    {
    }
}