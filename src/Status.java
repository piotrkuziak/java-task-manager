public enum Status
{
    TODO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String label;

    Status(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }
}
