package org.refal.j;

/******************************************************************************/

public abstract class Expr {

    static final Object[] EMPTY = {};
    static final Object[] T = {"T"};
    static final Object[] F = {"F"};
    static final Object[] EQ = { new Character('=') };
    static final Object[] GT = { new Character('>') };
    static final Object[] LT = { new Character('<') };

    // Helper methods

    static public Object[] subexpr(Object[] a,int pos,int len) {
        Object[] r = new Object[len];
        System.arraycopy(a,pos,r,0,len);
        return r;
        }

    static public Object[] conEE(Object[] ea, Object[] eb) {
        Object[] r = new Object[ea.length+eb.length];
        System.arraycopy(ea,0,r,0,ea.length);
        System.arraycopy(eb,0,r,ea.length,eb.length);
        return r;
        }

    static public Object[] conTE(Object t, Object[]e) {
        Object[] r = new Object[e.length+1];
        r[0] = t;
        System.arraycopy(e,0,r,1,e.length);
        return r;
        }

    static public Object[] conET(Object[]e, Object t) {
        Object[] r = new Object[e.length+1];
        System.arraycopy(e,0,r,0,e.length);
        r[e.length] = t;
        return r;
        }

    // $ENTRY LENGTH
    public static final Object LENGTH = Term.getExternal("LENGTH",
        new org.refal.j.Function("Expr.LENGTH") {
          public Object[] eval(Object[] e) throws Exception { return LENGTH(e); }
        });

    public static Object[] LENGTH (Object[] a) {
            return new Object[] { new Integer(a.length) };
    }

    // $ENTRY FIRST
    public static final Object FIRST = Term.getExternal("FIRST",
        new org.refal.j.Function("Expr.FIRST") {
          public Object[] eval(Object[] e) throws Exception { return FIRST(e); }
        });

    public static Object[] FIRST (Object[] a) {
            int la = a.length;
            if (la==0) return null;
            Number n = Arithm.getNumber(a[0]);
            if (n==null) return null;
            int first = n.intValue();
            if (first < 0) return null;
            if (first >= la) first = la-1;
            Object[] res = subexpr(a, first, la-first); // res[0] is extra element
            res[0] = subexpr(a,1,first);
            return res;
    }

    // $ENTRY LAST
    public static final Object LAST = Term.getExternal("LAST",
        new org.refal.j.Function("Expr.LAST") {
          public Object[] eval(Object[] e) throws Exception { return LAST(e); }
        });

    public static Object[] LAST (Object[] a) {
            int la = a.length;
            if (la==0) return null;
            Number n = Arithm.getNumber(a[0]);
            if (n==null) return null;
            int last = n.intValue();
            if (last < 0) return null;
            if (last >= la) last = la-1;
            int first = la-1-last;
            Object[] res = subexpr(a, first, la-first); // res[0] is extra element
            res[0] = subexpr(a,1,first);
            return res;
    }

    // $ENTRY Map
    public static final Object Map = Term.getExternal("Map",
        new org.refal.j.Function("Expr.Map") {
          public Object[] eval(Object[] e) throws Exception { return Map(e); }
        });

    public static Object[] Map (Object[] a) throws Exception {
            int la = a.length;
            if (la==0) return null;
            Object f = a[0];
            Box b = new Box();
            for (int i=1; i<la; i++) {
                Object[] arg = new Object[] {a[i]};
                Object[] res = Apply.APPLY(f,arg);
                if (res==null) {
                    Trace.Catch("",Convert.toString(f),arg,res);
                    return null;
                }
                b.addExpr(res);
            }
            return b.igetv();
    }

    // $ENTRY App
    public static final Object App = Term.getExternal("App",
        new org.refal.j.Function("Expr.App") {
          public Object[] eval(Object[] e) throws Exception { return App(e); }
        });

    public static Object[] App (Object[] a) throws Exception {
            int la = a.length;
            if (la==0) return null;
            Object f = a[0];
            for (int i=1; i<la; i++) {
                Object[] arg = new Object[] {a[i]};
                Object[] res = Apply.APPLY(f,arg);
                if (res==null) {
                    Trace.Catch("",Convert.toString(f),arg,res);
                    return null;
                }
            }
            return Expr.EMPTY;
    }

    // $ENTRY TYPE
    public static final Object TYPE = Term.getExternal("TYPE",
        new org.refal.j.Function("Expr.TYPE") {
          public Object[] eval(Object[] e) throws Exception { return TYPE(e); }
        });

    public static Object[] TYPE (Object[] a) {
        return conTE (new Character( Type(a) ), a) ;
    }

    public static char Type (Object[] a) {
            char res;
            int la = a.length;
            if (la==0) return '*';
            Object b = a[0];
            if (b instanceof Object[]) return 'B';
            if (b instanceof String) return 'F';
            if (b instanceof Number) return 'N';
            if (b instanceof Reference) return 'R';
            if (b instanceof Number) return 'N';
            if (b instanceof Character) {
                char c = ((Character)b).charValue();
                if (Character.isLetter(c)) return (Character.isUpperCase(c)? 'L' : 'l');
                if (Character.isDigit(c)) return 'D';
                return 'O';
            }
            return 'Z';
        }

    // $ENTRY COMPARE
    public static final Object COMPARE = Term.getExternal("COMPARE",
        new org.refal.j.Function("Expr.COMPARE") {
          public Object[] eval(Object[] e) throws Exception { return COMPARE(e); }
        });

    public static Object[] COMPARE (Object[] a) throws Error {
        if (a.length!=2) return null;
        int res = compareTerms(a[0],a[1]);
        return (res==0 ? EQ : (res>0 ? GT : LT));
    }

    private static int compareTerms(Object a, Object b) throws Error {
// (...)
        if (a instanceof Object[]) {
            if (b instanceof Object[]) {
                return compareExprs((Object[]) a, (Object[]) b);
            }
            else return 1;
        } else
        if (b instanceof Object[]) return -1; else
// &name
        if (a instanceof Reference) {
            if (b instanceof Reference) {
                int va = System.identityHashCode(a);
                int vb = System.identityHashCode(b);
                return (va>vb ? 1 : va<vb ? -1 : 0);
            }
            else return 1;
        } else
        if (b instanceof Reference) return -1; else
// 'c'
        if (a instanceof Character) {
            if (b instanceof Character) {
                return ((Character)a).charValue() - ((Character)b).charValue();
            }
            else return -1;
        } else
        if (b instanceof Character) return 1; else
// "..."
        if (a instanceof String) {
            if (b instanceof String) {
                String sa = (String)a;
                String sb = (String)b;
                int lsa = sa.length();
                int lsb = sb.length();
                for (int i=0; i<lsa && i<lsb; i++) {
                    int d = sa.charAt(i) - sb.charAt(i);
                    if (d!=0) return d;
                }
                return lsa - lsb;
            }
            else return -1;
        } else
        if (b instanceof String) return 1; else

// numbers

        if (a instanceof Number) {
            if (b instanceof Number) {
// 3.14
                Number x = (Number) a;
                Number y = (Number) b;
                int sortx = Arithm.sort(x);
                int sorty = Arithm.sort(y);
                //System.out.println("sortx="+sortx+", sorty="+sorty);
                if (sortx == Arithm.FOD) {
                    if (sorty == Arithm.FOD) {
                        double va = x.doubleValue();
                        double vb = y.doubleValue();
                        return (va>vb ? 1 : va<vb ? -1 : 0);
                    }
                    else return 1;
                } else
                if (sorty == Arithm.FOD) return -1; else
// 123
                switch (Math.min(sortx,sorty)) {
                    case Arithm.INT:
                        int va = x.intValue();
                        int vb = y.intValue();
                        return (va>vb ? 1 : va<vb ? -1 : 0);
                    case Arithm.BIG:
                        return Arithm.toBig(x).subtract(Arithm.toBig(y)).signum();
                    default: throw new java.lang.Error();
                }
            } else return -1;
        } else

        if (b instanceof Number) return 1; else
        // all other are greater than Numbers and less than references
            throw new Error("Expr.compareTerms: order undefined for "+a+" and "+b);
    }

    private static int compareExprs(Object[] a, Object[] b) throws Error {
        int la=a.length;
        int lb=b.length;
        for (int i=0; i<la && i<lb; i++) {
            int res = compareTerms(a[i], b[i]);
            if (res!=0) return res;
        }
        return la-lb;
    }
}