package org.refal.j;

public class Real implements Referable, Writable {

    protected double value;

    public Real() {
        try {
            init(EMPTY);
        } catch (Error e) {} // Never occurs
    }

    public Real(Object[] expr) {
        try {
            init(expr);
        } catch (Error e) {} // Never occurs
    }

/****************** Creation "interface" *********************/

    public static Object makeObject(String mode) {
        if (mode=="REAL")  {
            return new Real();
        } else return null;
    }

    public static Referable readObject(String header, java.io.PushbackReader in, Table t)
                                    throws Exception
    {
/* -- This functionality is already present in Mask
        if (header.length()==0) {
            int c = in.read();
            in.unread(c);
            if (Character.isDigit((char)c) || c=='-' || c == '+') {
                Number number = Stream.readNumber(in);
                Referable res = (number instanceof Double? (Referable) new Real() : (Referable) new Mask());
                res.set(number);
                return res;
            }
        }
*/
        return null;
    }

/******************************************************************/

    public void writeObject(java.io.Writer out, Table t) throws java.io.IOException {
        out.write(String.valueOf(value));
    }

/******************************************************************/

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMode() {
        return "REAL";
    }

    public void set(Object v) throws Error {
        if (v instanceof Number)
            value = ((Number)v).doubleValue();
        else throw new Error("Improper object value "+v+" for mode REAL");
    }

    public Object get() {
        return new Double(value);
    }

    public void init(Object[] a) throws Error {
        if (a.length==0) { value = 0.0; return; }
        if (a.length==1) {
            Object x = Arithm.getNumber(a[0]);
            if (x instanceof Number) {
                value = ((Number)x).doubleValue();
                return;
            }
        }
        throw new Error("Illegal initial expression for Real");
    }

    // $ENTRY REAL
    public static final Object REAL = Term.getExternal("REAL",
        new org.refal.j.Function("Real.REAL") {
          public Object[] eval(Object[] e) throws Exception { return REAL(e); }
        });

    public static Object[] REAL(Object[] a) {
        if (!(a.length==1)) return null;
        Object r = toReal(a[0]);
        if (r==null) return null;
        return new Object[] {r};
    }

    public static Object toReal(Object a) {
        Number x = Arithm.getNumber(a);
        if (x==null) return null;
        if ((x instanceof Double) ||(x instanceof Float)) return x;
        return new Double(x.doubleValue());
    }

    // $ENTRY INT
    public static final Object INT = Term.getExternal("INT",
        new org.refal.j.Function("Real.INT") {
          public Object[] eval(Object[] e) throws Exception { return INT(e); }
        });

    public static Object[] INT(Object[] a) {
        if (!(a.length==1)) return null;
        Object r = toInt(a[0]);
        if (r==null) return null;
        return new Object[] {r};
    }

    public static Object toInt(Object a) {
        Number x = Arithm.getNumber(a);
        if ((x instanceof Double) || (x instanceof Float))
            return Arithm.norm_long(x.longValue());
        return x;
    }

}
