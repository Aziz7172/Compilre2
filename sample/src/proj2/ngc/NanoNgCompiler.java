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
import proj2.ngc.parse.AstPrinter;

public class NanoNgCompiler {

    public static class CompUnit {
        public String selector;
        public String template;  // inline content (if any)
        public String templateUrl; // external template path (if any)
        public List<String> styles = new ArrayList<>();
        public List<String> styleUrls = new ArrayList<>();
        public Map<String, Object> data = new LinkedHashMap<>();
        public String className;
    }

    public void compileToDir(Path tsFile, Path outDir) throws IOException {
    if (!Files.exists(tsFile)) {
        throw new IOException("Input TypeScript file not found: " + tsFile.toString());
    }

    // Create SourceLoader
    SourceLoader fs = new SourceLoader(tsFile.getParent());

    // Load HTML template (get template from the Angular component)
    Optional<String> htmlContentOpt = fs.loadHtmlTemplate(tsFile);
    if (!htmlContentOpt.isPresent()) {
        System.err.println("Error: HTML template could not be loaded.");
        return;
    }
    String htmlContent = htmlContentOpt.get();

    // Parse the HTML template into an AST (Abstract Syntax Tree)
    HtmlTemplateParser parser = new HtmlTemplateParser();
    try {
        Node ast = parser.parse(htmlContent);  // Parse the HTML content into AST

        // Print AST for debugging (optional)
        AstPrinter printer = new AstPrinter();
        System.out.println("===== AST =====");
        System.out.println(printer.print(ast));
        System.out.println("================");

        // Dynamically generate the HTML content using the emitted AST
        HtmlEmitter emitter = new HtmlEmitter();
        String htmlOutput = emitter.emit(ast, new HashMap<String, Object>());

        // Ensure output directory exists
        if (!Files.exists(outDir)) {
            Files.createDirectories(outDir);
        }

        // Create and populate the CompUnit object
        CompUnit cu = new CompUnit();
        cu.className = "AddProductComponent"; // Example class name
        cu.data.put("name", "منتج جديد");
        cu.data.put("price", 100.0);
        cu.data.put("description", "وصف المنتج");

        // Add sample styles (CSS content for the component)
        cu.styles.add("body { font-family: Arial, sans-serif; padding: 20px; }");
        cu.styles.add("h1 { color: #333; }");
        cu.styleUrls.add("styles.css");

        // Add external CSS files (CSS contents for the component)
        cu.styleUrls.add("product-list.component.css");
        cu.styleUrls.add("product-detail.component.css");
        cu.styleUrls.add("add-product.component.css");

        // Generate index.html based on the parsed template (dynamic generation)
        String indexHtml = generateIndexHtml(htmlOutput);
        Files.write(outDir.resolve("index.html"), indexHtml.getBytes(StandardCharsets.UTF_8));

        // Generate details.html dynamically based on product data
        String detailsHtml = generateProductDetailsHtml(cu.data);
        Files.write(outDir.resolve("details.html"), detailsHtml.getBytes(StandardCharsets.UTF_8));

        // Generate CSS (from styles and styleUrls)
        CssBundler cssBundler = new CssBundler();
        StringBuilder cssOutput = new StringBuilder();

        // Add inline styles from `styles`
        for (String style : cu.styles) {
            cssOutput.append(style).append("\n\n");
        }

        // Add external styles from `styleUrls`
        for (String styleUrl : cu.styleUrls) {
            try {
                String cssFileContent = fs.loadFile(styleUrl); // Assuming the styles are already read from the paths
                if (!cssFileContent.trim().isEmpty()) {
                    cssOutput.append(cssFileContent).append("\n\n");
                }
            } catch (IOException e) {
                System.err.println("Warning: Could not load external CSS from: " + styleUrl + ", skipping.");
            }
        }

        // Write to styles.css
        Files.write(outDir.resolve("styles.css"), cssOutput.toString().getBytes(StandardCharsets.UTF_8));

        // Generate JS (from the data associated with the component)
        DataEmitter dataEmitter = new DataEmitter();
        String jsOutput = dataEmitter.emit(cu.data, cu.className == null ? "AppComponent" : cu.className);

        // Write to app.js
        Files.write(outDir.resolve("app.js"), jsOutput.getBytes(StandardCharsets.UTF_8));

    } catch (ParsingException e) {
        System.err.println("Error parsing HTML template: " + e.getMessage());
        return;
    } catch (IOException e) {
        System.err.println("Error writing output: " + e.getMessage());
        return;
    }
}

private String generateIndexHtml(String htmlContent) {
    StringBuilder indexHtml = new StringBuilder();
    indexHtml.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n")
             .append("    <meta charset=\"UTF-8\">\n")
             .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
             .append("    <title>Angular Generated Page</title>\n")
             .append("    <link rel=\"stylesheet\" href=\"styles.css\">\n")
             .append("</head>\n<body>\n")
             .append(htmlContent)  // This is where the Angular template content will go
             .append("\n<script src=\"app.js\"></script>\n")
             .append("</body>\n</html>");

    return indexHtml.toString();
}


    private String generateProductDetailsHtml(Map<String, Object> data) {
        StringBuilder detailsHtml = new StringBuilder();
        detailsHtml.append("<html><body><h1>تفاصيل المنتج</h1>");

        // Extract product details from data
        String productName = (String) data.getOrDefault("name", "اسم غير محدد");
        Double productPrice = (Double) data.getOrDefault("price", 0.0);
        String productDescription = (String) data.getOrDefault("description", "لا يوجد وصف للمنتج");

        // Add product details dynamically
        detailsHtml.append("<p>اسم المنتج: ").append(productName).append("</p>");
        detailsHtml.append("<p>السعر: ").append(productPrice).append(" ريال</p>");
        detailsHtml.append("<p>الوصف: ").append(productDescription).append("</p>");

        detailsHtml.append("</body></html>");
        return detailsHtml.toString();
    }
}
