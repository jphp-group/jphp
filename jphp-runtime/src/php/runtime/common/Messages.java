package php.runtime.common;

import java.util.IllegalFormatException;

public class Messages {
    private Messages(){}

    public static Item ERR_FILE_NOT_FOUND = new Item("File not found - %s");

    public static Item ERR_PARSE_UNEXPECTED_END_OF_FILE = new Item("Syntax error, unexpected end");
    public static Item ERR_PARSE_UNEXPECTED_END_OF_STRING = new Item("Syntax error, unexpected end of string");
    public static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = new Item("Syntax error, unexpected '%s', expecting '%s'");
    public static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y_NO_QUOTES = new Item("Syntax error, unexpected '%s', expecting %s");
    public static Item ERR_PARSE_UNEXPECTED_X = new Item("Syntax error, unexpected '%s'");
    public static Item ERR_IDENTIFIER_X_ALREADY_USED = new Item("Identifier '%s' already used");

    public static Item ERR_CANNOT_JUMP_TO_LEVEL = new Item("Cannot break/continue %s level(s)");
    public static Item ERR_CANNOT_JUMP = new Item("Cannot break/continue");
    public static Item ERR_EXPECTED_CONST_VALUE = new Item("Expecting constant value for %s");
    public static Item ERR_CANNOT_ASSIGN_REF_TO_NON_REF_VALUE = new Item("Cannot assign reference to non referencable value");
    public static Item ERR_CANNOT_FETCH_OF_NON_ARRAY = new Item("Cannot fetch an item of non-array");

    public static Item ERR_CALL_TO_UNDEFINED_FUNCTION = new Item("Call to undefined function %s()");
    public static Item ERR_CALL_TO_UNDEFINED_METHOD = new Item("Call to undefined method %s()");
    public static Item ERR_NON_STATIC_METHOD_CALLED_DYNAMICALLY = new Item("Non-static method %s::%s() should not be called statically");

    public static Item ERR_CALL_TO_PRIVATE_METHOD = new Item("Call to private method %s() from context '%s'");
    public static Item ERR_CALL_TO_PROTECTED_METHOD = new Item("Call to protected method %s() from context '%s'");

    public static Item ERR_ACCESS_TO_PROTECTED_PROPERTY = new Item("Cannot access protected property %s::$%s");
    public static Item ERR_ACCESS_TO_PRIVATE_PROPERTY = new Item("Cannot access private property %s::$%s");
    public static Item ERR_ACCESS_TO_UNDECLARED_STATIC_PROPERTY = new Item("Access to undeclared static property: %s::$%s");

    public static Item ERR_ACCESS_TO_PROTECTED_CONSTANT = new Item("Cannot access protected constant %s::%s");
    public static Item ERR_ACCESS_TO_PRIVATE_CONSTANT = new Item("Cannot access private constant %s::%s");

    public static Item ERR_STATIC_METHOD_CALLED_DYNAMICALLY = new Item("Static method %s() should not be called dynamically");
    public static Item ERR_INCORRECT_ARGUMENTS_TO_FUNCTION = new Item("Pass incorrect number of arguments to function %s()");
    public static Item ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT = new Item("Cannot get '%s' property of non-object");
    public static Item ERR_CANNOT_CALL_OF_NON_OBJECT = new Item("Cannot call '%s' method of non-object");
    public static Item ERR_CANNOT_SET_PROPERTY_OF_NON_OBJECT = new Item("Cannot set '%s' property of non-object");
    public static Item ERR_CANNOT_GET_OBJECT_PROPERTY_OF_CLASS = new Item("Cannot get '%s' object property of '%s' class");
    public static Item ERR_CANNOT_REDECLARE_CLASS = new Item("Cannot redeclare class %s");
    public static Item ERR_CANNOT_REDECLARE_FUNCTION = new Item("Cannot redeclare function %s");
    public static Item ERR_CANNOT_REDECLARE_CONSTANT = new Item("Cannot redeclare constant %s");
    public static Item ERR_INVALID_DECLARE_CONSTANT = new Item("Invalid declare constant name '%s'");
    public static Item ERR_CANNOT_IMPLEMENT = new Item("%s cannot implement %s - it is not an interface");
    public static Item ERR_CANNOT_EXTENDS = new Item("%s cannot extend from %s - it is not an class");
    public static Item ERR_CANNOT_USE_NON_TRAIT = new Item("%s cannot use %s - it is not a trait");
    public static Item ERR_INTERFACE_NOT_FOUND = new Item("Interface '%s' not found");

    public static Item ERR_IMPLEMENT_METHOD =
            new Item("Class %s contains %s abstract method(s) and must therefore be declared abstract or implement the remaining methods (%s)");

    public static Item ERR_INVALID_METHOD_SIGNATURE = new Item("Declaration of %s should be compatible with %s");
    public static Item ERR_INTERFACE_FUNCTION_CANNOT_CONTAIN_BODY = new Item("Interface function %s cannot contain body");
    public static Item ERR_ACCESS_TYPE_FOR_INTERFACE_METHOD = new Item("Access type for interface method %s must be omitted");
    public static Item ERR_CANNOT_MAKE_NON_STATIC_TO_STATIC = new Item("Cannot make non static method %s static in class %s");
    public static Item ERR_CANNOT_MAKE_STATIC_TO_NON_STATIC = new Item("Cannot make static method %s non static in class %s");
    public static Item ERR_CLASS_MAY_NOT_INHERIT_FINAL_CLASS = new Item("Class %s may not inherit from final class (%s)");
    public static Item ERR_CANNOT_OVERRIDE_FINAL_METHOD = new Item("Cannot override final method %s with %s");
    public static Item ERR_NON_ABSTRACT_METHOD_MUST_CONTAIN_BODY = new Item("Non-abstract method %s must contain body");
    public static Item ERR_ABSTRACT_METHOD_CANNOT_CONTAIN_BODY = new Item("Abstract method %s cannot contain body");
    public static Item ERR_CANNOT_USE_FINAL_ON_ABSTRACT = new Item("Cannot use the final modifier on an abstract class member");

    public static Item ERR_CLASS_NOT_FOUND = new Item("Class '%s' not found");
    public static Item ERR_TRAIT_NOT_FOUND = new Item("Trait '%s' not found");
    public static Item ERR_FUNCTION_NOT_FOUND = new Item("Function '%s' not found");
    public static Item ERR_CONSTANT_NOT_FOUND = new Item("Constant '%s' not found");
    public static Item ERR_METHOD_NOT_FOUND = new Item("Method %s::%s() not found");
    public static Item ERR_MISSING_ARGUMENT = new Item("Missing argument %s for %s()");
    public static Item ERR_EXPECT_LEAST_PARAMS = new Item("%s() expects at least %s parameter(s), %s given");
    public static Item ERR_EXPECT_EXACTLY_PARAMS = new Item("%s() expects exactly %s parameter(s), %s given");
    public static Item ERR_REQUIRE_FAILED = new Item("%s(): Failed opening required '%s'");
    public static Item ERR_INCLUDE_FAILED = new Item("%s(): Failed opening '%s' for inclusion");
    public static Item ERR_USE_UNDEFINED_CONSTANT = new Item("Use of undefined constant %s - assumed '%s'");
    public static Item ERR_RETURN_NOT_REFERENCE = new Item("Only variable references should be returned by reference");
    public static Item ERR_YIELD_NOT_REFERENCE = new Item("Only variable references should be yielded by reference");
    public static Item ERR_UNDEFINED_PROPERTY = new Item("Undefined property: %s::$%s");
    public static Item ERR_READONLY_PROPERTY = new Item("Property %s::$%s is readonly");
    public static Item ERR_UNDEFINED_CLASS_CONSTANT = new Item("Undefined class constant '%s'");
    public static Item ERR_INDIRECT_MODIFICATION_OVERLOADED_PROPERTY = new Item("Indirect modification of overloaded property %s::$%s has no effect");

    public static Item ERR_OPERATOR_ACCEPTS_ONLY_POSITIVE = new Item("'%s' operator accepts only positive numbers > 0");
    public static Item ERR_CANNOT_MIX_ARRAY_AND_LIST = new Item("Cannot mix [] and list()");
    public static Item ERR_CANNOT_MIX_KEYED_AND_UNKEYED_ARRAY_ENTRIES = new Item("Cannot mix keyed and unkeyed array entries in assignments");

    public static Item ERR_CANNOT_REDEFINE_CLASS_CONSTANT = new Item("Cannot redefine class constant %s");
    public static Item ERR_CANNOT_INHERIT_OVERRIDE_CONSTANT = new Item("Cannot inherit previously-inherited or override constant %s from interface %s");

    public static Item ERR_INTERFACE_MAY_NOT_INCLUDE_VARS = new Item("Interfaces may not include member variables");
    public static Item ERR_PACKAGE_CONSTANT_MUST_BE_NON_EMPTY_STRING = new Item("Class constant %s::__PACKAGE__ must be a non-empty string");
    public static Item ERR_PACKAGE_FILE_MUST_RETURN_ARRAY = new Item("Cannot use file '%s' for defining package '%s', it must return an array as [classes=>[], functions=>[], constants=>[]]");

    public static Item ERR_CANNOT_USE_SYSTEM_CLASS = new Item("Cannot use system class/interface %s for %s");

    public static Item ERR_ACCESS_LEVEL_MUST_BE_PROTECTED_OR_WEAKER = new Item("Access level to %s::$%s must be protected (as in class %s) or weaker");
    public static Item ERR_ACCESS_LEVEL_METHOD_MUST_BE_PROTECTED_OR_WEAKER = new Item("Access level to %s::%s() must be protected (as in class %s) or weaker");
    public static Item ERR_ACCESS_LEVEL_MUST_BE_PUBLIC = new Item("Access level to %s::$%s must be public (as in class %s)");
    public static Item ERR_ACCESS_LEVEL_METHOD_MUST_BE_PUBLIC = new Item("Access level to %s::%s() must be public (as in class %s)");
    public static Item ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC = new Item("Access level to %s::%s must be public (as in class %s)");
    public static Item ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC_FOR_INTERFACE = new Item("Access type for interface constant %s::%s must be public");
    public static Item ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PROTECTED = new Item("Access level to %s::%s must be protected (as in class %s)");
    public static Item ERR_CANNOT_REDECLARE_STATIC_AS_NON_STATIC = new Item("Cannot redeclare static %s::$%s as non static %s::$%s");
    public static Item ERR_CANNOT_REDECLARE_NON_STATIC_AS_STATIC = new Item("Cannot redeclare non static %s::$%s as static %s::$%s");

    public static Item ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC = new Item("Accessing static property %s::$%s as non static");

    public static Item ERR_CANNOT_USE_OBJECT_AS_ARRAY = new Item("Cannot use object %s as array");

    public static Item ERR_INVALID_ARRAY_ELEMENT_TYPE = new Item("All array elements must be instances of %s class, %s given");

    public static Item ERR_TRAIT_METHOD_COLLISION = new Item("Trait method '%s' has not been applied, because there are collision in '%s' and '%s' traits on %s");
    public static Item ERR_TRAIT_SAME_PROPERTY = new Item("%s and %s define the same property ($%s) in the composition of %s. However, the definition differs and is considered incompatible. Class was composed");

    public static Item ERR_TRAIT_MULTIPLE_RULE =
            new Item("Failed to evaluate a trait precedence (%s). Method of trait '%s' was defined to be excluded multiple times");

    public static Item ERR_TRAIT_WAS_NOT_ADDED = new Item("Required Trait '%s' wasn't added to '%s'");

    public static Item ERR_CANNOT_USE_EMPTY_LIST = new Item("Cannot use empty list");

    public static Item ERR_YIELD_CAN_ONLY_INSIDE_FUNCTION = new Item("The \"yield\" expression can only be used inside a function");

    public static Item ERR_RETURN_TYPE_INVALID = new Item("Return value of %s() must be %s, %s returned");
    public static Item ERR_INVALID_UNICODE_ESCAPE_SEQUENCE = new Item("Invalid UTF-8 codepoint escape sequence");
    public static Item ERR_INVALID_UNICODE_ESCAPE_SEQUENCE_WITH_REASON = new Item(ERR_INVALID_UNICODE_ESCAPE_SEQUENCE.message + ": %s");

    public static Item ERR_TIMEZONE_NULL_BYTE = new Item("%s: Timezone must not contain null bytes");
    public static Item ERR_WRONG_PARAM_TYPE = new Item("%s expects parameter %d to be %s, %s given");

    public static Item ERR_TYPED_PROP_INVALID_TYPE_OF_INHERITANCE = new Item("Type of %s::$%s must be %s (as in class %s)");
    public static Item ERR_TYPED_PROP_TYPE_MUST_BE_NOT_DEFINED_OF_INHERITANCE = new Item("Type of %s::$%s must not be defined (as in class %s)");

    public static Item ERR_INVALID_TYPE_ARGUMENT_PASSED = new Item("Argument %s passed to %s() must be %s, called in %s on line %d, position %d and defined");
    public static Item ERR_INVALID_SIMPLE_TYPE_ARGUMENT_PASSED = new Item("Argument %s passed to %s() must be of the type %s, %s given, called in %s on line %d, position %d and defined");
    public static Item ERR_INVALID_RANGE_ARGUMENT_PASSED = new Item("Argument %s passed to %s() must be a string belonging to the range [%s] as string, called in %s on line %d, position %d and defined");

    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED = new Item("Argument %s passed to %s() must %s, %s given, called in %s on line %d, position %d and defined");
    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED_BE_INSTANCE = new Item("be an instance of");
    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED_INTERFACE = new Item("implement interface");
    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED_OR_NULL = new Item("or null");

    public static Item ERR_ONLY_REF_CAN_BE_PASSED = new Item("Only variables can be passed by reference");
    public static Item ERR_ONLY_ARR_AND_TRAV_CAN_UNPACKED = new Item("Only arrays and Traversables can be unpacked");
    public static Item ERR_CANNOT_USE_POS_ARG_AFTER_UNPACK = new Item("Cannot use positional argument after argument unpacking");

    public static Item ERR_RETURN_DISALLOWED_MEMORY_AS_REF = new Item("Unable to return %s as ref, jphp will not support this feature");
    public static Item ERR_PASS_DISALLOWED_MEMORY_AS_REF = new Item("Unable to pass %s as ref argument, jphp will not support this feature");
    public static Item ERR_ASSIGN_BY_REF_FOR_TYPED_PROP = new Item("Unable to assign by ref for typed property %s::$%s, jphp will not support this feature");

    public static Item ERR_CANNOT_AUTO_INIT_ARR_IN_PROP = new Item("Cannot auto-initialize an array inside property %s::$%s of type %s");
    public static Item ERR_ACCESS_TO_STATIC_PROPERTY_BEFORE_INIT = new Item("Typed static property %s::$%s must not be accessed before initialization");
    public static Item ERR_ACCESS_TO_PROPERTY_BEFORE_INIT = new Item("Typed property %s::$%s must not be accessed before initialization");

    public static class Item {
        private String message;
        public Item(String message){
            this.message = message;
        }
        public String fetch(Object... args) {
            try {
                return String.format(message, args);
            } catch (IllegalFormatException e) {
                return message;
            }
        }
    }
}
