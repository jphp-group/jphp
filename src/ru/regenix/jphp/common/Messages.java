package ru.regenix.jphp.common;

final public class Messages {

    private Messages(){}

    public final static Item ERR_PARSE_UNEXPECTED_END_OF_FILE = new Item("Syntax error, unexpected end of file, expecting variable (T_VARIABLE) or ${ (T_DOLLAR_OPEN_CURLY_BRACES) or {$ (T_CURLY_OPEN)");
    public final static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = new Item("Syntax error, unexpected %s, expecting %s");
    public final static Item ERR_PARSE_UNEXPECTED_X = new Item("Syntax error, unexpected %s");


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
