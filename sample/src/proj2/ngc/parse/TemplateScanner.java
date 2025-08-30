package proj2.ngc.parse;

import proj2.ngc.model.*;

import java.util.*;
import java.util.regex.*;

public class TemplateScanner {

    public Node scan(String html){
        Elem root = new Elem("root");
        Deque<Elem> st = new ArrayDeque<Elem>();
        st.push(root);

        Pattern tag = Pattern.compile("<(/?)([a-zA-Z0-9\\-]+)[^>]*>");
        Matcher m = tag.matcher(html);
        int last = 0;

        while(m.find()){
            // text between tags
            if (m.start() > last){
                String text = html.substring(last, m.start());
                addTextWithInterpolations(st.peek(), text);
            }
            String slash = m.group(1);
            String name  = m.group(2);
            if ("/".equals(slash)){
                while(!st.isEmpty() && !st.peek().tag.equalsIgnoreCase(name)) st.pop();
                if (!st.isEmpty()) st.pop();
            } else {
                Elem el = new Elem(name);
                st.peek().kids.add(el);
                // naive: push unless void tag
                if (!isVoid(name)) st.push(el);
            }
            last = m.end();
        }
        if (last < html.length()){
            addTextWithInterpolations(st.peek(), html.substring(last));
        }
        return root;
    }

    private boolean isVoid(String n){
        String x = n.toLowerCase();
        return "img".equals(x)||"br".equals(x)||"hr".equals(x)||"meta".equals(x)||"link".equals(x)||"input".equals(x);
    }

   private void addTextWithInterpolations(Elem parent, String text){
    if (text == null || text.isEmpty()) return;

    // نهرّب الأقواس المعقوفة ونستخدم DOTALL لالتقاط أي محارف بين {{ و }}
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(
        "\\{\\{\\s*(.*?)\\s*\\}\\}",
        java.util.regex.Pattern.DOTALL
    );
    java.util.regex.Matcher m = p.matcher(text);

    int last = 0;
    while (m.find()){
        String pre = text.substring(last, m.start());
        if (!pre.isEmpty()) parent.kids.add(new Txt(pre));
        parent.kids.add(new Interp(m.group(1))); // التعبير داخل {{ ... }}
        last = m.end();
    }
    if (last < text.length()){
        parent.kids.add(new Txt(text.substring(last)));
    }
}

}
