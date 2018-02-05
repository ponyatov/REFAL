package org.refal.j;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;

public abstract class Convert {
/*
    static String toString (Object x) {
            if (x == null) {
                return "~";
            } else
            if (x instanceof Object[]) {
                return "("+toString((Object[])x)+")";
            } else
            if (x instanceof Reference) {
                return Reference.DEREFER+x.toString();
            } else {
                return x.toString();
            }
    }

    static String toString (Object[] x, int from, int to) {
        StringBuffer sb = new StringBuffer();
        for (int i = from; i < to; i++) {
            sb.append(toString(x[i]));
        }
        return sb.toString();
    }
*/

    static String toString (Object[] x) {
        return toString(x,0,x.length);
    }

    // Convert subexpression to string as for PRINT
    static String toString(Object[] a, int off, int len) {
        try {
        Writer w = new StringWriter();
        Stream.printExpr(w,a,off,len);
        return w.toString();
        } catch (IOException ex) {
            throw new java.lang.Error();
        }
    };

    // Convert term to string as for PRINT
    static String toString(Object a) {
        try {
            Writer w = new StringWriter();
            Stream.printTerm(w,a);
            return w.toString();
        } catch (IOException ex) {
            throw new java.lang.Error();
        }
    };

    // $ENTRY TO_CHARS
    public static final Object TO_CHARS = Term.getExternal("TO_CHARS",
        new org.refal.j.Function("Convert.TO_CHARS") {
          public Object[] eval(Object[] e) throws Exception { return TO_CHARS(e); }
        });

    static public Object[] TO_CHARS(Object[] a) throws IOException {
        String s = toString(a,0,a.length);
        Object res[] = new Object[s.length()];
        for (int i=0; i<res.length; i++)
            res[i] = new Character(s.charAt(i));
        return res;
        }

    // $ENTRY TO_WORD
    public static final Object TO_WORD = Term.getExternal("TO_WORD",
        new org.refal.j.Function("Convert.TO_WORD") {
          public Object[] eval(Object[] e) throws Exception { return TO_WORD(e); }
        });

    static public Object[] TO_WORD(Object[] a) throws IOException {
        return new Object[] {toString(a)};
        }

    // $ENTRY IMPLODE
    public static final Object IMPLODE = Term.getExternal("IMPLODE",
        new org.refal.j.Function("Convert.IMPLODE") {
          public Object[] eval(Object[] e) throws Exception { return IMPLODE(e); }
        });

    public static Object[] IMPLODE(Object[] a) {
        return new Object[] {toString(a)};
    }

    public static Object[] explode(String s) {
        Object[] r = new Object[s.length()];
        for (int i=0; i<s.length(); i++)
            r[i] = new Character(s.charAt(i));
        return r;
        }

    // $ENTRY EXPLODE
    public static final Object EXPLODE = Term.getExternal("EXPLODE",
        new org.refal.j.Function("Convert.EXPLODE") {
          public Object[] eval(Object[] e) throws Exception { return EXPLODE(e); }
        });

    public static Object[] EXPLODE(Object[] a) {
        return explode(toString(a));
    }

    // $ENTRY NUMB
    public static final Object NUMB = Term.getExternal("NUMB",
        new org.refal.j.Function("Convert.NUMB") {
          public Object[] eval(Object[] e) throws Exception { return NUMB(e); }
        });

    public static Object[] NUMB (Object[] a) {
        try {
            return new Object[] {Arithm.numberValueOf(toString(a))};
        } catch (Error e) { return null; }
    }

    // $ENTRY SYMB (== EXPLODE)
    public static final Object SYMB = Term.getExternal("SYMB",
        new org.refal.j.Function("Convert.SYMB") {
          public Object[] eval(Object[] e) throws Exception { return SYMB(e); }
        });

    public static Object[] SYMB (Object[] a) {
        return explode(toString(a));
    }

    // $ENTRY ORD
    public static final Object ORD = Term.getExternal("ORD",
        new org.refal.j.Function("Convert.ORD") {
          public Object[] eval(Object[] e) throws Exception { return ORD(e); }
        });

    public static Object[] ORD (Object[] a) {
            int la = a.length;
            Object[] res = new Object[la];
            for (int i=0; i<la; i++) {
                if (a[i] instanceof Character)
                    res[i] = new Integer ((int) ((Character)a[i]).charValue());
                else res[i] = a[i];
            }
            return res;
    }

    // $ENTRY CHR
    public static final Object CHR = Term.getExternal("CHR",
        new org.refal.j.Function("Convert.CHR") {
          public Object[] eval(Object[] e) throws Exception { return CHR(e); }
        });

    public static Object[] CHR (Object[] a) {
            int la = a.length;
            Object[] res = new Object[la];
            for (int i=0; i<la; i++) {
                if (a[i] instanceof Number)
                    res[i] = new Character ((char) ((Number)a[i]).intValue());
                else res[i] = a[i];
            }
            return res;
    }

    // $ENTRY UPPER
    public static final Object UPPER = Term.getExternal("UPPER",
        new org.refal.j.Function("Convert.UPPER") {
          public Object[] eval(Object[] e) throws Exception { return UPPER(e); }
        });

    public static Object[] UPPER (Object[] a) {
            int la = a.length;
            Object[] res = new Object[la];
            for (int i=0; i<la; i++) {
                if (a[i] instanceof Character)
                    res[i] = new Character(Character.toUpperCase(((Character)a[i]).charValue()));
                else res[i] = a[i];
            }
            return res;
    }

    // $ENTRY LOWER
    public static final Object LOWER = Term.getExternal("LOWER",
        new org.refal.j.Function("Convert.LOWER") {
          public Object[] eval(Object[] e) throws Exception { return LOWER(e); }
        });

    public static Object[] LOWER (Object[] a) {
            int la = a.length;
            Object[] res = new Object[la];
            for (int i=0; i<la; i++) {
                if (a[i] instanceof Character)
                    res[i] = new Character(Character.toLowerCase(((Character)a[i]).charValue()));
                else res[i] = a[i];
            }
            return res;
    }

}
