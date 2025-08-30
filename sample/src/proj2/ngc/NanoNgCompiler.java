package proj2.ngc;

import proj2.ngc.io.SourceLoader;
import proj2.ngc.parse.HtmlTemplateParser;
import proj2.ngc.render.HtmlEmitter;
import proj2.ngc.render.CssBundler;
import proj2.ngc.render.DataEmitter;
import proj2.ngc.model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import proj2.ngc.parse.ParsingException;

public class NanoNgCompiler {

    public static class CompUnit {
        public String selector;
        public String template;  // inline content (if any)
        public String templateUrl; // external template path (if any)
        public List<String> styles = new ArrayList<String>();
        public List<String> styleUrls = new ArrayList<String>();
        public Map<String, Object> data = new LinkedHashMap<String, Object>();
        public String className;
    }

    public void compileToDir(Path tsFile, Path outDir) throws IOException {
        if (!Files.exists(tsFile)) {
            throw new IOException("Input TypeScript file not found: " + tsFile.toString());
        }

        // Assuming the SourceLoader and other methods exist to process TypeScript
        SourceLoader fs = new SourceLoader(tsFile.getParent());

        // Ensure loadHtmlTemplate() method exists or handle this logic
        String htmlContent = fs.loadHtmlTemplate(tsFile); // Updated: Pass the tsFile to load HTML template
        if (htmlContent == null || htmlContent.isEmpty()) {
            System.err.println("Error: HTML template could not be loaded.");
            return;
        }

        HtmlTemplateParser parser = new HtmlTemplateParser();
        try {
            Node ast = parser.parse(htmlContent);  // Parse the HTML content into AST
            // Process the AST further...
            HtmlEmitter emitter = new HtmlEmitter();
            String htmlOutput = emitter.emit(ast, new HashMap<String, Object>()); // Assuming data is passed here
            // Save the generated HTML to the output directory
            Files.write(outDir.resolve("index.html"), htmlOutput.getBytes(StandardCharsets.UTF_8));

        } catch (ParsingException e) {
            System.err.println("Error parsing HTML template: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Error writing output: " + e.getMessage());
            return;
        }

        // Add further processing logic for styles or other outputs (JS, etc.)
    }
}
