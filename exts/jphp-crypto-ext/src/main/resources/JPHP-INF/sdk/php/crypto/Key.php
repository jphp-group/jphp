<?php
namespace php\crypto;

/**
 * Class CryptoKey
 * @package php\crypto
 * @packages crypto
 */
class Key
{
    /**
     *  constructor.
     * @param string $key
     * @param string $algorithm
     */
    public function __construct(string $key, string $algorithm)
    {
    }

    /**
     * @return string
     */
    public function getAlgorithm(): string
    {
    }

    /**
     * @return string
     */
    public function getFormat(): string
    {
    }

    /**
     * @return string
     */
    public function getEncoded(): string
    {
    }
}