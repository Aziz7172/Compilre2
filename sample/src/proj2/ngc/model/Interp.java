package proj2.ngc.model;

public class Interp implements Node {
    public final String expr; // raw expression text inside {{ ... }}
    public Interp(String e){ this.expr=e; }
}
