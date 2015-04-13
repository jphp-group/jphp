<?php
namespace php\gdx\audio;

/**
 * Class Music
 * @package php\gdx\audio
 */
class Music {
    private function __construct() {}

    /**
     * Starts the play back of the music stream. In case the stream was paused this will resume the play back. In case the music
     * stream is finished playing this will restart the play back.
     */
    public function play() { }

    /**
     * Pauses the play back. If the music stream has not been started yet or has finished playing a call to this method will be
     * ignored.
     */
    public function pause() { }

    /**
     * Stops a playing or paused Music instance. Next time play() is invoked the Music will start from the beginning.
     */
    public function stop() { }

    /**
     * @return bool whether this music stream is playing
     */
    public function isPlaying() { }

    /**
     * @param bool $isLooping
     */
    public function setLooping($isLooping) { }

    /**
     * @return bool
     */
    public function isLooping() { }

    /**
     * @param double $volume
     */
    public function setVolume($volume) { }

    /**
     * @return double
     */
    public function getVolume() { }

    /**
     * Sets the panning and volume of this music stream.
     * @param double $pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * @param double $volume
     */
    public function setPan($pan, $volume) { }

    /**
     * Returns the playback position in milliseconds.
     * @return double
     */
    public function getPosition() { }

    /**
     * Releases all the resources.
     */
    public function dispose() { }

    /**
     * Register a callback to be invoked when the end of a music stream has been reached during playback.
     * @param callable $listener (Music $music)
     */
    public function setOnCompletionListener($listener) { }
}