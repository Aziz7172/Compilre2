package proj2.ngc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;

public class SourceLoader {
    private final Path base;

    public SourceLoader(Path baseDir) {
        this.base = baseDir == null ? Paths.get(".") : baseDir;
    }

    public String readOrEmpty(String rel) {
        try {
            Path p = base.resolve(rel).normalize();
            if (Files.exists(p)) return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading file: " + rel);
            e.printStackTrace();
        }
        return "";
    }

    public Optional<String> loadHtmlTemplate(Path tsFile) {
        // افترض أن القالب HTML موجود في نفس المجلد مع ملف الـ TypeScript
        Path htmlTemplatePath = tsFile.resolveSibling(tsFile.getFileName().toString().replace(".ts", ".html"));
        
        if (Files.exists(htmlTemplatePath)) {
            try {
                return Optional.of(new String(Files.readAllBytes(htmlTemplatePath), StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public String loadFile(String rel) throws IOException {
        Path filePath = base.resolve(rel).normalize();
        if (Files.exists(filePath)) {
            return new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        } else {
            throw new IOException("File not found: " + filePath.toString());
        }
    }
}
