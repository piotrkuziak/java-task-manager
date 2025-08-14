import java.time.format.DateTimeFormatter;
import java.util.List;

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

        return String.format("id: %d, description: \"%s\", status: \"%s\", Created At: \"%s\", Updated At: \"%s\"",
                task.id(), task.description(), task.status().getLabel(), createdAtFormatted, updatedAtFormatted);
    }

    public static void displayTasksTable(List<Task> tasks)
    {
        // Table header
        System.out.printf("%-5s %-30s %-15s %-20s %-20s%n", "ID", "Description", "Status", "Created At", "Updated At");
        System.out.println("--------------------------------------------------------------------------------------");

        // Table rows
        tasks.stream()
                .map(task -> {
                    String createdAtFormatted = task.createdAt().format(FORMATTER);
                    String updatedAtFormatted = task.updatedAt()
                            .map(time -> time.format(FORMATTER))
                            .orElse("N/A");

                    return String.format("%-5d %-30s %-15s %-20s %-20s",
                            task.id(),
                            truncate(task.description()),
                            task.status().getLabel(),
                            createdAtFormatted,
                            updatedAtFormatted
                    );
                })
                .forEach(System.out::println);
    }

    // Helper to truncate long descriptions so table stays aligned
    private static String truncate(String text)
    {
        if (text.length() <= 30) return text;
        return text.substring(0, 30 - 3) + "...";
    }
}