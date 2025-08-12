public final class JsonFormatter
{
    private JsonFormatter() {}

    public static String taskToJson(Task task)
    {
        final String updatedAtStr = task.updatedAt()
                .map(String::valueOf)
                .orElse("null");

        return String.format("\t{\n\t\t\"id\": %d,\n\t\t\"description\": \"%s\",\n\t\t\"status\": \"%s\",\n\t\t\"Created At\": \"%s\",\n\t\t\"Updated At\": \"%s\"\n\t}",
                task.id(), task.description(), task.status(), task.createdAt(), updatedAtStr);
    }
}