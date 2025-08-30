package proj2.ngc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class SourceLoader {
    private final Path base;
    public SourceLoader(Path baseDir){ this.base = baseDir == null? Paths.get(".") : baseDir; }

    public String readOrEmpty(String rel){
        try {
            Path p = base.resolve(rel).normalize();
            if (Files.exists(p)) return new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
        } catch (Exception ignored){}
        return "";
    }
    public String loadHtmlTemplate(Path tsFile) throws IOException {
        // افترض أن القالب HTML موجود في نفس المجلد مع ملف الـ TypeScript
        Path htmlTemplatePath = tsFile.resolveSibling("template.html"); // أو أي مسار آخر يعتمد على بنية المشروع
        return new String(Files.readAllBytes(htmlTemplatePath));
    }
}
