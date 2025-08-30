package proj2;

import proj2.ngc.NanoNgCompiler;

import java.nio.file.*;

public class Proj2 {
    public static void main(String[] args) throws Exception {
        Path ts = args.length > 0 ? Paths.get(args[0]) : Paths.get("examples/basic.component.ts");
        Path out = args.length > 1 ? Paths.get(args[1]) : Paths.get("build2");

        new NanoNgCompiler().compileToDir(ts, out);

        System.out.println("✔ Generated into: " + out.toAbsolutePath());
        System.out.println("  - index.html\n  - styles.css\n  - app.js");
    }
}
