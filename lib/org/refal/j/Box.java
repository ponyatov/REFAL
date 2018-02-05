package org.refal.j;

public class Box extends Container {
    protected Object[] expr;
    protected int length; // length <= expr.length
    protected boolean shared; // cloning is needed for modification
    protected String mode="CHAIN"; // 'CHAIN' or 'VECTOR'

    //static { System.out.println("Start initializing Box"); }

    public Box() {
        init(EMPTY);
    }

    public Box(Object[] expr) {
        init(expr);
    }

    public Box(int size) {
        expr = new Object[size];
        length=0;
        shared=false;
        }

/****************** Creation "interface" *********************/

    public static Object makeObject(String mode) {
        if (mode=="BOX" || mode=="CHAIN" || mode=="VECTOR")  {
            return new Box().setMode(mode);
        } else return null;
    }

    public static Referable readObject(String header, java.io.PushbackReader in, Table t)
                                    throws Exception
    {
//        try {
            if (header.length()==0
            || (header.charAt(0)=='C')) {
                int size = (header.length()<=1? 0 : Integer.parseInt(header.substring(1)));
                Container cont = new Box(size);
                if (cont.readBody(in,t))  // defined in Container
                    return cont;
            }
//        } catch (NumberFormatException e) {}
        return null;
    }

/*************************/

    public void writeObject(java.io.Writer out, Table t) throws java.io.IOException {
        //out.write('C');
        //out.write(String.valueOf(igetlen()));
        writeBody(out,t); // defined in Container
    }

/******************************************************************/

    public Object clone() throws CloneNotSupportedException {
        shared = true;
        return super.clone();
    }

    public String getMode() {
        return mode;
    }

    public Box setMode(String mode) {
        this.mode=mode;
        return this;
    }

    public void set(Object v) {
        init((Object[]) v);
    }

    public Object get() {
        return igetv();
    }

    public void init(Object[] a) {
        expr = (Object[])a;
        length = expr.length;
        shared = true;
    }

    // Make it unshared
    protected final void copy() {
        expr = (Object[]) expr.clone();
        shared = false;
    }

    // Virtual methods from Container:
    public Object[] igetv() {
        if (expr.length==length) {
            shared = true;
            return expr;
            }
        else {
            return Expr.subexpr(expr,0,length);
            }
        }

    Object igetn(int pos) { // null if pos>=length
//      if(pos<0) pos = length+1+pos;
        if(pos<length && pos>=0)
            return expr[pos];
        else
            return null;
    }

    void   isetn(int pos, Object a) {
//      if (pos<0) pos = length+1+pos;
        if (pos>=length) isetlen(pos+1);
        if (pos >=0) {
            if (shared) copy();
            expr[pos]=a;
            }
        }

    int    igetlen() {
        return length;
        }

    void   isetlen(int len) {          // fill new cells with BLANKs
        if (len<length) {
            if (!shared) {
                for (int i=len; i<length; i++) expr[i]=null; // let gc do its job
            }
        } else {
            if (len>expr.length) {
                Object[] newexpr = new Object[(shared? len : len+expr.length)]; // no extra room if shared
                System.arraycopy(expr,0,newexpr,0,length);
                expr = newexpr;
                shared = false;
            }
            else    if (shared) copy();
            for (int i=length; i<len; i++) {
                expr[i] = BLANK;
            }
        };
        length=len;
    }

    void ishift(int pos, int shift) { // fill new cells with BLANKs
//      if (pos<0) pos = length+pos+1;
        if (pos<0) pos = 0;
        if (pos>length) isetlen(pos); // now pos<=length
        int lentail = length-pos; // >=0
        if (shift>0) {
                isetlen(length+shift);
                if(shared)copy(); // for sure
                System.arraycopy(expr,pos,expr,pos+shift,lentail);
                for (int i=pos; i<pos+shift; i++) {
                    expr[i] = BLANK;
                }
        } else {
            if (lentail+shift>0) {
                if(shared)copy();
                System.arraycopy(expr,pos-shift,expr,pos,lentail+shift);
                isetlen(length+shift);
            } else {
                isetlen(pos);
            }
        }
    }
}