<?php
namespace php\swing;

use php\concurrent\TimeoutException;

/**
 * Class SwingWorker
 * @package php\swing
 */
abstract class SwingWorker {

    /**
     * @return mixed
     */
    abstract protected function doInBackground();

    /**
     * @param int $timeout
     * @return mixed
     * @throws TimeoutException
     */
    public function get($timeout = -1) { return ''; }

    /**
     * @return int
     */
    public function getProgress() { return 0; }

    /**
     * @param int $val
     */
    protected function setProgress($val) { }

    /**
     * @param array $values
     */
    protected function publish(array $values) { }

    /**
     * @param array $values
     */
    protected function process(array $values) { }

    /**
     * @return bool
     */
    public function isDone() { return false; }

    /**
     * @return bool
     */
    public function isCanceled() { return false; }

    /**
     * @return string PENDING, STARTED, DONE
     */
    public function getState() { return ''; }

    /**
     * @param bool $mayInterruptIfRunning
     */
    public function cancel($mayInterruptIfRunning) { }

    public function run() { }

    /**
     * Schedules this SwingWorker for execution on a worker
     * thread. There are a number of worker threads available. In the
     * event all worker threads are busy handling other
     * SwingWorkers this SwingWorker is placed in a waiting
     * queue.
     */
    public function execute() { }
}
