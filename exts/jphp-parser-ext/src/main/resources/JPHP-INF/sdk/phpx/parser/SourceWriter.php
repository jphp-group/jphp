<?php
namespace phpx\parser;
use php\io\Stream;

/**
 * Class SourceWriter
 * @package phpx\parser
 */
abstract class SourceWriter
{
    /**
     * @param SourceManager $manager
     * @param SourceFile $file
     */
    public function write(SourceManager $manager, SourceFile $file) {}

    /**
     * @param SourceManager $manager
     * @param SourceFile $file
     * @param ModuleRecord $record
     * @param Stream $out
     *
     * @return mixed
     */
    abstract public function writeModule(SourceManager $manager, SourceFile $file, ModuleRecord $record, Stream $out);
}