<?php
namespace php\gdx;

use php\gdx\audio\AudioDevice;
use php\gdx\audio\AudioRecorder;
use php\gdx\audio\Music;
use php\gdx\audio\Sound;
use php\gdx\files\FileHandle;

/**
 * Class Audio
 * @package php\gdx
 */
class Audio {
    private function __construct() { }

    /**
     * Creates a new AudioDevice either in mono or stereo mode. The AudioDevice has to be disposed via its
     * AudioDevice->dispose() method when it is no longer used.
     *
     * @param int $samplingRate
     * @param boolean $isMono
     * @return AudioDevice
     *
     * @throws GdxRuntimeException in case the device could not be created
     */
    public function newAudioDevice($samplingRate, $isMono) { }

    /**
     * Creates a new AudioRecorder. The AudioRecorder has to be disposed after it is no longer used.
     *
     * @param int $samplingRate
     * @param bool $isMono
     * @return AudioRecorder
     *
     * @throws GdxRuntimeException in case the recorder could not be created
     */
    public function newAudioRecorder($samplingRate, $isMono) { }

    /**
     * Creates a new Sound which is used to play back audio effects such as gun shots or explosions. The Sound's audio data
     * is retrieved from the file specified via the FileHandle. Note that the complete audio data is loaded into RAM. You
     * should therefore not load big audio files with this methods. The current upper limit for decoded audio is 1 MB.
     *
     * Currently supported formats are WAV, MP3 and OGG.
     *
     * The Sound has to be disposed if it is no longer used via the {@link Sound#dispose()} method.
     *
     * @param FileHandle $fileHandle
     * @return Sound
     *
     * @throws GdxRuntimeException in case the sound could not be loaded
     */
    public function newSound(FileHandle $fileHandle) { }

    /**
     * Creates a new Music instance which is used to play back a music stream from a file. Currently supported formats are
     * WAV, MP3 and OGG. The Music instance has to be disposed if it is no longer used via the Music->dispose() method.
     * Music instances are automatically paused when ApplicationListener->pause() is called and resumed when
     * ApplicationListener->resume() is called.
     *
     * @param FileHandle $fileHandle
     * @return Music
     *
     * @throws GdxRuntimeException in case the music could not be loaded
     */
    public function newMusic(FileHandle $fileHandle) { }
} 