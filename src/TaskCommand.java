import java.util.Optional;

public sealed interface TaskCommand permits
        TaskCommand.CommandAdd,
        TaskCommand.CommandUpdateDescription,
        TaskCommand.CommandDelete,
        TaskCommand.CommandMarkStatus,
        TaskCommand.CommandList
{
    record CommandAdd(String description) implements TaskCommand {}

    record CommandUpdateDescription(int id, String description) implements TaskCommand {}

    record CommandDelete(int id) implements TaskCommand {}

    record CommandMarkStatus(int id, Status status) implements TaskCommand {}

    record CommandList(Optional<Status> status) implements TaskCommand {}
}
