package com.cumulocity.me.agent.smartrest.model.template;

import com.cumulocity.me.util.StringUtils;

import java.util.Vector;

public class Path {

    public static final class PathBuilder implements Supplier {
        private Vector path = new Vector();

        public PathBuilder add(String part) {
            path.addElement(part);
            return this;
        }

        public String get() {
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < path.size(); i++) {
                final String part = (String) path.elementAt(i);
                sb.append(part).append("/");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
    }

    public static PathBuilder path() {
        return new PathBuilder();
    }

    public static PathBuilder path(String string) {
        final PathBuilder result = path();
        final String[] split = StringUtils.split(string, "/");
        for (int i = 0; i < split.length; i++) {
            String part = split[i];
            result.add(part);
        }
        return result;
    }
}
