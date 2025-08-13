import java.time.format.DateTimeFormatter;

public final class TaskFormatter
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private TaskFormatter() {}

    public static String taskToDisplayString(final Task task)
    {
        final String createdAtFormatted = task.createdAt().format(FORMATTER);
        final String updatedAtFormatted = task.updatedAt()
                .map(time -> time.format(FORMATTER))
                .orElse("N/A");

        return String.format("{\n\tid: %d,\n\tdescription: \"%s\",\n\tstatus: \"%s\",\n\tCreated At: \"%s\",\n\tUpdated At: \"%s\"\n}",
                task.id(), task.description(), task.status().getLabel(), createdAtFormatted, updatedAtFormatted);
    }
}
