package proj2.ngc.model;

public class InterpolationExpressionNode implements Node {
    public final String expression; // raw expression text inside {{ ... }}

    public InterpolationExpressionNode(String e) {
        this.expression = e;
    }
}
