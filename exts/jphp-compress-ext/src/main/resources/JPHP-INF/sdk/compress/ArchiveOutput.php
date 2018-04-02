<?php
namespace compress;

use php\io\Stream;

/**
 * Class ArchiveOutputStream
 * @package compress
 * @packages compress
 */
abstract class ArchiveOutput
{
    /**
     * @return Stream
     */
    public function stream(): Stream
    {
    }

    /**
     * Writes the headers for an archive entry to the output stream.
     * The caller must then write the content to the stream and call
     * closeArchiveEntry() to complete the process.
     *
     * @param ArchiveEntry $entry
     */
    public function putEntry(ArchiveEntry $entry)
    {
    }

    /**
     * Closes the archive entry, writing any trailer information that may
     * be required.
     */
    public function closeEntry(): void
    {
    }

    /**
     * Whether this stream is able to write the given entry.
     *
     * Some archive formats support variants or details that are
     * not supported (yet).
     *
     * @param ArchiveEntry $entry
     * @return bool
     */
    public function canWriteEntryData(ArchiveEntry $entry): bool
    {
    }

    /**
     * Create an archive entry using the inputFile and entryName provided.
     *
     * @param $file
     * @param string $entryName
     * @return ArchiveEntry
     */
    abstract public function createEntry($file, string $entryName);

    /**
     * Finishes the addition of entries to this stream, without closing it.
     * Additional data can be written, if the format supports it.
     */
    public function finish()
    {
    }

    /**
     * Returns the current number of bytes written to this stream.
     * @return int
     */
    public function getBytesWritten(): int
    {
    }
}