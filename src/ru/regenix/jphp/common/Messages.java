package ru.regenix.jphp.common;

final public class Messages {

    private Messages(){}

    public final static Item ERR_PARSE_UNEXPECTED_END_OF_FILE = new Item("Syntax error, unexpected end of file, expecting variable (T_VARIABLE) or ${ (T_DOLLAR_OPEN_CURLY_BRACES) or {$ (T_CURLY_OPEN)");
    public final static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = new Item("Syntax error, unexpected %s, expecting %s");
    public final static Item ERR_PARSE_UNEXPECTED_X = new Item("Syntax error, unexpected '%s'");
    public final static Item ERR_COMPILE_CANNOT_JUMP_TO_LEVEL = new Item("Cannot break/continue %s level(s)");
    public final static Item ERR_COMPILE_CANNOT_JUMP = new Item("Cannot break/continue");
    public final static Item ERR_FATAL_CALL_TO_UNDEFINED_FUNCTION = new Item("Call to undefined function %s()");
    public final static Item ERR_FATAL_CALL_TO_UNDEFINED_METHOD = new Item("Call to undefined method %s()");
    public final static Item ERR_FATAL_PASS_INCORRECT_ARGUMENTS_TO_FUNCTION = new Item("Pass incorrect number of arguments to function %s()");

    public final static Item ERR_WARNING_MISSING_ARGUMENT = new Item("Missing argument %s for %s()");
    public final static Item ERR_FATAL_REQUIRE_FAILED = new Item("%s(): Failed opening required '%s'");
    public final static Item ERR_WARNING_INCLUDE_FAILED = new Item("%s(): Failed opening '%s' for inclusion");
    public final static Item ERR_NOTICE_USE_UNDEFINED_CONSTANT = new Item("Use of undefined constant %s - assumed '%s'");
    public final static Item ERR_NOTICE_RETURN_NOT_REFERENCE = new Item("Only variable references should be returned by reference");

    public final static Item ERR_FATAL_OPERATOR_ACCEPTS_ONLY_POSITIVE = new Item("'%s' operator accepts only positive numbers > 0");

    public static class Item {
        private String message;
        protected Item(String message){
            this.message = message;
        }
        public String fetch(Object... args){
            return String.format(message, args);
        }
    }
}
