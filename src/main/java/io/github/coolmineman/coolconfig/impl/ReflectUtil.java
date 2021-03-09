package io.github.coolmineman.coolconfig.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ReflectUtil {
    private ReflectUtil() { }

    private static class MethodOffset implements Comparable<MethodOffset> {
        MethodOffset(Method method, int offset) {
            this.method = method;
            this.offset = offset;
        }

        @Override
        public int compareTo(MethodOffset target) {
            return offset - target.offset;
        }

        Method method;
        int offset;
    }

    // Based on the aproach taken here: https://github.com/wmacevoy/kiss/blob/master/src/main/java/kiss/util/Reflect.java#L122-L201
    // Your guess is as good as mine
    public static Method[] getDeclaredMethodsInOrder(Class<?> clazz) {
        Method[] methods = null;
        try {
            String resource = clazz.getName().replace('.', '/') + ".class";

            methods = clazz.getDeclaredMethods();

            InputStream is = clazz.getClassLoader().getResourceAsStream(resource);

            if (is == null) {
                return methods;
            }

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] streamData = new byte[1024];
            while ((nRead = is.read(streamData, 0, streamData.length)) != -1) {
                buffer.write(streamData, 0, nRead);
            }
            // buffer.flush();
            byte[] data = buffer.toByteArray();

            Arrays.sort(methods, (a, b) -> b.getName().length() - a.getName().length());

            String sdata = new String(data, StandardCharsets.UTF_8);
            int lnt = sdata.indexOf("LineNumberTable");
            if (lnt < 0) throw new RuntimeException("Expected LineNumberTable Attribute");
            int cde = sdata.lastIndexOf("SourceFile");
            if (cde < 0) throw new RuntimeException("Expected SourceFile Attribute");
            sdata = sdata.substring(lnt + "LineNumberTable".length() + 3, cde);
            
            MethodOffset mo[] = new MethodOffset[methods.length];

            // Why does this work?
            for (int i = 0; i < methods.length; ++i) {
                int pos = -1;
                for (;;) {
                    pos = sdata.indexOf(methods[i].getName(),pos);
                    if (pos == -1) break;
                    boolean subset = false;
                    for (int j = 0; j < i; ++j) {
                        if (mo[j].offset >= 0 && mo[j].offset <= pos && pos < mo[j].offset + mo[j].method.getName().length()) {
                            subset = true;
                            break;
                        }
                    }
                    if (subset) {
                        pos += methods[i].getName().length();
                    } else {
                        break;
                    }
                }
                mo[i] = new MethodOffset(methods[i],pos);
            }

            Arrays.sort(mo);
            for (int i=0; i<mo.length; ++i) {
                methods[i]=mo[i].method;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return methods;
    }
}
