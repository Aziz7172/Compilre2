package proj2.ngc.render;

import java.util.*;

public class DataEmitter {
    public String emit(Map<String, Object> map, String comp) {
        StringBuilder b = new StringBuilder();
        b.append("(function(){\n");
        b.append("  window.APP2 = window.APP2 || {};\n");
        b.append("  APP2.component = '").append(comp).append("';\n");
        b.append("  APP2.data = ").append(toJson(map)).append(";\n");
        b.append("})();\n");
        return b.toString();
    }

    private String toJson(Object v) {
        if (v == null) return "null";
        if (v instanceof String) {
            String s = (String) v;
            return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
        }
        if (v instanceof Number || v instanceof Boolean) return String.valueOf(v);
        if (v instanceof Map) {
            StringBuilder b = new StringBuilder("{");
            boolean first = true;
            for (Object e : ((Map) v).entrySet()) {
                Map.Entry en = (Map.Entry) e;
                if (!first) b.append(",");
                first = false;
                b.append(toJson(String.valueOf(en.getKey()))).append(":").append(toJson(en.getValue()));
            }
            return b.append("}").toString();
        }
        if (v instanceof Iterable) {
            StringBuilder b = new StringBuilder("[");
            boolean first = true;
            for (Object x : (Iterable) v) {
                if (!first) b.append(",");
                first = false;
                b.append(toJson(x));
            }
            return b.append("]").toString();
        }
        return toJson(String.valueOf(v));
    }
}
