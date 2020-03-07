package php.runtime.common.l10n;

import php.runtime.common.Messages.Item;

public class Messages_RU extends L10NMessages {

    public String MSG_STACK_TRACE = "Стек Трейс";
    public String MSG_UNCAUGHT_ERROR = "Непойманная ошибка ({0}): {1}";
    public String MSG_UNCAUGHT_FATAL = "Непойманное исключение '{0}' с сообщением '{1}'";
    public String MSG_CALLED_AT = "вызвано из [{0}:{1}]";
    public String MSG_INTERNAL_SYMBOL = "<система>";
    public String MSG_MAIN_SYMBOL = "<начало>";
    public String MSG_THROWN_IN_ON_LINE = "произошло в {0} на строке {1}";
    public String MSG_IN_ON_LINE_POS = "в {0} на строке {1}, позиция {2}";
    public String MSG_IN_ON_LINE_AT_POS = "в '{0}' на строке {1} в позиции {2}";

    public String MSG_E_DEPRECATED = "Устаревшее";
    public String MSG_E_STRICT = "Строгие Стандарты";
    public String MSG_E_NOTICE = "Замечание";
    public String MSG_E_ALL = "Сообщение";
    public String MSG_E_COMPILE_ERROR = "Ошибка компиляции";
    public String MSG_E_COMPILE_WARNING = "Предупреждение компиляции";
    public String MSG_E_CORE_ERROR = "Ошибка ядра";
    public String MSG_E_CORE_WARNING = "Предупреждение ядра";
    public String MSG_E_ERROR = "Фатальная ошибка";
    public String MSG_E_PARSE = "Ошибка парсинга";
    public String MSG_E_RECOVERABLE_ERROR = "Исправимая ошибка";
    public String MSG_E_USER_DEPRECATED = "Устаревшее (Своё)";
    public String MSG_E_USER_ERROR = "Ошибка (Своя)";
    public String MSG_E_USER_NOTICE = "Замечание (Своё)";
    public String MSG_E_USER_WARNING = "Предупреждение (Своё)";
    public String MSG_E_WARNING = "Предупреждение";
    public String MSG_E_UNKNOWN = "Неизвестно";

    public String ERR_FILE_NOT_FOUND = "Файл не найден - {0}";

    public String ERR_PARSE_UNEXPECTED_END_OF_FILE = "Ошибка синтаксиса, неожиданный конец";
    public String ERR_PARSE_UNEXPECTED_END_OF_STRING = "Ошибка синтаксиса, неожиданный конец для строки";
    public String ERR_PARSE_UNEXPECTED_X_EXPECTED_Y = "Ошибка синтаксиса, неожидаемое '{0}', ожидаемое '{1}'";
    public String ERR_PARSE_UNEXPECTED_X_EXPECTED_Y_NO_QUOTES = "Ошибка синтаксиса, неожидаемое '{0}', ожидаемое {1}";
    public String ERR_PARSE_UNEXPECTED_X = "Ошибка синтаксиса, неожидаемое '{0}'";
    public String ERR_IDENTIFIER_X_ALREADY_USED = "Индетификатор '{0}' уже используется";

    public String ERR_CANNOT_JUMP_TO_LEVEL = "Невозможно сделать break/continue на {0} уровн(ь/я/ей)";
    public String ERR_CANNOT_JUMP = "Невозможно сделать break/continue";
    public String ERR_EXPECTED_CONST_VALUE = "Ожидалось константное значение для {0}";
    public String ERR_CANNOT_ASSIGN_REF_TO_NON_REF_VALUE = "Невозможно присвоить ссылку к нессылочному значению";
    public String ERR_CANNOT_FETCH_OF_NON_ARRAY = "Невозможно достать строку не из массива";

    public String ERR_CALL_TO_UNDEFINED_FUNCTION = "Вызов несуществующей функции {0}()";
    public String ERR_CALL_TO_UNDEFINED_METHOD = "Вызов несуществующего метода {0}()";
    public String ERR_NON_STATIC_METHOD_CALLED_DYNAMICALLY = "Обычный метод {0}::{1}() не может быть вызван как статический";

    public String ERR_CALL_TO_PRIVATE_METHOD = "Вызов приватного (private) метода {0}() из контекста '{1}'";
    public String ERR_CALL_TO_PROTECTED_METHOD = "Вызов защищенного (protected) метода {0}() из контекста '{1}'";

    public String ERR_ACCESS_TO_PROTECTED_PROPERTY = "Невозможно обраться к защищенному (protected) свойству {0}::${1}";
    public String ERR_ACCESS_TO_PRIVATE_PROPERTY = "Невозможно обраться к приватному (private) свойству {0}::${1}";
    public String ERR_ACCESS_TO_UNDECLARED_STATIC_PROPERTY = "Обращение к необъявленному свойству: {0}::${1}";

    public String ERR_ACCESS_TO_PROTECTED_CONSTANT = "Невозможно обратиться к защищенной (protected) константе {0}::{1}";
    public String ERR_ACCESS_TO_PRIVATE_CONSTANT = "Невозможно обратиться к приватной (private) константе {0}::{1}";

    public String ERR_STATIC_METHOD_CALLED_DYNAMICALLY = "Метод {0}() должен быть вызван динамически";
    public String ERR_INCORRECT_ARGUMENTS_TO_FUNCTION = "Передано некорректное количество аргументов в функцию {0}()";
    public String ERR_CANNOT_GET_PROPERTY_OF_NON_OBJECT = "Невозможно обратиться к '{0}' свойству для не объекта";
    public String ERR_CANNOT_CALL_OF_NON_OBJECT = "Невозможно вызвать '{0}' метод для не объекта";
    public String ERR_CANNOT_SET_PROPERTY_OF_NON_OBJECT = "Невозможно изменить '{0}' свойство для не объекта";
    public String ERR_CANNOT_GET_OBJECT_PROPERTY_OF_CLASS = "Невозможно обратиться к '{0}' свойству объекта класса '{1}'";
    public String ERR_CANNOT_REDECLARE_CLASS = "Класс {0} уже был объявлен ранее";
    public String ERR_CANNOT_REDECLARE_FUNCTION = "Функция {0} уже была объявлена ранее";
    public String ERR_CANNOT_REDECLARE_CONSTANT = "Константа {0} уже была объявлена ранее";
    public String ERR_INVALID_DECLARE_CONSTANT = "Некорректное имя для объявления константы '{0}'";
    public String ERR_CANNOT_IMPLEMENT = "{0} невозможно реализовать интерфейс {1}, т.к. это не интерфейс";
    public String ERR_CANNOT_EXTENDS = "{0} невозможно расширить классом {1}, т.к. это не класс";
    public String ERR_CANNOT_USE_NON_TRAIT = "{0} невозможно использовать трейт {1}, т.к. это не трейт";
    public String ERR_INTERFACE_NOT_FOUND = "Интерфейс '{0}' не найден";

    public String ERR_IMPLEMENT_METHOD =
            "Класс {0} содержит {1} абстрактный(е) метод(ы) и поэтому должен быть объявлен как абстрактный или реализовать все оставшиеся методы ({2})";

    public String ERR_INVALID_METHOD_SIGNATURE = "Объявление {0} должно быть совместимо с {1}";
    public String ERR_INTERFACE_FUNCTION_CANNOT_CONTAIN_BODY = "Методы интерфейса {0} не могут содержать тело с кодом";
    public String ERR_ACCESS_TYPE_FOR_INTERFACE_METHOD = "Модификатор доступа для метода интерфейса {0} должен быть опущен";
    public String ERR_CANNOT_MAKE_NON_STATIC_TO_STATIC = "Невозможно сделать нестатичный метод {0} статичным в классе {1}";
    public String ERR_CANNOT_MAKE_STATIC_TO_NON_STATIC = "Невозможно сделать статичным метод {0} нестатичным в классе {1}";
    public String ERR_CLASS_MAY_NOT_INHERIT_FINAL_CLASS = "Класс {0} не может быть унасследован от финального класса ({1})";
    public String ERR_CANNOT_OVERRIDE_FINAL_METHOD = "Невозможно переопределить финальный метод {0} методом {1}";
    public String ERR_NON_ABSTRACT_METHOD_MUST_CONTAIN_BODY = "Не абстрактный метод {0} должен содержать тело с кодом";
    public String ERR_ABSTRACT_METHOD_CANNOT_CONTAIN_BODY = "Абстрактный метод {0} не может содержать тело с кодом";
    public String ERR_CANNOT_USE_FINAL_ON_ABSTRACT = "Невозможно использовать final модификатор в абстрактных методах";

    public String ERR_CLASS_NOT_FOUND = "Класс '{0}' не найден";
    public String ERR_TRAIT_NOT_FOUND = "Трейт '{0}' не найден";
    public String ERR_FUNCTION_NOT_FOUND = "Функция '{0}' не найдена";
    public String ERR_CONSTANT_NOT_FOUND = "Константа '{0}' не найдена";
    public String ERR_METHOD_NOT_FOUND = "Метод {0}::{1}() не найден";
    public String ERR_MISSING_ARGUMENT = "Пропущенный параметр {0} для {1}()";
    public String ERR_EXPECT_LEAST_PARAMS = "{0}() ожидает не меньше {1} параметра(ов), {2} передано";
    public String ERR_EXPECT_EXACTLY_PARAMS = "{0}() ожидает ровно {1} параметр(а/ов), {2} передано";
    public String ERR_REQUIRE_FAILED = "{0}(): Неудачное открытие '{1}' для require";
    public String ERR_INCLUDE_FAILED = "{0}(): Неудачное открытие '{1}' для include";
    public String ERR_USE_UNDEFINED_CONSTANT = "Использование необъявленной константы {0} - предполагалось '{1}'";
    public String ERR_RETURN_NOT_REFERENCE = "Только ссылки на переменные должны быть возвращены по ссылке";
    public String ERR_YIELD_NOT_REFERENCE = "Только ссылки на переменные должны быть получены (yield) по ссылке";
    public String ERR_UNDEFINED_PROPERTY = "Необъявленное свойство: {0}::${1}";
    public String ERR_READONLY_PROPERTY = "Свойство {0}::${1} только для чтения";
    public String ERR_UNDEFINED_CLASS_CONSTANT = "Необъявленная константа класса '{0}'";
    public String ERR_INDIRECT_MODIFICATION_OVERLOADED_PROPERTY = "Косвенное изменение перегруженного свойства {0}::${1} не имеет никакого эффекта";

    public String ERR_OPERATOR_ACCEPTS_ONLY_POSITIVE = "'{0}' оператор принимает только положительные числа > 0";
    public String ERR_CANNOT_MIX_ARRAY_AND_LIST = "Невозможно использовать одновременно и [] и list()";
    public String ERR_CANNOT_MIX_KEYED_AND_UNKEYED_ARRAY_ENTRIES = "Невозможно смешивать ключевые и неключевые записи массива при объявлении";

    public String ERR_CANNOT_REDEFINE_CLASS_CONSTANT = "Невозможно заново объявить константу класса {0}";
    public String ERR_CANNOT_INHERIT_OVERRIDE_CONSTANT = "Невозможно наследовать ранее унаследованную или переопределенную константу {0} из интерфейса {1}";

    public String ERR_INTERFACE_MAY_NOT_INCLUDE_VARS = "Интерфейсы не могут включать в себя переменные и свойства";
    public String ERR_PACKAGE_CONSTANT_MUST_BE_NON_EMPTY_STRING = "Константа класса {0}::__PACKAGE__ должна быть не пустой строкой";
    public String ERR_PACKAGE_FILE_MUST_RETURN_ARRAY = "Невозможно использовать файл '{0}' для объявления пакета '{1}', он должен возвращать массив в виде [classes=>[], functions=>[], constants=>[]]";

    public String ERR_CANNOT_USE_SYSTEM_CLASS = "Невозможно использовать системный класс/интерфейс {0} для {1}";

    public String ERR_ACCESS_LEVEL_MUST_BE_PROTECTED_OR_WEAKER = "Уровень доступа {0}::${1} должен быть protected (как в классе {2}) или слабее";
    public String ERR_ACCESS_LEVEL_METHOD_MUST_BE_PROTECTED_OR_WEAKER = "Уровень доступа метода {0}::{1}() должен быть protected (как в классе {2}) или слабее";
    public String ERR_ACCESS_LEVEL_MUST_BE_PUBLIC = "Уровень доступа {0}::${1} должен быть public (как в классе {2})";
    public String ERR_ACCESS_LEVEL_METHOD_MUST_BE_PUBLIC = "Уровень доступа метода {0}::{1}() должен быть public (как в классе {2})";
    public String ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC = "Уровень доступа {0}::{1} должен быть (как в классе {2})";
    public String ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PUBLIC_FOR_INTERFACE = "Уровень доступа для константы интерфейса {0}::{1} должен быть public";
    public String ERR_ACCESS_LEVEL_CONSTANT_MUST_BE_PROTECTED = "Уровень доступа {0}::{1} должен быть protected (как в классе {2})";
    public String ERR_CANNOT_REDECLARE_STATIC_AS_NON_STATIC = "Невозможно сделать static {0}::${1} как не static {2}::${3}";
    public String ERR_CANNOT_REDECLARE_NON_STATIC_AS_STATIC = "Невозможно сделать не static {0}::${1} как static {2}::${3}";

    public String ERR_ACCESSING_STATIC_PROPERTY_AS_NON_STATIC = "Доступ к свойству {0}::${1} в нестатичном (static) виде";

    public String ERR_CANNOT_USE_OBJECT_AS_ARRAY = "Невозможно использовать объект {0} как массив";

    public String ERR_INVALID_ARRAY_ELEMENT_TYPE = "Все элементы массива должны быть экземплярами класса {0}, а не {1}";

    public String ERR_TRAIT_METHOD_COLLISION = "Метод трейта '{0}' не был применен, т.к. в {3} есть конфликт в {3} и {2}";
    public String ERR_TRAIT_SAME_PROPERTY = "{0} и {1} определяют одно и то же свойство (${2}) в составе {3}. Однако определение отличается и считается несовместимым. Класс был составлен";

    public String ERR_TRAIT_MULTIPLE_RULE =
            "Не удалось оценить приоритет трейта ({0}). Метод трейта '{1}' определен как исключаемый несколько раз";

    public String ERR_TRAIT_WAS_NOT_ADDED = "Требуемый трейт \"{0}\" не был добавлен к \"{1}\"";

    public String ERR_CANNOT_USE_EMPTY_LIST = "Невозможно использовать пустой список";

    public String ERR_YIELD_CAN_ONLY_INSIDE_FUNCTION = "Выражение \"yield\" может использоваться только внутри функции";

    public String ERR_RETURN_TYPE_INVALID = "Возвращаемое значение {0}() должно быть {1}, а не {2}";
    public String ERR_INVALID_UNICODE_ESCAPE_SEQUENCE = "Неверная escape-последовательность UTF-8";

    public String ERR_TIMEZONE_NULL_BYTE = "{0}: Часовой пояс не должен содержать нулевые байты";
    public String ERR_WRONG_PARAM_TYPE = "{0} ожидает, что параметр {1} будет {2}, а передан {3}";

    public String ERR_TYPED_PROP_INVALID_TYPE_OF_INHERITANCE = "Тип для {0}::${1} должен быть {2} (как в классе {3})";
    public String ERR_TYPED_PROP_TYPE_MUST_BE_NOT_DEFINED_OF_INHERITANCE = "Тип для {0}::${1} не должен быть определен (как в классе {2})";

    public String ERR_INVALID_TYPE_ARGUMENT_PASSED = "Аргумент {0}, переданный в {1}(), должен быть {2}, вызван в {3} в строке {4}, позиции {5} и определен";
    public String ERR_INVALID_SIMPLE_TYPE_ARGUMENT_PASSED = "Аргумент {0}, переданный в {1}(), должен быть заданного типа {2}, {3}, вызван в {4} в строке {5}, позиции {6} и определен";
    public String ERR_INVALID_RANGE_ARGUMENT_PASSED = "Аргумент {0}, переданный в {1}(), должен быть строкой, принадлежащей диапазону [{2}] в виде строки, вызываемой в {3} в строке {4}, позиции {5} и определен";

    public String ERR_INVALID_OBJ_ARGUMENT_PASSED = "Аргумент {0}, переданный в {1}(), должен {2}, а не {3}, вызван в {4} в строке {5}, позиции {6} и определен";
    public String ERR_INVALID_OBJ_ARGUMENT_PASSED_BE_INSTANCE = "быть экземпляром";
    public String ERR_INVALID_OBJ_ARGUMENT_PASSED_INTERFACE = "реализовывать интерфейс";
    public String ERR_INVALID_OBJ_ARGUMENT_PASSED_OR_NULL = "или null";

    public String ERR_ONLY_REF_CAN_BE_PASSED = "Только переменные могут быть переданы по ссылке";
    public String ERR_ONLY_ARR_AND_TRAV_CAN_UNPACKED = "Только массивы и Traversable значения могут быть распакованы";
    public String ERR_CANNOT_USE_POS_ARG_AFTER_UNPACK = "Невозможно использовать позиционный аргумент после распаковки аргумента";

    public String ERR_RETURN_DISALLOWED_MEMORY_AS_REF = "Невозможно вернуть {0} в качестве ссылки, jphp не будет поддерживать эту функцию";
    public String ERR_PASS_DISALLOWED_MEMORY_AS_REF = "Невозможно передать {0} в качестве аргумента-ссылки, jphp не будет поддерживать эту функцию";
    public String ERR_ASSIGN_BY_REF_FOR_TYPED_PROP = "Невозможно использовать ссылки для типизированного свойства {0}::${1}, jphp не будет поддерживать эту функцию";

    public String ERR_CANNOT_AUTO_INIT_ARR_IN_PROP = "Не удается автоматически инициализировать массив внутри свойства {0}::${1} типа {2}";
    public String ERR_ACCESS_TO_STATIC_PROPERTY_BEFORE_INIT = "Типизированное статическое свойство {0}::${1} не должно быть доступно до инициализации";
    public String ERR_ACCESS_TO_PROPERTY_BEFORE_INIT = "Типизированное свойство {0}::${1} не должно быть доступно до инициализации";

    public String ERR_CANNOT_ACCESS_PARENT_WHEN_SCOPE_NO_PARENT = "Невозможно обратиться к parent:: когда текущий контекст класса не имеет родителя (parent)";
    public String ERR_CANNOT_FIND_JAVA_CONSTRUCTOR = "Невозможно найти java конструктор {0}(Environment, ClassEntity)";
    public String ERR_CANNOT_INSTANT_ABSTRACT_CLASS = "Невозможно создать объект абстрактного класса {0}";
    public String ERR_CANNOT_INSTANT_INTERFACE = "Невозможно создать объект с помощью интерфейса {0}";
    public String ERR_CANNOT_INSTANT_TRAIT = "Невозможно создать объект с помощью трейта {0}";

    public String ERR_USING_THIS_NOT_IN_OBJECT_CONTEXT = "Использование $this вне контекста с объектом";

    @Override
    public String getLang() {
        return "ru";
    }
}
