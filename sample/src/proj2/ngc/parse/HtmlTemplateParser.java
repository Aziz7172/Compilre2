package proj2.ngc.parse;

import proj2.ngc.model.*;

import java.util.*;
import java.util.regex.*;

public class HtmlTemplateParser {

    public Node parse(String html) throws ParsingException {
        HtmlElementNode root = new HtmlElementNode("root");
        Deque<HtmlElementNode> stack = new ArrayDeque<HtmlElementNode>();
        stack.push(root);

        Pattern tagPattern = Pattern.compile("<(/?)([a-zA-Z0-9\\-]+)[^>]*>");
        Matcher matcher = tagPattern.matcher(html);
        int last = 0;

        while (matcher.find()) {
            // Handle text between tags
            if (matcher.start() > last) {
                String text = html.substring(last, matcher.start());
                addTextWithInterpolations(stack.peek(), text);
            }

            String slash = matcher.group(1);
            String name = matcher.group(2);

            if ("/".equals(slash)) {
                // Closing tag, pop from stack
                while (!stack.isEmpty() && !stack.peek().tag.equalsIgnoreCase(name)) {
                    stack.pop();
                }
                if (!stack.isEmpty()) stack.pop();
            } else {
                // Opening tag, add to stack
                HtmlElementNode element = new HtmlElementNode(name);
                stack.peek().children.add(element);
                stack.push(element);
            }
            last = matcher.end();
        }

        // Handle remaining text after last tag
        if (last < html.length()) {
            addTextWithInterpolations(stack.peek(), html.substring(last));
        }

        return root;
    }

    private void addTextWithInterpolations(HtmlElementNode parent, String text) {
        // Split the text by interpolation markers ({{...}})
        Pattern interpolationPattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        Matcher interpolationMatcher = interpolationPattern.matcher(text);

        int lastIdx = 0;
        while (interpolationMatcher.find()) {
            // Add plain text before interpolation
            if (interpolationMatcher.start() > lastIdx) {
                String plainText = text.substring(lastIdx, interpolationMatcher.start());
                parent.children.add(new TextNode(plainText));
            }

            // Add interpolation expression node
            String expr = interpolationMatcher.group(1);
            parent.children.add(new InterpolationExpressionNode(expr));
            lastIdx = interpolationMatcher.end();
        }

        // Add any remaining plain text after the last interpolation
        if (lastIdx < text.length()) {
            parent.children.add(new TextNode(text.substring(lastIdx)));
        }
    }
}
