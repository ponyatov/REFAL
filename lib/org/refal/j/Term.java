package org.refal.j;

/******************************************************************************/
/*       CONST tA => T | F                                                    */
/*       MODE  tA => Word                                                     */
/******************************************************************************/

public abstract class Term {
    //static { System.out.println("Start initializing Term"); }

    static final Object[] EMPTY = {};  // Empty expression in brackets
    static final Object T = "T";
    static final Object F = "F";
    static final Object EQ = new Character('=');
    static final Object GT = new Character('>');
    static final Object LT = new Character('<');

    public static boolean CONST(Object a) {
        return (!(a instanceof Reference));
    }

    public static Object[] CONST(Object[] a) {
        if (a.length!=1) return null;
        else return (CONST(a[0]) ? Expr.T: Expr.F);
        }

/**************** for refal-generated modules ***************************/

//    public static Reference make(String name) {
//        return new Reference(name);
//        }

//    public static Reference make(String mode, String name) {
//        return new Reference(name, Reference.make(mode));
//        }

/*********************************************************************SYSTABLE************/
    static Object dummy = Table.makeObject(null); // make sure to start initializing Table

    //static { System.out.println("Start initializing Term 1"); }

    static Table sysTable; // = new Table();
    static {
        if (sysTable==null) {
            System.out.println("Initializing Term: initializing sysTable");
            sysTable = new Table(); // No start initializing Table
        }
    }

    //static { System.out.println("Start initializing Term 2"); }

    static Reference SYSTABLE = new Reference("SYSTABLE", sysTable);
    static { sysTable.link("SYSTABLE",SYSTABLE); }

    public static Object getExternal(Object name) {
        //System.out.println("Term.getExternal: "+name);
        return getExternal(name,null);
    }

    public static Object getExternal(Object name, Referable object) {
        if (sysTable==null) sysTable = new Table();
        return sysTable.defineReference(name,object);
        /*
        Reference r = (Reference) sysTable.nameToValue(Table.UPDATE, name);
        if (object!=null) {
            if (r.getReferable()!=null) System.err.println("Reference symbol "+name+" is redefined");
            r.setReferable(object);
        }
        return r;
        */
    }

    public static Object getExternal(Object name1, Object name2, Referable object) {
        getExternal(name2,object);
        return getExternal(name1,object);
    }

    /**
     * Defines external constant &name==value, or gets old value if value==null
     * If old value not exists and value==null, new Reference(name) is created as a new value
     * If both old value exist and value!=null Error is thrown
     * Note: defineExternal(name,null) is equivalent to getExternal(name,null) or getExternal(name)
     */
    public static Object defineExternal (Object name, Object value) throws Error {
        if (sysTable==null) sysTable = new Table();
        return sysTable.define(name,value);
    }

    // $ENTRY CONST
    public static final Object CONST = Term.getExternal("CONST",
        new org.refal.j.Function("Term.CONST") {
          public Object[] eval(Object[] e) throws Exception { return CONST(e); }
        });

/**********************************************************************************************/

    public static String MODE(Object a) {
        if (a instanceof Character) return "CHAR";
        if (a instanceof String)    return "STRING";
        if (a instanceof Double)    return "REAL";
        if (a instanceof Float)     return "REAL";
        if (a instanceof Number)    return "MASK";
        if (a instanceof Object[])  return "CHAIN";
        if (a instanceof Reference) {
            Referable obj = ((Reference)a).getReferable();
            if (obj==null) return "EMPTY";
            else
                return obj.getMode();
            }
        if (a instanceof Referable) {
            Referable obj = (Referable)a;
            return obj.getMode();
        }
        else return "OTHER";
    }

    // $ENTRY MODE
    public static final Object MODE = Term.getExternal("MODE",
        new org.refal.j.Function("Term.MODE") {
          public Object[] eval(Object[] e) throws Exception { return MODE(e); }
        });

    public static Object[] MODE(Object[] a) {
        if (a.length!=1) return null;
        String res = MODE(a[0]);
        if (res == null) return null;
        return new Object[] { res };
        }

    // $ENTRY NEWOBJ
    public static final Object NEWOBJ = Term.getExternal("NEWOBJ",
        new org.refal.j.Function("Term.NEWOBJ") {
          public Object[] eval(Object[] e) throws Exception { return NEWOBJ(e); }
        });

    public static Object[] NEWOBJ(Object[] a) throws Exception {
        if (a.length!=1) return null;
        if (!(a[0] instanceof String)) return null;
        Object res = newObj ((String)a[0]);
        if (res == null) return null;
        return new Object[] { res };
        }

    public static Object newObj(String cname) throws Exception {
        Class clazz = Class.forName(cname);
        Object obj = clazz.newInstance();
        return obj;
    }
}