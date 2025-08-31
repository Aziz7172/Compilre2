package proj2.ngc.render;

import proj2.ngc.model.*;
import proj2.ngc.parse.ExprMini;

import java.util.*;

public class HtmlEmitter {

    public String emit(Node root, Map<String, Object> data) {
        StringBuilder b = new StringBuilder();
        if (!(root instanceof HtmlElementNode)) return "";
        for (Node n : ((HtmlElementNode) root).children) {
            render(n, data, b);
        }
        return b.toString();
    }

    private void render(Node n, Map<String, Object> data, StringBuilder b) {
        if (n instanceof HtmlElementNode) {
            HtmlElementNode e = (HtmlElementNode) n;
            b.append("<").append(e.tag).append(">");
            for (Node k : e.children) {
                render(k, data, b);
            }
            b.append("</").append(e.tag).append(">");
        } else if (n instanceof TextNode) {
            b.append(((TextNode) n).text);
        } else if (n instanceof InterpolationExpressionNode) {
            Object v = new ExprMini(((InterpolationExpressionNode) n).expression).eval(data);
            b.append(escape(String.valueOf(v)));
        }
    }

    private String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
