package ru.regenix.jphp.common;

final public class Messages {

    private Messages(){}

    public final static Item ERR_PARSE_UNEXPECTED_END_OF_FILE = new Item("Syntax error, unexpected end");
    public final static Item ERR_PARSE_UNEXPECTED_END_OF_STRING = new Item("Syntax error, unexpected end of string");
    public final static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = new Item("Syntax error, unexpected '%s', expecting '%s'");
    public final static Item ERR_PARSE_UNEXPECTED_X = new Item("Syntax error, unexpected '%s'");
    public final static Item ERR_PARSE_IDENTIFIER_X_ALREADY_USED = new Item("Identifier '%s' already used");

    public final static Item ERR_COMPILE_CANNOT_JUMP_TO_LEVEL = new Item("Cannot break/continue %s level(s)");
    public final static Item ERR_COMPILE_CANNOT_JUMP = new Item("Cannot break/continue");
    public final static Item ERR_COMPILE_EXPECTED_CONST_VALUE = new Item("Expecting const value for %s");
    public final static Item ERR_COMPILE_CANNOT_FETCH_OF_NON_ARRAY = new Item("Cannot fetch an item of non-array");

    public final static Item ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION = new Item("Call to undefined function %s()");
    public final static Item ERR_FATAL_CALL_TO_UNDEFINED_METHOD = new Item("Call to undefined method %s()");
    public final static Item ERR_FATAL_NON_STATIC_METHOD_CALLED_DYNAMICALLY = new Item("Non-static method %s() should not be called statically");

    public final static Item ERR_FATAL_CALL_TO_PRIVATE_METHOD = new Item("Call to private method %s() from context '%s'");
    public final static Item ERR_FATAL_CALL_TO_PROTECTED_METHOD = new Item("Call to protected method %s() from context '%s'");

    public final static Item ERR_FATAL_ACCESS_TO_PROTECTED_PROPERTY = new Item("Cannot access protected property %s::$%s");
    public final static Item ERR_FATAL_ACCESS_TO_PRIVATE_PROPERTY = new Item("Cannot access private property %s::$%s");
    public final static Item ERR_FATAL_ACCESS_TO_UNDECLARED_STATIC_PROPERTY = new Item("Access to undeclared static property: %s::$%s");

    public final static Item ERR_FATAL_STATIC_METHOD_CALLED_DYNAMICALLY = new Item("Static method %s() should not be called dynamically");
    public final static Item ERR_FATAL_PASS_INCORRECT_ARGUMENTS_TO_FUNCTION = new Item("Pass incorrect number of arguments to function %s()");
    public final static Item ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT = new Item("Cannot get '%s' property of non-object");
    public final static Item ERR_FATAL_CANNOT_CALL_OF_NON_OBJECT = new Item("Cannot call '%s' method of non-object");
    public final static Item ERR_FATAL_CANNOT_SET_PROPERTY_OF_NON_OBJECT = new Item("Cannot set '%s' property of non-object");
    public final static Item ERR_FATAL_CANNOT_GET_OBJECT_PROPERTY_OF_CLASS = new Item("Cannot get '%s' object property of '%s' class");
    public final static Item ERR_FATAL_CANNOT_REDECLARE_CLASS = new Item("Cannot redeclare class %s");
    public final static Item ERR_FATAL_CANNOT_REDECLARE_FUNCTION = new Item("Cannot redeclare function %s");
    public final static Item ERR_FATAL_CANNOT_REDECLARE_CONSTANT = new Item("Cannot redeclare constant %s");
    public final static Item ERR_FATAL_CANNOT_IMPLEMENT = new Item("%s cannot implement %s - it is not an interface");
    public final static Item ERR_FATAL_INTERFACE_NOT_FOUND = new Item("Interface '%s' not found");
    public final static Item ERR_FATAL_TRAIT_NOT_FOUND = new Item("Trait '%s' not found");

    public final static Item ERR_FATAL_IMPLEMENT_METHOD =
            new Item("Class %s contains %s abstract method(s) and therefore be declared abstract or implement the remaining methods (%s)");

    public final static Item ERR_FATAL_INVALID_METHOD_SIGNATURE = new Item("Declaration of %s must be compatible with %s");
    public final static Item ERR_FATAL_INTERFACE_FUNCTION_CANNOT_CONTAIN_BODY = new Item("Interface function %s cannot contain body");
    public final static Item ERR_FATAL_ACCESS_TYPE_FOR_INTERFACE_METHOD = new Item("Access type for interface method %s must be omitted");
    public final static Item ERR_FATAL_CANNOT_MAKE_NON_STATIC_TO_STATIC = new Item("Cannot make non static method %s static in class %s");
    public final static Item ERR_FATAL_CANNOT_MAKE_STATIC_TO_NON_STATIC = new Item("Cannot make static method %s non static in class %s");

    public final static Item ERR_FATAL_CLASS_NOT_FOUND = new Item("Class '%s' not found");
    public final static Item ERR_WARNING_MISSING_ARGUMENT = new Item("Missing argument %s for %s()");
    public final static Item ERR_WARNING_EXPECT_LEAST_PARAMS = new Item("%s() expects at least %s parameters, %s given");
    public final static Item ERR_FATAL_REQUIRE_FAILED = new Item("%s(): Failed opening required '%s'");
    public final static Item ERR_WARNING_INCLUDE_FAILED = new Item("%s(): Failed opening '%s' for inclusion");
    public final static Item ERR_NOTICE_USE_UNDEFINED_CONSTANT = new Item("Use of undefined constant %s - assumed '%s'");
    public final static Item ERR_NOTICE_RETURN_NOT_REFERENCE = new Item("Only variable references should be returned by reference");
    public final static Item ERR_NOTICE_UNDEFINED_PROPERTY = new Item("Undefined property: %s::$%s");
    public final static Item ERR_FATAL_UNDEFINED_CLASS_CONSTANT = new Item("Undefined class constant '%s'");

    public final static Item ERR_FATAL_OPERATOR_ACCEPTS_ONLY_POSITIVE = new Item("'%s' operator accepts only positive numbers > 0");

    public static class Item {
        private String message;
        public Item(String message){
            this.message = message;
        }
        public String fetch(Object... args){
            return String.format(message, args);
        }
    }
}
