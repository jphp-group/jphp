<?php
namespace php\gdx\audio;

/**
 * Class Sound
 * @package php\gdx\audio
 */
class Sound {
    private function __construct() {}

    /**
     * Plays the sound. If the sound is already playing, it will be played again, concurrently.
     *
     * @param double $volume (optional)
     * @param double $pitch (optional)
     * @param double $pan (optional)
     * @return int the id of the sound instance if successful, or -1 on failure.
     */
    public function play($volume, $pitch, $pan) { }

    /**
     * Plays the sound, looping. If the sound is already playing, it will be played again, concurrently.
     * You need to stop the sound via a call to stop(long) using the returned id.
     *
     * @param double $volume (optional)
     * @param double $pitch (optional)
     * @param double $pan (optional)
     * @return int the id of the sound instance if successful, or -1 on failure.
     */
    public function loop($volume, $pitch, $pan) { }

    /**
     * Stops playing all or $soundId instance(s) of this sound.
     * @param int $soundId (optional)
     */
    public function stop($soundId) { }

    /**
     * Pauses the sound instance with the given id as returned by play() or all sounds. If the sound is no
     * longer playing, this has no effect.
     *
     * @param int $soundId (optional)
     */
    public function pause($soundId) { }

    /**
     * @param int $soundId (optional)
     */
    public function resume($soundId) { }

    /**
     * Sets the sound instance with the given id to be looping. If the sound is no longer playing this has no effect
     * @param int $soundId
     * @param bool $looping
     */
    public function setLooping($soundId, $looping) { }

    /**
     * Changes the pitch multiplier of the sound instance with the given id as returned by play().
     * If the sound is no longer playing, this has no effect.
     * @param int $soundId
     * @param float $pitch
     */
    public function setPitch($soundId, $pitch) { }

    /**
     * Sets the panning and volume of the sound instance with the given id as returned by play().
     * If the sound is no longer playing, this has no effect.
     * @param int $soundId
     * @param float $pan
     * @param float $volume
     */
    public function setPan($soundId, $pan, $volume) { }

    /**
     * Sets the priority of a sound currently being played back. Higher priority sounds will be considered last if the maximum
     * number of concurrently playing sounds is exceeded. This is only a hint and might not be honored by a backend implementation.
     * @param int $soundId
     * @param int $priority the priority (0 == lowest)
     */
    public function setPriority($soundId, $priority) { }

    /**
     * Releases all the resources.
     */
    public function dispose() { }
} 