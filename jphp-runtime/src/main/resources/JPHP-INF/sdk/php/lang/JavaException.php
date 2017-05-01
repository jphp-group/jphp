<?php
namespace php\lang;

/**
 * Class JavaException
 * @packages std, core
 */
class JavaException extends \Exception
{


    /**
     * Check exception instance of java.lang.RuntimeException
     * @return bool
     */
    public function isRuntimeException() { }

    /**
     * Check exception instance of java.lang.NullPointerException
     * @return bool
     */
    public function isNullPointerException() { }

    /**
     * Check exception instance of java.lang.IllegalArgumentException
     * @return bool
     */
    public function isIllegalArgumentException() { }

    /**
     * Check exception instance of java.lang.NumberFormatException
     * @return bool
     */
    public function isNumberFormatException() { }

    /**
     * @return JavaClass
     */
    public function getExceptionClass() { }

    /**
     * @return JavaObject
     */
    public function getJavaException() { }

    /**
     * Print jvm stack trace
     */
    public function printJVMStackTrace() { }

    public function getErrno()
    {
    }
}
