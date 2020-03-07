<?php
namespace php\io;

/**
 * Class DataStream
 * @package php\io
 */
class DataStream
{
    /**
     * DataStream constructor.
     * @param Stream $stream
     */
    public function __construct(Stream $stream)
    {
    }

    /**
     * @return int
     */
    public function read(): int
    {
    }

    /**
     * @param int $value
     */
    public function write(int $value)
    {
    }

    public function readBool(): bool
    {
    }

    public function readByte(): int
    {
    }

    public function readUnsignedByte(): int
    {
    }

    public function readShort(): int
    {
    }

    public function readUnsignedShort(): int
    {
    }

    public function readInt(): int
    {
    }

    public function readLong(): int
    {
    }

    public function readFloat(): float
    {
    }

    public function readDouble(): float
    {
    }

    public function readUTF(): string
    {
    }

    public function readChar(): string
    {
    }

    public function writeByte(int $value)
    {
    }

    public function writeShort(int $value)
    {
    }

    public function writeInt(int $value)
    {
    }

    public function writeLong(int $value)
    {
    }

    public function writeFloat(float $value)
    {
    }

    public function writeDouble(float $value)
    {
    }

    public function writeChar(string $value)
    {
    }

    public function writeChars(string $value)
    {
    }

    public function writeUTF(string $value)
    {
    }

    public function writeBinary(string $value)
    {
    }

    public function writeBool(bool $value)
    {
    }
}