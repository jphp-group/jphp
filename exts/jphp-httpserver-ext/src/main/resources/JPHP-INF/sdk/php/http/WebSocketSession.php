<?php
namespace php\http;

use php\io\IOException;
use php\io\Stream;
use php\util\Locale;

/**
 * @package php\http
 * @packages http
 */
abstract class WebSocketSession
{
    /**
     * @return int
     */
    public function idleTimeout(): int
    {
    }

    /**
     * @return string
     */
    public function remoteAddress(): string
    {
    }

    /**
     * @param string $text
     * @param callable|null $callback
     * @throws IOException
     */
    public function sendText(string $text, ?callable $callback = null): void
    {
    }

    /**
     * @param string $text
     * @param bool $isLast
     * @throws IOException
     */
    public function sendPartialText(string $text, bool $isLast): void
    {
    }

    /**
     * @param int $status
     * @param string|null $reason
     */
    public function close(int $status = 1000, string $reason = null): void
    {
    }

    public function disconnect(): void
    {
    }

    /**
     * @return bool
     */
    public function isOpen(): bool
    {
    }

    /**
     * @return bool
     */
    public function isSecure(): bool
    {
    }

    /**
     * @return string
     */
    public function protocolVersion(): string
    {
    }

    /**
     * @return array
     */
    public function policy(): array
    {
    }

    /**
     * @param string $mode [optional]
     * @return string
     */
    public function batchMode(string $mode): string
    {
    }

    /**
     * @throws IOException
     */
    public function flush(): void
    {
    }
}