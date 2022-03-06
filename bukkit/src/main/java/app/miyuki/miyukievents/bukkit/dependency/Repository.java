package app.miyuki.miyukievents.bukkit.dependency;

import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Getter;
import lombok.val;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@AllArgsConstructor
@Getter
public enum Repository {

    MAVEN_CENTRAL("https://repo1.maven.org/maven2/"),
    CODE_MC("https://repo.codemc.org/repository/maven-public/"),
    SONATYPE("https://oss.sonatype.org/content/repositories/snapshots/");

    private final String url;

    public byte[] downloadRaw(Dependency dependency) throws DependencyDownloadException {
        try {
            val url = new URL(this.url + dependency.getMavenRepositoryPath());
            URLConnection connection = url.openConnection();

            @Cleanup val inputStream = connection.getInputStream();

            val bytes = ByteStreams.toByteArray(inputStream);

            if (bytes.length == 0) {
                throw new DependencyDownloadException("Empty stream");
            }

            return bytes;
        } catch (Exception exception) {
            throw new DependencyDownloadException(exception);
        }
    }

    public byte[] download(Dependency dependency) throws DependencyDownloadException, NoSuchAlgorithmException {
        val bytes = downloadRaw(dependency);

        val messageDigest = MessageDigest.getInstance("SHA-256");

        val checksum = Base64.getEncoder().encodeToString(messageDigest.digest(bytes));

        if (!dependency.validateChecksum(checksum)) {
            throw new DependencyDownloadException("Dependency " + dependency.name() + " has an invalid hash. " +
                    "Expected: " + checksum + " " +
                    "Actual: " + dependency.getChecksum());
        }

        return bytes;
    }

    public void download(Dependency dependency, Path file) throws DependencyDownloadException {
        try {
            Files.write(file, download(dependency));
        } catch (IOException | NoSuchAlgorithmException exception) {
            throw new DependencyDownloadException(exception);
        }
    }


}
