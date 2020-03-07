package php.runtime.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Map.Entry;
import php.runtime.common.l10n.L10NMessages;
import php.runtime.common.l10n.Messages_RU;

public class Messages {
    private Messages(){}

    public static Item MSG_STACK_TRACE = new Item("Stack Trace");
    public static Item MSG_UNCAUGHT_ERROR = new Item("Uncaught {0}: {1}");
    public static Item MSG_UNCAUGHT_FATAL = new Item("Uncaught exception '{0}' with message '{1}'");
    public static Item MSG_CALLED_AT = new Item("called at [{0}:{1}]");
    public static Item MSG_INTERNAL_SYMBOL = new Item("<internal>");
    public static Item MSG_CLOSURE_SYMBOL = new Item("{closure}");
    public static Item MSG_MAIN_SYMBOL = new Item("{main}");
    public static Item MSG_THROWN_IN_ON_LINE = new Item("thrown in {0} on line {1}");
    public static Item MSG_IN_ON_LINE_POS = new Item("in {0} on line {1}, position {2}");
    public static Item MSG_IN_ON_LINE_AT_POS = new Item("in '{0}' on line {1} at pos {2}");

    public static Item MSG_E_DEPRECATED = new Item("Deprecated");
    public static Item MSG_E_STRICT = new Item("Strict Standards");
    public static Item MSG_E_NOTICE = new Item("Notice");
    public static Item MSG_E_ALL = new Item("Message");
    public static Item MSG_E_COMPILE_ERROR = new Item("Compile error");
    public static Item MSG_E_COMPILE_WARNING = new Item("Compile warning");
    public static Item MSG_E_CORE_ERROR = new Item("Core error");
    public static Item MSG_E_CORE_WARNING = new Item("Core warning");
    public static Item MSG_E_ERROR = new Item("Fatal error");
    public static Item MSG_E_PARSE = new Item("Parse error");
    public static Item MSG_E_RECOVERABLE_ERROR = new Item("Recoverable error");
    public static Item MSG_E_USER_DEPRECATED = new Item("User deprecated");
    public static Item MSG_E_USER_ERROR = new Item("User error");
    public static Item MSG_E_USER_NOTICE = new Item("User notice");
    public static Item MSG_E_USER_WARNING = new Item("User warning");
    public static Item MSG_E_WARNING = new Item("Warning");
    public static Item MSG_E_UNKNOWN = new Item("Unknown");

    public static Item ERR_FILE_NOT_FOUND = new Item("File not found - {0}");

    public static Item ERR_PARSE_UNEXPECTED_END_OF_FILE = new Item("Syntax error, unexpected end");
    public static Item ERR_PARSE_UNEXPECTED_END_OF_STRING = new Item("Syntax error, unexpected end of string");
    public static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = new Item("Syntax error, unexpected '{0}', expecting '{1}'");
    public static Item ERR_PARSE_UNEXPECTED_X_EXPECTED_Y_NO_QUOTES = new Item("Syntax error, unexpected '{0}', expecting {1}");
    public static Item ERR_PARSE_UNEXPECTED_X = new Item("Syntax error, unexpected '{0}'");
    public static Item ERR_IDENTIFIER_X_ALREADY_USED = new Item("Identifier '{0}' already used");

    public static Item ERR_CANNOT_JUMP_TO_LEVEL = new Item("Cannot break/continue {0} level(s)");
    public static Item ERR_CANNOT_JUMP = new Item("Cannot break/continue");
    public static Item ERR_EXPECTED_CONST_VALUE = new Item("Expecting constant value for {0}");
    public static Item ERR_CANNOT_ASSIGN_REF_TO_NON_REF_VALUE = new Item("Cannot assign reference to non referencable value");
    public static Item ERR_CANNOT_FETCH_OF_NON_ARRAY = new Item("Cannot fetch an item of non-array");

    public static Item ERR_CALL_TO_UNDEFINED_FUNCTION = new Item("Call to undefined function {0}()");
    public static Item ERR_CALL_TO_UNDEFINED_METHOD = new Item("Call to undefined method {0}()");
    public static Item ERR_NON_STATIC_METHOD_CALLED_DYNAMICALLY = new Item("Non-static method {0}::{1}() should not be called statically");

    public static Item ERR_CALL_TO_PRIVATE_METHOD = new Item("Call to private method {0}() from context '{1}'");
    public static Item ERR_CALL_TO_PROTECTED_METHOD = new Item("Call to protected method {0}() from context '{1}'");

    public static Item ERR_ACCESS_TO_PROTECTED_PROPERTY = new Item("Cannot access protected property {0}::${1}");
    public static Item ERR_ACCESS_TO_PRIVATE_PROPERTY = new Item("Cannot access private property {0}::${1}");
    public static Item ERR_ACCESS_TO_UNDECLARED_STATIC_PROPERTY = new Item("Access to undeclared static property: {0}::${1}");

    public static Item ERR_ACCESS_TO_PROTECTED_CONSTANT = new Item("Cannot access protected constant {0}::{1}");
    public static Item ERR_ACCESS_TO_PRIVATE_CONSTANT = new Item("Cannot access private constant {0}::{1}");

    public static Item ERR_STATIC_METHOD_CALLED_DYNAMICALLY = new Item("Static method {0}() should not be called dynamically");
    public static Item ERR_INCORRECT_ARGUMENTS_TO_FUNCTION = new Item("Pass incorrect number of arguments to function {0}()");
    public static Item ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT = new Item("Cannot get '{0}' property of non-object");
    public static Item ERR_CANNOT_CALL_OF_NON_OBJECT = new Item("Cannot call '{0}' method of non-object");
    public static Item ERR_CANNOT_SET_PROPERTY_OF_NON_OBJECT = new Item("Cannot set '{0}' property of non-object");
    public static Item ERR_CANNOT_GET_OBJECT_PROPERTY_OF_CLASS = new Item("Cannot get '{0}' object property of '{1}' class");
    public static Item ERR_CANNOT_REDECLARE_CLASS = new Item("Cannot redeclare class {0}");
    public static Item ERR_CANNOT_REDECLARE_FUNCTION = new Item("Cannot redeclare function {0}");
    public static Item ERR_CANNOT_REDECLARE_CONSTANT = new Item("Cannot redeclare constant {0}");
    public static Item ERR_INVALID_DECLARE_CONSTANT = new Item("Invalid declare constant name '{0}'");
    public static Item ERR_CANNOT_IMPLEMENT = new Item("{0} cannot implement {1} - it is not an interface");
    public static Item ERR_CANNOT_EXTENDS = new Item("{0} cannot extend from {1} - it is not an class");
    public static Item ERR_CANNOT_USE_NON_TRAIT = new Item("{0} cannot use {1} - it is not a trait");
    public static Item ERR_INTERFACE_NOT_FOUND = new Item("Interface '{0}' not found");

    public static Item ERR_IMPLEMENT_METHOD =
            new Item("Class {0} contains {1} abstract method(s) and must therefore be declared abstract or implement the remaining methods ({2})");

    public static Item ERR_INVALID_METHOD_SIGNATURE = new Item("Declaration of {0} should be compatible with {1}");
    public static Item ERR_INTERFACE_FUNCTION_CANNOT_CONTAIN_BODY = new Item("Interface function {0} cannot contain body");
    public static Item ERR_ACCESS_TYPE_FOR_INTERFACE_METHOD = new Item("Access type for interface method {0} must be omitted");
    public static Item ERR_CANNOT_MAKE_NON_STATIC_TO_STATIC = new Item("Cannot make non static method {0} static in class {1}");
    public static Item ERR_CANNOT_MAKE_STATIC_TO_NON_STATIC = new Item("Cannot make static method {0} non static in class {1}");
    public static Item ERR_CLASS_MAY_NOT_INHERIT_FINAL_CLASS = new Item("Class {0} may not inherit from final class ({1})");
    public static Item ERR_CANNOT_OVERRIDE_FINAL_METHOD = new Item("Cannot override final method {0} with {1}");
    public static Item ERR_NON_ABSTRACT_METHOD_MUST_CONTAIN_BODY = new Item("Non-abstract method {0} must contain body");
    public static Item ERR_ABSTRACT_METHOD_CANNOT_CONTAIN_BODY = new Item("Abstract method {0} cannot contain body");
    public static Item ERR_CANNOT_USE_FINAL_ON_ABSTRACT = new Item("Cannot use the final modifier on an abstract class member");

    public static Item ERR_CLASS_NOT_FOUND = new Item("Class '{0}' not found");
    public static Item ERR_TRAIT_NOT_FOUND = new Item("Trait '{0}' not found");
    public static Item ERR_FUNCTION_NOT_FOUND = new Item("Function '{0}' not found");
    public static Item ERR_CONSTANT_NOT_FOUND = new Item("Constant '{0}' not found");
    public static Item ERR_METHOD_NOT_FOUND = new Item("Method {0}::{1}() not found");
    public static Item ERR_MISSING_ARGUMENT = new Item("Missing argument {0} for {1}()");
    public static Item ERR_EXPECT_LEAST_PARAMS = new Item("{0}() expects at least {1} parameter(s), {2} given");
    public static Item ERR_EXPECT_EXACTLY_PARAMS = new Item("{0}() expects exactly {1} parameter(s), {2} given");
    public static Item ERR_REQUIRE_FAILED = new Item("{0}(): Failed opening required '{1}'");
    public static Item ERR_INCLUDE_FAILED = new Item("{0}(): Failed opening '{1}' for inclusion");
    public static Item ERR_USE_UNDEFINED_CONSTANT = new Item("Use of undefined constant {0} - assumed '{1}'");
    public static Item ERR_RETURN_NOT_REFERENCE = new Item("Only variable references should be returned by reference");
    public static Item ERR_YIELD_NOT_REFERENCE = new Item("Only variable references should be yielded by reference");
    public static Item ERR_UNDEFINED_PROPERTY = new Item("Undefined property: {0}::${1}");
    public static Item ERR_READONLY_PROPERTY = new Item("Property {0}::${1} is readonly");
    public static Item ERR_UNDEFINED_CLASS_CONSTANT = new Item("Undefined class constant '{0}'");
    public static Item ERR_INDIRECT_MODIFICATION_OVERLOADED_PROPERTY = new Item("Indirect modification of overloaded property {0}::${1} has no effect");

    public static Item ERR_OPERATOR_ACCEPTS_ONLY_POSITIVE = new Item("'{0}' operator accepts only positive numbers > 0");
    public static Item ERR_CANNOT_MIX_ARRAY_AND_LIST = new Item("Cannot mix [] and list()");
    public static Item ERR_SPREAD_OPERATOR_NOT_SUPPORTED_IN_ASSIGN = new Item("Spread operator is not supported in assignments");
    public static Item ERR_CANNOT_MIX_KEYED_AND_UNKEYED_ARRAY_ENTRIES = new Item("Cannot mix keyed and unkeyed array entries in assignments");

    public static Item ERR_CANNOT_REDEFINE_CLASS_CONSTANT = new Item("Cannot redefine class constant {0}");
    public static Item ERR_CANNOT_INHERIT_OVERRIDE_CONSTANT = new Item("Cannot inherit previously-inherited or override constant {0} from interface {1}");

    public static Item ERR_INTERFACE_MAY_NOT_INCLUDE_VARS = new Item("Interfaces may not include member variables");
    public static Item ERR_PACKAGE_CONSTANT_MUST_BE_NON_EMPTY_STRING = new Item("Class constant {0}::__PACKAGE__ must be a non-empty string");
    public static Item ERR_PACKAGE_FILE_MUST_RETURN_ARRAY = new Item("Cannot use file '{0}' for defining package '{1}', it must return an array as [classes=>[], functions=>[], constants=>[]]");

    public static Item ERR_CANNOT_USE_SYSTEM_CLASS = new Item("Cannot use system class/interface {0} for {1}");

    public static Item ERR_ACCESS_LEVEL_MUST_BE_PROTECTED_OR_WEAKER = new Item("Access level to {0}::${1} must be protected (as in class {2}) or weaker");
    public static Item ERR_ACCESS_LEVEL_METHOD_MUST_BE_PROTECTED_OR_WEAKER = new Item("Access level to {0}::{1}() must be protected (as in class {2}) or weaker");
    public static Item ERR_ACCESS_LEVEL_MUST_BE_PUBLIC = new Item("Access level to {0}::${1} must be public (as in class {2})");
    public static Item ERR_ACCESS_LEVEL_METHOD_MUST_BE_PUBLIC = new Item("Access level to {0}::{1}() must be public (as in class {2})");
    public static Item ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC = new Item("Access level to {0}::{1} must be public (as in class {2})");
    public static Item ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC_FOR_INTERFACE = new Item("Access type for interface constant {0}::{1} must be public");
    public static Item ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PROTECTED = new Item("Access level to {0}::{1} must be protected (as in class {2})");
    public static Item ERR_CANNOT_REDECLARE_STATIC_AS_NON_STATIC = new Item("Cannot redeclare static {0}::${1} as non static {2}::${3}");
    public static Item ERR_CANNOT_REDECLARE_NON_STATIC_AS_STATIC = new Item("Cannot redeclare non static {0}::${1} as static {2}::${3}");

    public static Item ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC = new Item("Accessing static property {0}::${1} as non static");

    public static Item ERR_CANNOT_USE_OBJECT_AS_ARRAY = new Item("Cannot use object {0} as array");

    public static Item ERR_INVALID_ARRAY_ELEMENT_TYPE = new Item("All array elements must be instances of {0} class, {1} given");

    public static Item ERR_TRAIT_METHOD_COLLISION = new Item("Trait method '{0}' has not been applied, because there are collision in '{1}' and '{2}' traits on {3}");
    public static Item ERR_TRAIT_SAME_PROPERTY = new Item("{0} and {1} define the same property (${2}) in the composition of {3}. However, the definition differs and is considered incompatible. Class was composed");

    public static Item ERR_TRAIT_MULTIPLE_RULE =
            new Item("Failed to evaluate a trait precedence ({0}). Method of trait '{1}' was defined to be excluded multiple times");

    public static Item ERR_TRAIT_WAS_NOT_ADDED = new Item("Required Trait '{0}' wasn't added to '{1}'");

    public static Item ERR_CANNOT_USE_EMPTY_LIST = new Item("Cannot use empty list");

    public static Item ERR_YIELD_CAN_ONLY_INSIDE_FUNCTION = new Item("The \"yield\" expression can only be used inside a function");

    public static Item ERR_RETURN_TYPE_INVALID = new Item("Return value of {0}() must be {1}, {2} returned");
    public static Item ERR_INVALID_UNICODE_ESCAPE_SEQUENCE = new Item("Invalid UTF-8 codepoint escape sequence");
    public static Item ERR_INVALID_UNICODE_ESCAPE_SEQUENCE_WITH_REASON = new Item(ERR_INVALID_UNICODE_ESCAPE_SEQUENCE.message + ": %s");

    public static Item ERR_TIMEZONE_NULL_BYTE = new Item("{0}: Timezone must not contain null bytes");
    public static Item ERR_WRONG_PARAM_TYPE = new Item("{0} expects parameter {1} to be {2}, {3} given");

    public static Item ERR_TYPED_PROP_INVALID_TYPE_OF_INHERITANCE = new Item("Type of {0}::${1} must be {2} (as in class {3})");
    public static Item ERR_TYPED_PROP_TYPE_MUST_BE_NOT_DEFINED_OF_INHERITANCE = new Item("Type of {0}::${1} must not be defined (as in class {2})");

    public static Item ERR_INVALID_TYPE_ARGUMENT_PASSED = new Item("Argument {0} passed to {1}() must be {2}, called in {3} on line {4}, position {5} and defined");
    public static Item ERR_INVALID_SIMPLE_TYPE_ARGUMENT_PASSED = new Item("Argument {0} passed to {1}() must be of the type {2}, {3} given, called in {4} on line {5}, position {6} and defined");
    public static Item ERR_INVALID_RANGE_ARGUMENT_PASSED = new Item("Argument {0} passed to {1}() must be a string belonging to the range [{2}] as string, called in {3} on line {4}, position {5} and defined");

    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED = new Item("Argument {0} passed to {1}() must {2}, {3} given, called in {4} on line {5}, position {6} and defined");
    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED_BE_INSTANCE = new Item("be an instance of");
    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED_INTERFACE = new Item("implement interface");
    public static Item ERR_INVALID_OBJ_ARGUMENT_PASSED_OR_NULL = new Item("or null");

    public static Item ERR_ONLY_REF_CAN_BE_PASSED = new Item("Only variables can be passed by reference");
    public static Item ERR_ONLY_ARR_AND_TRAV_CAN_UNPACKED = new Item("Only arrays and Traversables can be unpacked");
    public static Item ERR_CANNOT_USE_POS_ARG_AFTER_UNPACK = new Item("Cannot use positional argument after argument unpacking");

    public static Item ERR_RETURN_DISALLOWED_MEMORY_AS_REF = new Item("Unable to return {0} as ref, jphp will not support this feature");
    public static Item ERR_PASS_DISALLOWED_MEMORY_AS_REF = new Item("Unable to pass {0} as ref argument, jphp will not support this feature");
    public static Item ERR_ASSIGN_BY_REF_FOR_TYPED_PROP = new Item("Unable to assign by ref for typed property {0}::${1}, jphp will not support this feature");

    public static Item ERR_CANNOT_AUTO_INIT_ARR_IN_PROP = new Item("Cannot auto-initialize an array inside property {0}::${1} of type {2}");
    public static Item ERR_ACCESS_TO_STATIC_PROPERTY_BEFORE_INIT = new Item("Typed static property {0}::${1} must not be accessed before initialization");
    public static Item ERR_ACCESS_TO_PROPERTY_BEFORE_INIT = new Item("Typed property {0}::${1} must not be accessed before initialization");

    public static Item ERR_CANNOT_ACCESS_PARENT_WHEN_SCOPE_NO_PARENT = new Item("Cannot access parent:: when current class scope has no parent");
    public static Item ERR_CANNOT_FIND_JAVA_CONSTRUCTOR = new Item("Cannot find a java constructor {0}(Environment, ClassEntity)");
    public static Item ERR_CANNOT_INSTANT_ABSTRACT_CLASS = new Item("Cannot instantiate abstract class {0}");
    public static Item ERR_CANNOT_INSTANT_INTERFACE = new Item("Cannot instantiate interface {0}");
    public static Item ERR_CANNOT_INSTANT_TRAIT = new Item("Cannot instantiate trait {0}");

    public static Item ERR_ONLY_ARR_OR_TRAVERSABLES_CAN_BE_UNPACKED = new Item("Only arrays and Traversables can be unpacked");
    public static Item ERR_CANNOT_UNPACK_ARR_WITH_STRING_KEYS = new Item("Cannot unpack array with string keys");
    public static Item ERR_CANNOT_UNPACK_TRAVERSABLE_WITH_NON_INT_KEYS = new Item("Cannot unpack Traversable with non-integer keys");

    public static Item ERR_USING_THIS_NOT_IN_OBJECT_CONTEXT = new Item("Using $this when not in object context");
    public static Item ERR_CANNOT_RE_ASSIGN_THIS = new Item("Cannot re-assign $this");

    protected static Map<String, Item> itemMap = new HashMap<>();
    protected static Map<String, Item> originItemMap = new HashMap<>();

    public static void localize(L10NMessages messages) throws Throwable {
        if (messages == null) {
            for (Item value : itemMap.values()) {
                value.localizedMessage = null;
            }
        } else {
            for (Entry<String, String> entry : messages.getMessages().entrySet()) {
                Item item = itemMap.get(entry.getKey());

                if (item != null) {
                    item.localizedMessage = entry.getValue();
                }
            }
        }
    }

    static {
        Field[] fields = Messages.class.getFields();
        for (Field field : fields) {
            try {
                Object o = field.get(null);
                if (o instanceof Item) {
                    Item item = (Item) o;
                    itemMap.put(field.getName(), item);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        L10NMessages.register(new Messages_RU());
    }

    public static class Item {
        private String message;
        private String localizedMessage;
        private boolean oldFormat;

        public Item(String message) {
            this.message = message;
            this.oldFormat = message.contains("%s") || message.contains("%d");
        }

        public String fetch(Object... args) {
            try {
                if (oldFormat) {
                    return String.format(localizedMessage == null ? message : localizedMessage, args);
                } else {
                    String format = localizedMessage == null ? message : localizedMessage;
                    if (args != null) {
                        int i = 0;
                        for (Object arg : args) {
                            format = format.replace("{" + i + "}", String.valueOf(arg));
                            i++;
                        }
                    }

                    return format;
                }
            } catch (IllegalFormatException e) {
                return localizedMessage == null ? message : localizedMessage;
            }
        }

        @Override
        public String toString() {
            return fetch();
        }
    }
}
