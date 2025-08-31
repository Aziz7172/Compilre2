package proj2.ngc.model;

import java.util.*;

public class Elem implements Node {
    public final String tag;
    public final List<Node> kids = new ArrayList<Node>();
    public Elem(String t){ this.tag=t; }
}
