<?php
namespace system;


/**
 * Class DFFI
 * @package system
 */
class DFFI
{
	/**
	 * DFFI constructor.
     * @param string $lib
     */
    public function __construct($lib){ }
	
	/**
     * Bind function
     * 
     * @param string $functionName
     * @param string $returnType
     * @param array $types
     */
	public function bind($functionName, $returnType, $types) {}
    
    /**
     * Add library to search path
     * 
     * @param string $lib
     * @param string $path
     */
	public function addSearchPath($lib, $path) {}
    
    /**
     * Get JavaFX window handle
     * 
     * @param UXForm $form
     */
	public function getJFXHandle($form) {}
}