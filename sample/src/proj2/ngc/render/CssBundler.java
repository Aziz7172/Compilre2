package proj2.ngc.render;

import java.io.IOException;
import java.util.*;
import proj2.ngc.io.SourceLoader;

public class CssBundler {
    public String bundle(List<String> blocks, List<String> styleUrls, SourceLoader fs) throws IOException {
        StringBuilder b = new StringBuilder();

        // إضافة الأساليب المدمجة (inline styles)
        for (String s : blocks) {
            if (s == null) continue;
            b.append(s.trim()).append("\n\n");
        }

        // إضافة الأساليب من ملفات styleUrls
        for (String styleUrl : styleUrls) {
            String cssFileContent = fs.loadFile(styleUrl); // Assuming the styles are already read from the paths
            if (!cssFileContent.trim().isEmpty()) {
                b.append(cssFileContent).append("\n\n");
            }
        }

        return b.toString();
    }
}
