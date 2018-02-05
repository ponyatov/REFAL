package org.refal.j;

public abstract class Container implements Referable, Writable {
    //static { System.out.println("Start initializing Container"); }
    // methods from Referable

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
        }

    // Virtual methods:
    // pos>=0 counts from left, positions after end are valid
    // pos<0 counts from right, positions before beginning are treated as pos=0
    //

    abstract Object[] igetv();
//  abstract void     isetv(Object[] a); // init may be used instead
    abstract Object   igetn(int pos); // null if pos>=length
    abstract void     isetn(int pos, Object a);
    abstract int      igetlen();
    abstract void     isetlen(int len);          // fill with BLANK
    abstract void     ishift(int pos, int shift); // fill with BLANK

    static final char BLANK_CHAR = ' ';
    static final Object BLANK = new Character(BLANK_CHAR);

    // Helper methods

    protected static Container getContainer(Object a) {
        if (a instanceof Reference) {
            Referable r = ((Reference)a).getReferable();
            if (r==null) { // Make EMPTY reference Box
                r=Reference.make("BOX");
                ((Reference) a).setReferable(r);
                }
            if (! (r instanceof Container)) return null;
            return (Container) r;
        }
        else
        if (a instanceof Object[])
            return new Box((Object[])a);
        else
        if (a instanceof String)
            return new Chars((String)a);
        else
        if (a instanceof Number)
            return Mask.create((Number)a);
        else
            return null;
    }

    protected static Object refOrVal (Object a, Container c) {
        return (a instanceof Reference? a : c.get() );
        }

/***************************IN/OUT***************************************************/
/* Read/Write contents as sequence of terms (expression) in parentheses */

    boolean readBody(java.io.PushbackReader in, Table t) throws Exception {
        int c = in.read();
        //System.out.println("Container.readBody: start:("+this+"): "+(char)c);
        if (c=='(') {
            Stream.readExpr(this,in,t,Stream.RIGHT_BRAC);
            return true;
        }
        else {
            in.unread(c);
            return false;
        }
    }

    void writeBody(java.io.Writer out, Table t) throws java.io.IOException {
        Stream.writeTerm(out,t,igetv());
    }

    // Top methods:
/**************************************************************************** GETV ***/
    // $ENTRY GETV
    public static final Object GETV = Term.getExternal("GETV",
        new org.refal.j.Function("Container.GETV") {
          public Object[] eval(Object[] e) throws Exception { return GETV(e); }
        });
    public static Object[] GETV(Object[] a) {
        if (a.length!=1) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        return c.igetv();
        }

/**************************************************************************** SETV ***/
    // $ENTRY SETV
    public static final Object SETV = Term.getExternal("SETV",
        new org.refal.j.Function("Convert.SETV") {
          public Object[] eval(Object[] e) throws Exception { return SETV(e); }
        });
    public static Object[] SETV(Object[] a) throws Error {
        int la1 = a.length-1;
        if (la1<0) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        c.init (Expr.subexpr(a,1,la1));
        return new Object[] {refOrVal(a[0],c)};
    }

/**************************************************************************** POS ***/
    int[] pos(Object a) {
        int len = igetlen();
        int[] res = {};
        int size=0;
        for (int i=0; i<len; i++) {
            if (a.equals(getn(i))) {
                int[] newres = new int[size+1];
                if (size>0) System.arraycopy(res,0,newres,0,size);
                newres[size] = i;
                size++;
                res=newres;
            }
        }
        return res;
    }
  // $ENTRY POS
    public static final Object POS = Term.getExternal("POS",
        new org.refal.j.Function("Convert.POS") {
          public Object[] eval(Object[] e) throws Exception { return POS(e); }
        });
    public static Object[] POS(Object[] a) {
        if (a.length!=2) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        int[] ps = c.pos(a[1]);
        Object[] result = new Object[ps.length];
        for (int i=0; i<ps.length; i++) {
            result[i] = new Integer(ps[i]);
        }
        return result;
    }

/**************************************************************************** GETN ***/
    Object getn(int pos) {
        Object r = igetn(pos);
        if (r==null) return BLANK;
        return r;
    }
     // $ENTRY GETN
    public static final Object GETN = Term.getExternal("GETN",
        new org.refal.j.Function("Convert.GETN") {
          public Object[] eval(Object[] e) throws Exception { return GETN(e); }
        });
   public static Object[] GETN(Object[] a) {
        if (a.length!=2) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        Number n = Arithm.getNumber(a[1]);
        if (n==null) return null;
        int pos = n.intValue();
        if (pos<0) pos = c.igetlen()+1+pos;
        return (new Object[] { c.getn(pos) } );
    }

/**************************************************************************** SETN ***/
     // $ENTRY SETN
    public static final Object SETN = Term.getExternal("SETN",
        new org.refal.j.Function("Convert.SETN") {
          public Object[] eval(Object[] e) throws Exception { return SETN(e); }
        });
    public static Object[] SETN(Object[] a) {
        if (a.length!=3) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        Number n = Arithm.getNumber(a[1]);
        if (n==null) return null;
        int pos = n.intValue();
        if (pos<0) pos = c.igetlen()+1+pos;
        c.isetn(pos,a[2]);
        return new Object[] { refOrVal(a[0],c) };
    }

/**************************************************************************** GETS ***/
    Object[] gets(int pos, int len) {
        if (pos<0) pos = igetlen()+1+pos;
        Object[] r = new Object[len];
        for (int i=0; i<len; i++) {
            r[i] = getn(pos+i);
        }
        return r;
    }
     // $ENTRY GETS
    public static final Object GETS = Term.getExternal("GETS",
        new org.refal.j.Function("Convert.GETS") {
          public Object[] eval(Object[] e) throws Exception { return GETS(e); }
        });
    public static Object[] GETS(Object[] a) {
        if (a.length!=3) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        Number n = Arithm.getNumber(a[1]);
        if (n==null) return null;
        Number l = Arithm.getNumber(a[2]);
        if (l==null) return null;
        return c.gets(n.intValue(), l.intValue());
    }

/**************************************************************************** SETS ***/
    void sets(int pos, int count, Object[] a, int from, int to) {
        if (pos<0) pos = igetlen()+(1+pos);
        if (count<=0) return;
        int p=from;
        for (int i=0; i<count; i++) {
            isetn(pos+i, (to>from ? a[p] : BLANK));
            p++;
            if (p>=to) p=from;
        }
    }
     // $ENTRY SETS
    public static final Object SETS = Term.getExternal("SETS",
        new org.refal.j.Function("Convert.SETS") {
          public Object[] eval(Object[] e) throws Exception { return SETS(e); }
        });
    public static Object[] SETS(Object[] a) {
        int la = a.length;
        if (la<3) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        Number n = Arithm.getNumber(a[1]);
        if (n==null) return null;
        Number l = Arithm.getNumber(a[2]);
        if (l==null) return null;
        c.sets(n.intValue(),l.intValue(),a,3,la);
        return new Object[] { refOrVal(a[0],c) };
    }

/**************************************************************************** SHIFT ***/
     // $ENTRY SHIFT
    public static final Object SHIFT = Term.getExternal("SHIFT",
        new org.refal.j.Function("Convert.SHIFT") {
          public Object[] eval(Object[] e) throws Exception { return SHIFT(e); }
        });
    public static Object[] SHIFT(Object[] a) {
        if (a.length!=3) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        Number n = Arithm.getNumber(a[1]);
        if (n==null) return null;
        Number l = Arithm.getNumber(a[2]);
        if (l==null) return null;
        int pos = n.intValue();
        if (pos<0) pos = c.igetlen()+1+pos;
        c.ishift(pos,l.intValue());
        return new Object[] { refOrVal(a[0],c) };
    }

/**************************************************************************** LENCONT ***/
     // $ENTRY LENCONT
    public static final Object LENCONT = Term.getExternal("LENCONT",
        new org.refal.j.Function("Convert.LENCONT") {
          public Object[] eval(Object[] e) throws Exception { return LENCONT(e); }
        });
    public static Object[] LENCONT(Object[] a) {
        if (a.length!=1) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        return new Object[] { new Integer(c.igetlen()) };
    }

/**************************************************************************** CONCAT ***/
     // $ENTRY CONCAT
    public static final Object CONCAT = Term.getExternal("CONCAT",
        new org.refal.j.Function("Convert.CONCAT") {
          public Object[] eval(Object[] e) throws Exception { return CONCAT(e); }
        });
    public static Object[] CONCAT(Object[] a) throws Error {
        int la = a.length;
        if (la<1) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        Container c1;
        try {
            c1 = (Container) c.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("CloneNotSupportedException");
        }
        int pos=c.igetlen();
        for (int i=1; i<la; i++) {
            Container d = getContainer(a[i]);
            int ld = d.igetlen();
            for (int j=0; j<ld; j++, pos++) {
                c1.isetn(pos,d.igetn(j));
            }
        }
        ((Reference)a[0]).setReferable(c1);
        return new Object[] {refOrVal(a[0],c1)};
    }

/**************************************************************************** APPEND ***/
     // $ENTRY APPEND
    public static final Object APPEND = Term.getExternal("APPEND",
        new org.refal.j.Function("Convert.APPEND") {
          public Object[] eval(Object[] e) throws Exception { return APPEND(e); }
        });
    public static Object[] APPEND(Object[] a) {
        int la = a.length;
        if (la<1) return null;
        Container c = getContainer(a[0]);
        if (c==null) return null;
        //int oldlen = c.igetlen();
        //c.isetlen(oldlen+la-1);
        c.addExpr(a,1,la);
        return new Object[] { refOrVal(a[0],c) };
    }

    void addTerm(Object x) {
        isetn(igetlen(), x);
    }

    void addExpr(Object[] x) {
        addExpr(x, 0, x.length);
    }

    void addExpr(Object[] x, int from, int to) {
        int pos = igetlen();
        for (int i=from; i<to; i++, pos++)
            isetn(pos, x[i]);
    }

    void addContents(Container d) {
        addContents(d,0,d.igetlen());
    }

    void addContents(Container d, int from, int to) {
        int pos = igetlen();
        for (int i=from; i<to; i++, pos++)
            isetn(pos, d.igetn(i));
    }
}