package ru.regenix.jphp.common;

final public class Messages {

    private Messages(){}

    public final static Item ERR_PARSE_UNEXPECTED_END_OF_FILE = new Item("Syntax error, unexpected end");
    public final static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = new Item("Syntax error, unexpected '%s', expecting '%s'");
    public final static Item ERR_PARSE_UNEXPECTED_X = new Item("Syntax error, unexpected '%s'");
    public final static Item ERR_COMPILE_CANNOT_JUMP_TO_LEVEL = new Item("Cannot break/continue %s level(s)");
    public final static Item ERR_COMPILE_CANNOT_JUMP = new Item("Cannot break/continue");
    public final static Item ERR_COMPILE_EXPECTED_CONST_VALUE = new Item("Expecting const value for %s");
    public final static Item ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION = new Item("Call to undefined function %s()");
    public final static Item ERR_FATAL_CALL_TO_UNDEFINED_METHOD = new Item("Call to undefined method %s()");
    public final static Item ERR_FATAL_PASS_INCORRECT_ARGUMENTS_TO_FUNCTION = new Item("Pass incorrect number of arguments to function %s()");
    public final static Item ERR_FATAL_CANNOT_GET_PROPERTY_OF_NON_OBJECT = new Item("Cannot get '%s' property of non-object");
    public final static Item ERR_FATAL_CANNOT_CALL_OF_NON_OBJECT = new Item("Cannot call '%s' method of non-object");
    public final static Item ERR_FATAL_CANNOT_SET_PROPERTY_OF_NON_OBJECT = new Item("Cannot set '%s' property of non-object");
    public final static Item ERR_FATAL_CANNOT_GET_OBJECT_PROPERTY_OF_CLASS = new Item("Cannot get '%s' object property of '%s' class");

    public final static Item ERR_FATAL_CLASS_NOT_FOUND = new Item("Class '%s' not found");
    public final static Item ERR_WARNING_MISSING_ARGUMENT = new Item("Missing argument %s for %s()");
    public final static Item ERR_FATAL_REQUIRE_FAILED = new Item("%s(): Failed opening required '%s'");
    public final static Item ERR_WARNING_INCLUDE_FAILED = new Item("%s(): Failed opening '%s' for inclusion");
    public final static Item ERR_NOTICE_USE_UNDEFINED_CONSTANT = new Item("Use of undefined constant %s - assumed '%s'");
    public final static Item ERR_NOTICE_RETURN_NOT_REFERENCE = new Item("Only variable references should be returned by reference");

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
