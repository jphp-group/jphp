<?php
namespace packager;

/**
 * Class Event
 * @package packager
 */
class Event
{
    /**
     * @var Packager
     */
    private $packager;

    /**
     * @var Package
     */
    private $package;

    /**
     * @var array
     */
    private $args;

    /**
     * @var array
     */
    private $flags = [];

    /**
     * Event constructor.
     * @param Packager $packager
     * @param Package $package
     * @param array $args
     * @param array $flags
     */
    public function __construct(Packager $packager, ?Package $package, array $args, array $flags = [])
    {
        $this->packager = $packager;
        $this->package = $package;
        $this->args = $args;
        $this->flags = $flags;
    }

    /**
     * @return Packager
     */
    public function packager(): Packager
    {
        return $this->packager;
    }

    /**
     * @return Package
     */
    public function package(): ?Package
    {
        return $this->package;
    }

    /**
     * @return array
     */
    public function args(): array
    {
        return $this->args;
    }

    /**
     * @param int $num
     * @return mixed
     */
    public function arg(int $num)
    {
        return $this->args()[$num];
    }

    /**
     * @return array
     */
    public function flags(): array
    {
        return flow($this->flags)->keys()->toArray();
    }

    /**
     * @param mixed ...$names
     * @return bool
     */
    function isFlagExists(...$names): bool
    {
        foreach ($names as $name) {
            if (isset($this->flags[$name])) return true;
        }

        return false;
    }

    /**
     * @param array ...$names
     * @return bool
     */
    function isFlag(...$names): bool
    {
        foreach ($names as $name) {
            if ($this->flags[$name]) return true;
        }

        return false;
    }
}