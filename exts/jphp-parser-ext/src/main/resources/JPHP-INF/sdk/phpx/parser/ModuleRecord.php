<?php
namespace phpx\parser;

/**
 * Class ModuleRecord
 * @package phpx\parser
 */
class ModuleRecord extends AbstractSourceRecord
{
    /**
     * @return NamespaceRecord[]
     */
    public function getNamespaces(): array
    {
    }

    /**
     * @return ClassRecord[]
     */
    public function getClasses(): array
    {
    }

    /**
     * @return MethodRecord[]
     */
    public function getFunctions(): array
    {
    }

    /**
     * @param string $name
     * @return null|ClassRecord
     */
    public function findClass(string $name): ?ClassRecord
    {
    }

    /**
     * Alias of findFunction().
     * @param string $name
     * @return null|MethodRecord
     */
    public function findMethod(string $name): ?MethodRecord
    {
    }

    /**
     * @param string $name
     * @return null|MethodRecord
     */
    public function findFunction(string $name): ?MethodRecord
    {
    }
}