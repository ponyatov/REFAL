package org.refal.j;

public class Chars extends Container {
    protected char[] value;
    protected int length; // length <= expr.length
    protected boolean shared; // cloning is needed for modification

    public Chars() {
        init(EMPTY);
    }

    public Chars(Object[] expr) {
        init(expr);
    }

    public Chars(int size) {
        value = new char[size];
        length=0;
        shared=false;
        }

    public Chars(String s) {
        try {
            set(s);
        } catch (Error e) {} // never occurs
    }

/****************** Creation "interface" *********************/

    public static Object makeObject(String mode) {
        if (mode=="STRING")  {
            return new Chars();
        } else return null;
    }

    public static Referable readObject(String header, java.io.PushbackReader in, Table t)
                                    throws Exception
    {
        if (header.length()==0) {
            int c = in.read();
            switch (c) {
                case '\"': return new Chars (Stream.readQuote(in,c));
                default: in.unread(c);
            }
        } else
        if (header.charAt(0)=='S') {
            Container cont = new Chars();
            if (cont.readBody(in,t))  // defined in Container
                return cont;
        }
        return null;
    }

/*************************/

    public void writeObject(java.io.Writer out, Table t) throws java.io.IOException {
        out.write('\"');
        for (int i=0; i<length; i++)
            Stream.writeChar(out, value[i]);
        out.write('\"');
    }

/******************************************************************/

    public Object clone() throws CloneNotSupportedException {
        shared = true;
        return super.clone();
    }

    public String getMode() {
        return "STRING";
    }

    public void set(Object v) throws Error {
        if (v instanceof String) {
            value = ((String)v).toCharArray();
            length = value.length;
            shared = false;
        }
        else throw new Error("Improper object value "+v+" for mode STRING");
    }

    public Object get() {
        return new String(value,0,length);
    }

    public void init(Object[] a) {
        length = a.length;
        value = new char[length];
        for (int i=0; i<length; i++)
            isetn(i,a[i]);
        shared = false;
    }

    // Make it unshared
    protected final void copy() {
        value = (char[]) value.clone();
        shared = false;
    }

    // Virtual methods from Container:
    public Object[] igetv() {
        Object[] res = new Object[length];
        for (int i=0; i<length; i++)
            res[i] = new Character(value[i]);
        return res;
        }

    Object igetn(int pos) { // null if pos>=length
//        if (pos<0) pos = length+1+pos;
        if(pos<length && pos>=0)
            return new Character(value[pos]);
        else
            return null;
    }

    void   isetn(int pos, Object a) {
//        if (pos<0) pos = length+1+pos;
        if (pos>=length) isetlen(pos+1);
        if (pos >=0) {
            if (shared) copy();
            value[pos]=(a instanceof Character? ((Character)a).charValue() : BLANK_CHAR);
            }
        }

    int    igetlen() {
        return length;
        }

    void   isetlen(int len) {          // fill new cells with BLANKs
        if (len>value.length) {
            char[] newvalue = new char[(shared? len : len+value.length)]; // no extra room if shared
            System.arraycopy(value,0,newvalue,0,length);
            value = newvalue;
            shared = false;
        }
        else    if (shared) copy();
        for (int i=length; i<len; i++) {
            value[i] = BLANK_CHAR;
        }
        length=len;
    }

    void ishift(int pos, int shift) { // fill new cells with BLANKs
//        if (pos<0) pos = length+pos+1;
        if (pos<0) pos = 0;
        if (pos>length) isetlen(pos); // now pos<=length
        int lentail = length-pos; // >=0
        if (shift>0) {
                isetlen(length+shift);
                if(shared)copy(); // for sure
                System.arraycopy(value,pos,value,pos+shift,lentail);
                for (int i=pos; i<pos+shift; i++) {
                    value[i] = BLANK_CHAR;
                }
        } else {
            if (lentail+shift>0) {
                if(shared)copy();
                System.arraycopy(value,pos-shift,value,pos,lentail+shift);
                isetlen(length+shift);
            } else {
                isetlen(pos);
            }
        }
    }
}