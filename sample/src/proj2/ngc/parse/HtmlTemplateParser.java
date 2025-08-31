package proj2.ngc.parse;

import proj2.ngc.model.*;
import java.util.*;
import java.util.regex.*;

public class HtmlTemplateParser {

    private static final Set<String> VOID_TAGS = new HashSet<>(Arrays.asList(
            "br","img","input","hr","meta","link","area","source","embed","param","track","col","wbr"
    ));

    public Node parse(String html) throws ParsingException {
        // فحص سريع لتوازن {{ }} على مستوى النص كله
        int opens = count(html, "{{");
        int closes = count(html, "}}");
        if (opens != closes) {
            throw new ParsingException("Unbalanced interpolation markers: found " + opens +
                    " '{{' vs " + closes + " '}}'.");
        }

        HtmlElementNode root = new HtmlElementNode("root");
        Deque<HtmlElementNode> stack = new ArrayDeque<>();
        stack.push(root);

        Pattern tagPattern = Pattern.compile("<(/?)([a-zA-Z][a-zA-Z0-9\\-]*)([^>]*)>", Pattern.DOTALL);
        Matcher matcher = tagPattern.matcher(html);
        int last = 0;

        while (matcher.find()) {
            // نص بين الوسوم
            if (matcher.start() > last) {
                String text = html.substring(last, matcher.start());
                addTextWithInterpolations(stack.peek(), text);
            }

            boolean closing = "/".equals(matcher.group(1));
            String name = matcher.group(2).toLowerCase(Locale.ROOT);
            String rest = matcher.group(3) == null ? "" : matcher.group(3);

            if (closing) {
                if (stack.size() == 1) {
                    throw new ParsingException("Unexpected closing tag </" + name + "> with no open tag.");
                }
                HtmlElementNode top = stack.peek();
                if (!top.tag.equalsIgnoreCase(name)) {
                    throw new ParsingException("Mismatched closing tag </" + name + ">; expected </" + top.tag + ">.");
                }
                stack.pop();
            } else {
                HtmlElementNode element = new HtmlElementNode(name);
                stack.peek().children.add(element);

                boolean selfClosing = rest.trim().endsWith("/");
                if (!selfClosing && !VOID_TAGS.contains(name)) {
                    stack.push(element);
                }
            }
            last = matcher.end();
        }

        // نص بعد آخر وسم
        if (last < html.length()) {
            addTextWithInterpolations(stack.peek(), html.substring(last));
        }

        // وسوم غير مُغلقة
        if (stack.size() > 1) {
            List<String> unclosed = new ArrayList<>();
            while (stack.size() > 1) unclosed.add(stack.pop().tag);
            Collections.reverse(unclosed);
            throw new ParsingException("Unclosed tags: " + String.join(", ", unclosed));
        }

        return root;
    }

    private void addTextWithInterpolations(HtmlElementNode parent, String text) {
        Pattern interpolationPattern = Pattern.compile("\\{\\{(.*?)\\}\\}", Pattern.DOTALL);
        Matcher m = interpolationPattern.matcher(text);

        int last = 0;
        while (m.find()) {
            if (m.start() > last) {
                String plain = text.substring(last, m.start());
                if (!plain.isEmpty()) parent.children.add(new TextNode(plain));
            }
            String expr = m.group(1);
            parent.children.add(new InterpolationExpressionNode(expr));
            last = m.end();
        }
        if (last < text.length()) {
            String tail = text.substring(last);
            if (!tail.isEmpty()) parent.children.add(new TextNode(tail));
        }
    }

    private static int count(String s, String sub) {
        int idx = 0, c = 0;
        while ((idx = s.indexOf(sub, idx)) != -1) { c++; idx += sub.length(); }
        return c;
    }
}
