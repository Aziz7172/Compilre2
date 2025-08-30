package proj2.ngc.model;

import java.util.*;

public class HtmlElementNode implements Node {
    public final String tag;
    public final List<Node> children = new ArrayList<Node>();
    public final Map<String, String> attributes = new HashMap<String, String>();  // For CSS or other attributes

    public HtmlElementNode(String t) {
        this.tag = t;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }
}
