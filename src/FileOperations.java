import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileOperations
{
    private FileOperations() {}

    public static void createFileIfNotExists(final Path filePath)
    {
        if (!Files.exists(filePath))
        {
            try
            {
                Files.createFile(filePath);
                Files.writeString(filePath, "{}");
            }
            catch (IOException e)
            {
                throw new RuntimeException("Failed to create file: " + filePath, e);
            }
        }
    }

    public static String readFile(final Path filePath)
    {
        try
        {
            return Files.readString(filePath);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    public static void writeFile(final Path filePath, final String content)
    {
        try
        {
            Files.writeString(filePath, content);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }
}
