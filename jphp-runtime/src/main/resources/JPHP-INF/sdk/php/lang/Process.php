<?php
namespace php\lang;
use php\io\File;
use php\io\MiscStream;
use php\io\Stream;

/**
 * Class Process
 * @packages std, core
 */
class Process
{


    /**
     * @param array $commands
     * @param null|string|File $directory
     * @param array $environment
     */
    public function __construct(array $commands, $directory = null, array $environment = null) { }

    /**
     * @return Process
     * @throws IllegalStateException
     */
    public function start() { return new Process([]); }

    /**
     * Causes the current thread to wait, if necessary, until the
     * process represented by this `Process` object has
     * terminated.  This method returns immediately if the subprocess
     * has already terminated.  If the subprocess has not yet
     * terminated, the calling thread will be blocked until the
     * subprocess exits.
     *
     * @return Process
     * @throws IllegalStateException
     */
    public function startAndWait(): Process { }

    /**
     * Returns the exit value for the subprocess.
     *
     * @return int|null - null if process is working
     * @throws IllegalStateException
     */
    public function getExitValue() { return 0; }

    /**
     * Kills the subprocess. The subprocess represented by this
     * `Process` object is forcibly terminated.
     *
     * @param bool $force
     * @throws IllegalStateException
     */
    public function destroy($force = false) { }

    /**
     * Returns the input stream connected to the normal output of the
     * subprocess.  The stream obtains data piped from the standard
     * output of the process represented by this `Process` object.
     *
     * @return Stream
     * @throws IllegalStateException
     */
    public function getInput() { return Stream::of(''); }

    /**
     * Returns the output stream connected to the normal input of the
     * subprocess.  Output to the stream is piped into the standard
     * input of the process represented by this `Process` object.
     *
     * @return Stream
     * @throws IllegalStateException
     */
    public function getOutput() { return Stream::of(''); }

    /**
     * Returns the input stream connected to the error output of the
     * subprocess.  The stream obtains data piped from the error output
     * of the process represented by this `Process` object.
     *
     * @return Stream
     * @throws IllegalStateException
     */
    public function getError() { return Stream::of(''); }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function inheritIO(): Process
    {
    }

    /**
     * @param $file
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectOutputToFile($file): Process
    {
    }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectOutputToInherit(): Process
    {
    }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectOutputToPipe(): Process
    {
    }

    /**
     * @param $file
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectErrorToFile($file): Process
    {
    }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectErrorToInherit(): Process
    {
    }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectErrorToPipe(): Process
    {
    }

    /**
     * @param $file
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectInputFromFile($file): Process
    {
    }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectInputFromInherit(): Process
    {
    }

    /**
     * @return Process
     * @throws IllegalArgumentException
     */
    public function redirectInputFromPipe(): Process
    {
    }


    /**
     * Tests whether the subprocess represented by this {@code Process} is
     * alive.
     *
     * @return bool
     */
    public function isAlive(): bool
    {
    }

    /**
     * Causes the current thread to wait, if necessary, until the
     * process represented by this {@code Process} object has
     * terminated.
     *
     * @return int
     */
    public function waitFor(): int { }
}
