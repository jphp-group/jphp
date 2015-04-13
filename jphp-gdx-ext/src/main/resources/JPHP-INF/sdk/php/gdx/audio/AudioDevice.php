<?php
namespace php\gdx\audio;

/**
 * Class AudioDevice
 * @package php\gdx\audio
 */
class AudioDevice {

    private function __construct() {}

    /**
     * @return bool
     */
    public function isMono() { }

    /**
     * Writes the array of 16-bit signed PCM samples to the audio device and blocks until they have been processed.
     *
     * @param int[] $samples
     * @param int $offset
     * @param int $numSamples
     */
    public function writeSamples(array $samples, $offset, $numSamples) { }

    /**
     * Writes the array of float PCM samples to the audio device and blocks until they have been processed.
     *
     * @param double[] $samples
     * @param int $offset
     * @param int $numSamples
     */
    public function writeFloatSamples(array $samples, $offset, $numSamples) { }

    /**
     * @return int
     */
    public function getLatency() { }

    /**
     * Frees all resources associated with this AudioDevice. Needs to be called when the device is no longer needed.
     */
    public function dispose() { }

    /**
     * Sets the volume in the range [0,1].
     * @param double $volume
     */
    public function setVolume($volume) { }
} 