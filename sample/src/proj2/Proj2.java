package proj2;

import proj2.ngc.NanoNgCompiler;

import java.nio.file.*;

public class Proj2 {

    public static void main(String[] args) throws Exception {
        //Path ts = args.length > 0 ? Paths.get(args[0]) : Paths.get("examples/basic.component.ts");
        //Path ts = args.length > 0 ? Paths.get(args[0]) : Paths.get("ErrorExample/err-unclosed.component.ts");
        Path tsFile = Paths.get("examples/add-product.component.ts");  // ملف TypeScript الخاص بك
        Path outDir = Paths.get("output"); // مسار الإخراج
        new NanoNgCompiler().compileToDir(tsFile, outDir);

        System.out.println("✔ Generated into: " + outDir.toAbsolutePath());
        System.out.println("  - index.html\n  - styles.css\n  - app.js");
    }
}
