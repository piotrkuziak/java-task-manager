import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class JsonParser
{
    private static final Pattern[] PATTERNS = {
            Pattern.compile("\"id\"\\s*:\\s*(\\d+)"), // id
            Pattern.compile("\"description\"\\s*:\\s*\"([^\"]*)\""), // description
            Pattern.compile("\"status\"\\s*:\\s*\"([^\"]*)\""), // status
            Pattern.compile("\"Created At\"\\s*:\\s*\"([^\"]*)\""), // createdAt
            Pattern.compile("\"Updated At\"\\s*:\\s*\"([^\"]*)\"") // updatedAt
    };

    private JsonParser() {}

    private static <T> T parseField(Matcher matcher, String fieldName, Function<String, T> parser)
    {
        if (matcher.find())
            return parser.apply(matcher.group(1));

        throw new IllegalArgumentException(fieldName + " field missing or invalid");
    }

    private static <T> Optional<T> parseOptionalField(Matcher matcher, Function<String, T> parser)
    {
        if (matcher.find())
            return Optional.of(parser.apply(matcher.group(1)));

        return Optional.empty();
    }

    private static int parseId(Matcher matcher)
    {
        return JsonParser.parseField(matcher, "id", Integer::parseInt);
    }

    private static String parseDescription(Matcher matcher)
    {
        return JsonParser.parseField(matcher, "description", String::valueOf);
    }

    private static Status parseStatus(Matcher matcher)
    {
        return JsonParser.parseField(matcher, "status", Status::valueOf);
    }

    private static LocalDateTime parseCreatedAt(Matcher matcher)
    {
        return JsonParser.parseField(matcher, "createdAt", LocalDateTime::parse);
    }

    private static Optional<LocalDateTime> parseUpdatedAt(Matcher matcher)
    {
        return JsonParser.parseOptionalField(matcher, LocalDateTime::parse);
    }

    public static Task fromJsonString(final String jsonTaskString)
    {
        final Matcher[] matchers = Arrays.stream(JsonParser.PATTERNS)
                .map(pattern -> pattern.matcher(jsonTaskString))
                .toArray(Matcher[]::new);

        // Parse id
        final int id = JsonParser.parseId(matchers[0]);

        // Parse description
        final String description = JsonParser.parseDescription(matchers[1]);

        // Parse status
        final Status status = JsonParser.parseStatus(matchers[2]);

        // Parse createdAt
        final LocalDateTime createdAt = JsonParser.parseCreatedAt(matchers[3]);

        // Parse updatedAt
        final Optional<LocalDateTime> updatedAt = JsonParser.parseUpdatedAt(matchers[4]);

        return new Task(id, description, status, createdAt, updatedAt);
    }
}
