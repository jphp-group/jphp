<?php
namespace php\lang;

/**
 * Class ThreadGroup
 * @packages std, core
 */
class ThreadGroup
{


    /**
     * @param $name
     * @param ThreadGroup $parent
     */
    public function __construct($name, ThreadGroup $parent = null) { }

    /**
     * @return string
     */
    public function getName() { }

    /**
     * @return ThreadGroup|null
     */
    public function getParent() { }

    /**
     * @return int
     */
    public function getActiveCount() { }

    /**
     * @return int
     */
    public function getActiveGroupCount() { }

    /**
     * @return bool
     */
    public function isDaemon() { }

    /**
     * @param bool $value
     */
    public function setDaemon($value) { }

    /**
     * @return bool
     */
    public function isDestroyed() { }

    /**
     * @return int
     */
    public function getMaxPriority() { }

    /**
     * @param int $value
     */
    public function setMaxPriority($value) { }

    /**
     * Destroys this thread group and all of its subgroups.
     */
    public function destroy() { }

    /**
     * Determines if the currently running thread has permission to
     * modify this thread group.
     * @throws JavaException
     */
    public function checkAccess() { }

    /**
     * Interrupts all threads in this thread group.
     */
    public function interrupt() { }
}