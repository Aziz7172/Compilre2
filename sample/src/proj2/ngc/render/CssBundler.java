package proj2.ngc.render;

import java.util.*;

public class CssBundler {
    public String bundle(List<String> blocks){
        StringBuilder b = new StringBuilder();
        for (String s : blocks){
            if (s==null) continue;
            b.append(s.trim()).append("\n\n");
        }
        return b.toString();
    }
}
