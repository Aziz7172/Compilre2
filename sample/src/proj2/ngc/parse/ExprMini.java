package proj2.ngc.parse;

public class ExprMini {
    private final String s; private int i=0, n;
    public ExprMini(String src){ this.s=src==null? "": src; this.n=this.s.length(); }

    private void ws(){ while(i<n && Character.isWhitespace(s.charAt(i))) i++; }
    private boolean eat(char c){ ws(); if (i<n && s.charAt(i)==c){ i++; return true; } return false; }

    public Object eval(java.util.Map<String,Object> data){
        Object v = parseAdd(data);
        return v;
    }

    private Object parseAdd(java.util.Map<String,Object> data){
        Object v = parsePrimary(data);
        while(true){
            ws();
            if (eat('+')){
                Object r = parsePrimary(data);
                v = plus(v, r);
            } else break;
        }
        return v;
    }

    private Object parsePrimary(java.util.Map<String,Object> data){
        ws();
        if (i<n && (s.charAt(i)=='"' || s.charAt(i)=='\'')){
            return readString();
        }
        if (i<n && (Character.isDigit(s.charAt(i)))){
            return readNumber();
        }
        // identifier
        int j=i; while(i<n && (Character.isLetterOrDigit(s.charAt(i))||s.charAt(i)=='_'||s.charAt(i)=='$')) i++;
        String id = s.substring(j,i);
        if (id.length()>0 && data!=null && data.containsKey(id)) return data.get(id);
        return id; // fallback
    }

    private String readString(){
        char q = s.charAt(i++); StringBuilder b = new StringBuilder();
        while(i<n && s.charAt(i)!=q){ b.append(s.charAt(i++)); }
        if (i<n) i++;
        return b.toString();
    }
    private Double readNumber(){
        int j=i; while(i<n && (Character.isDigit(s.charAt(i))||s.charAt(i)=='.')) i++;
        return Double.valueOf(s.substring(j,i));
    }

    private Object plus(Object a, Object b){
        if (a instanceof Number && b instanceof Number){
            return ((Number)a).doubleValue() + ((Number)b).doubleValue();
        }
        return String.valueOf(a) + String.valueOf(b);
    }
}
