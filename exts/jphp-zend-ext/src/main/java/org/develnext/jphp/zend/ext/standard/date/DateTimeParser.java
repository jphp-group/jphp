package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_OF_YEAR;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_dd;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_hh;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_ii;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_mm;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_ss;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK;
import static org.develnext.jphp.zend.ext.standard.date.Token.EOF;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class DateTimeParser {
    private final static EnumMap<Symbol, List<GroupNode>> parseTree = new EnumMap<>(Symbol.class);

    private static final Year4 YEAR_4_DIGIT = Year4.of();
    private static final SymbolNode MINUS_NODE = SymbolNode.of(Symbol.MINUS);
    private static final CharacterNode ISO_WEEK_NODE = CharacterNode.of('W');

    private static final SymbolNode COLON_NODE = SymbolNode.of(Symbol.COLON);
    private static final GroupNode EXIF = GroupNode.of(
            "EXIF",
            YEAR_4_DIGIT,
            COLON_NODE,
            Month2.of(),
            COLON_NODE,
            Day2.of(),
            SymbolNode.of(Symbol.SPACE),
            Hour24.of(),
            COLON_NODE,
            Minute2.of(),
            COLON_NODE,
            Second2.of()
    );

    private static final GroupNode ISOYearWeek = GroupNode.of(
            "ISO year with ISO week",
            YEAR_4_DIGIT,
            OrNode.of(
                    ISO_WEEK_NODE,
                    AndNode.of(MINUS_NODE, ISO_WEEK_NODE)
            ),
            PatternNode.ofDigits(WEEK)
    );
    private static final GroupNode MySQL = GroupNode.of(
            "MySQL",
            YEAR_4_DIGIT,
            MINUS_NODE

    );

    private static final GroupNode PostgreSQLYearWithDayOfYear = GroupNode.of(
            "PostgreSQL: Year with day-of-year",
            OrNode.of(
                    AndNode.of(
                            YEAR_4_DIGIT,
                            OrNode.of(
                                    OrNode.of(SymbolNode.of(Symbol.DOT), PatternNode.ofDigits(DAY_OF_YEAR)),
                                    PatternNode.ofDigits(DAY_OF_YEAR)
                            )
                    ),
                    PatternNode.ofDigits(Pattern.compile("[0-9]{4}(00[1-9]|0[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])"))
            )
    );

    private static final GroupNode COMPOUND_ROOT = GroupNode.of(
            "Compound Root",
            YEAR_4_DIGIT,
            MINUS_NODE,
            OrNode.of(
                    GroupNode.of("WDDX",
                            PatternNode.ofDigits(MONTH_mm),
                            MINUS_NODE,
                            PatternNode.ofDigits(DAY_dd),
                            CharacterNode.of('T'),
                            PatternNode.ofDigits(HOUR_hh),
                            COLON_NODE,
                            PatternNode.ofDigits(MINUTE_ii),
                            COLON_NODE,
                            PatternNode.ofDigits(SECOND_ss)
                    ),
                    GroupNode.of(
                            "MYSQL", Month2.of(), MINUS_NODE, Day2.of(), SymbolNode.of(Symbol.SPACE), Hour24.of(), COLON_NODE, Minute2.of(), COLON_NODE, Second2.of()
                    )
            )
    );
    private static final GroupNode XMLRPC = GroupNode.of(
            "XMLRPC",
            YearMonthDay.of(),
            CharacterNode.of('T'),
            OrNode.of(Hour12.of(), Hour24.of()),
            COLON_NODE,
            Minute2.of(),
            COLON_NODE,
            Second2.of()
    );

    private static final GroupNode XMLRPC_COMPACT = GroupNode.of(
            "XMLRPC Compact",
            YearMonthDay.of(),
            CharacterNode.ofCaseInsensitive('t'),
            HourMinuteSecond.of(5, 6)
    );

    private static final GroupNode WDDX = GroupNode.of(
            "WDDX",
            YEAR_4_DIGIT

    );

    private static final GroupNode UNIX_TIMESTAMP = GroupNode.of(
            "Unix Timestamp",
            SymbolNode.of(Symbol.AT),
            SymbolNode.of(Symbol.DIGITS)
    );

    static {
        parseTree.put(Symbol.AT, Arrays.asList(UNIX_TIMESTAMP));

        parseTree.put(Symbol.DIGITS, Arrays.asList(
                // Localized Compound formats
                COMPOUND_ROOT
        ));
    }

    private final DateTimeTokenizer tokenizer;

    DateTimeParser(String dateTime) {
        this.tokenizer = new DateTimeTokenizer(dateTime);
    }

    public static List<Token> tokenize(final String time) {
        List<Token> tokens = new ArrayList<>();
        DateTimeTokenizer tokenizer = new DateTimeTokenizer(time);

        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);

        return tokens;
    }

    private static Token pool(LinkedList<Token> tokens) {
        Token token = tokens.poll();
        return token == null ? EOF : token;
    }

    private static Token peek(LinkedList<Token> tokens) {
        Token token = tokens.peek();
        return token == null ? EOF : token;
    }

    public ZonedDateTime parse() {
        LinkedList<Token> tokens = new LinkedList<>();

        ZonedDateTime date = ZonedDateTime.now();
        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);

        for (GroupNode nodes : parseTree.get(peek(tokens).symbol())) {
            Cursor cursor = new Cursor();
            boolean matches = nodes.matches(tokens, cursor, tokenizer);

            if (matches)
                break;
        }

        return date;
    }

}
