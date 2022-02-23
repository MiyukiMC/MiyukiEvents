package app.miyuki.miyukievents.bukkit.dependency;

public class DependencyDownloadException extends Exception {

    public DependencyDownloadException(String message) {
        super(message);
    }

    public DependencyDownloadException(Throwable cause) {
        super(cause);
    }

}
