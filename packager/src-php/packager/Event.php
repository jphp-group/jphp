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
     * Event constructor.
     * @param Packager $packager
     * @param Package $package
     * @param array $args
     */
    public function __construct(Packager $packager, Package $package, array $args)
    {
        $this->packager = $packager;
        $this->package = $package;
        $this->args = $args;
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
    public function package(): Package
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
}