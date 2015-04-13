<?php
namespace php\gdx\assets;

use php\gdx\audio\Music;
use php\gdx\audio\Sound;
use php\gdx\graphics\Pixmap;
use php\gdx\graphics\Texture;

/**
 * Class AssetManager
 */
class AssetManager {
    /**
     * @param callable $resolver (optional) - function($fileName): FileHandle
     */
    public function __construct(callable $resolver) { }

    /**
     * @param string $fileName
     * @return Texture|Pixmap|Music|Sound
     */
    public function get($fileName) { }

    /**
     * @param string $fileName
     */
    public function loadTexture($fileName) { }

    /**
     * @param string $fileName
     */
    public function loadPixmap($fileName) { }

    /**
     * @param string $fileName
     */
    public function loadMusic($fileName) { }

    /**
     * @param string $fileName
     */
    public function loadSound($fileName) { }

    /**
     * @param string $fileName
     */
    public function unload($fileName) { }

    /**
     * @param string $fileName
     */
    public function isLoaded($fileName) { }

    /**
     * @param object|mixed $asset
     */
    public function containsAsset($asset) { }

    /**
     * @param object|mixed $asset
     */
    public function getAssetFileName($asset) { }

    /**
     * @param string $fileName
     */
    public function disposeDependencies($fileName) { }

    /**
     * @param int $millis (optional)
     */
    public function update($millis) { }

    /**
     *
     */
    public function finishLoading() { }

    /**
     * @return int
     */
    public function getLoadedAssets() { }

    /**
     * @return int
     */
    public function getQueuedAssets() { }

    /**
     * @return float
     */
    public function getProgress() { }

    /**
     *
     */
    public function dispose() { }

    /**
     *
     */
    public function clear() { }

    /**
     * @param string $fileName
     * @return int
     */
    public function getReferenceCount($fileName) { }

    /**
     * @param string $fileName
     * @param int $refCount
     */
    public function setReferenceCount($fileName, $refCount) { }

    /**
     * @return string
     */
    public function getDiagnostics() { }
} 