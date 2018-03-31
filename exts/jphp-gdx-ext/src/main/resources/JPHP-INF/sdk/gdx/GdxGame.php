<?php
namespace gdx;

/**
 * Class GdxGame
 * @package gdx
 */
class GdxGame
{
    /**
     * @param string $event
     * @param callable|null $handler
     * @return GdxGame
     */
    public function on(string $event, ?callable $handler): GdxGame
    {
    }

    /**
     * @param string $event
     * @return GdxGame
     */
    public function off(string $event): GdxGame
    {
    }

    /**
     * @param string $event
     * @param array ...$args
     * @return GdxGame
     */
    public function trigger(string $event, ...$args): GdxGame
    {
    }

    /**
     * @param callable $runnable
     * @return GdxGame
     */
    public function later(callable $runnable): GdxGame
    {
    }

    /**
     * @return GdxGame
     */
    public function exit(): GdxGame
    {
    }
}