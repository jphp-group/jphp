<?php
namespace php\gdx\audio;

/**
 * Class AudioRecorder
 * @package php\gdx\audio
 */
class AudioRecorder {
    private function __construct() { }

    /**
     * Reads in numSamples samples into the array samples starting at offset. If the recorder is in stereo you have to multiply
     * numSamples by 2.
     *
     * @param int[] $samples
     * @param int $offset
     * @param int $numSamples
     */
    public function read(array $samples, $offset, $numSamples) { }

    /**
     * Disposes the AudioRecorder
     */
    public function dispose() { }
} 