package org.refal.j;

public class Apply {

    // $ENTRY APPLY
    public static final Object APPLY = Term.getExternal("APPLY",
        new org.refal.j.Function("Apply.APPLY") {
          public Object[] eval(Object[] e) throws Exception { return APPLY(e); }
        });

    public static Object[] APPLY(Object f, Object[] a) throws Exception {
        if (f instanceof Reference) {
            Referable r = ((Reference)f).getReferable();
            if (r instanceof Function) {
                return ((Function)r).eval(a);
            } else
            if (r==null)
                throw new Error("Function symbol "+Convert.toString(f)+" is undefined");
        }
        // if term f is not a function then invoke <APPLY_OBJECT tF eA>;
        Referable ao = ((Reference)APPLY_OBJECT).getReferable();
        if (ao==null) {
            throw new Error("First term must be a function (here: "+Convert.toString(f)+")"); // external function APPLY_OBJECT is undefined
        }
        else if (ao instanceof Function) {
            return ((Function) ao).eval(Expr.conTE(f,a));
        }
        else
            throw new Error("External reference APPLY_OBJECT not a function");
    }

    public static Object[] APPLY(Object[] a) throws Exception {
        if (a.length==0) throw new Error("Empty");
        return APPLY(a[0],Expr.subexpr(a,1,a.length-1)); // NOTE: The call should be expanded in the future
    }

    // This function may be redefined in Refal
    static private final Object APPLY_OBJECT = Term.getExternal("APPLY_OBJECT",
    new org.refal.j.Function("Apply.APPLY_OBJECT") {
          public Object[] eval(Object[] e) throws Exception { return APPLY_OBJECT(e); }
        });

    public static Object[] APPLY_OBJECT (Object[] a) throws Exception {
        int la = a.length;
        if (la==0) return null;
        Container c = Container.getContainer(a[0]);
        if (c==null) return null;
        return APPLY(Expr.conEE( c.igetv(), Expr.subexpr(a,1,la-1) ));
    }
}
