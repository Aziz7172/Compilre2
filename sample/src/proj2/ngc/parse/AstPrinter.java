package proj2.ngc.parse;

import proj2.ngc.model.*;
import java.util.*;

public class AstPrinter {

    private final String indentUnit;

    public AstPrinter() { this("  "); } // مسافة مزدوجة
    public AstPrinter(String indentUnit){ this.indentUnit = indentUnit; }

    public String print(Node root) {
        StringBuilder sb = new StringBuilder();
        if (!(root instanceof HtmlElementNode)) {
            return "(empty)";
        }
        printNode(root, 0, sb);
        return sb.toString();
    }

    private void printNode(Node n, int level, StringBuilder sb){
        String indent = repeat(indentUnit, level);
        if (n instanceof HtmlElementNode) {
            HtmlElementNode e = (HtmlElementNode)n;
            sb.append(indent).append("<").append(e.tag).append(">").append("\n");
            for (Node k : e.children) {
                printNode(k, level + 1, sb);
            }
            sb.append(indent).append("</").append(e.tag).append(">").append("\n");
        } else if (n instanceof TextNode) {
            TextNode t = (TextNode)n;
            String text = t.text.replace("\n", "\\n");
            sb.append(indent).append("\"").append(text).append("\"").append("\n");
        } else if (n instanceof InterpolationExpressionNode) {
            InterpolationExpressionNode i = (InterpolationExpressionNode)n;
            sb.append(indent).append("{{ ").append(i.expression.trim()).append(" }}").append("\n");
        }
    }

    private static String repeat(String s, int n){
        StringBuilder b = new StringBuilder(s.length() * n);
        for (int i = 0; i < n; i++) b.append(s);
        return b.toString();
    }
}
