package org.refal.j;

public class Table extends Box {
    //static { System.out.println("Start initializing Table"); }

    static final int UPDATE = 1;
    static final int NEW    = 2;
    static final int DELETE = 3;
    static final int OLD    = 4;

    public Table() { super(); }
    public Table(int size) { super(size); }

    public String getMode() { return "TABLE"; }

    public Object get() { return this; };

/****************** Creation "interface" *********************/

    public static Object makeObject(String mode) {
        if (mode=="TABLE")  {
            return new Table();
        } else return null;
    }

    public static Referable readObject(String header, java.io.PushbackReader in, Table t)
                                    throws Exception
    {
//        try {
            if (header.length()>0 && (header.charAt(0)=='T')) {
                int size = (header.length()<=1? 0 : Integer.parseInt(header.substring(1)));
                Container cont = new Table(size);
                if (cont.readBody(in,t))  // defined in Container
                    return cont;
            }
//        } catch (NumberFormatException e) {}
        return null;
    }

/*************************/

    public void writeObject(java.io.Writer out, Table t) throws java.io.IOException {
        out.write('T');
        out.write(String.valueOf(igetlen()));
        writeBody(out,t); // defined in Container
    }

/******************************************************************/

    protected static Table getTable(Object a) {
        if (a instanceof Reference) {
            a = ((Reference)a).getReferable();
        }
        if (a instanceof Table)
            return (Table)a;
        else
            return null;
    }

    protected static int getOper(Object a) {
        char op;
        if (a instanceof Character) op = ((Character)a).charValue();
        else if (a instanceof String) {
                String s = (String) a;
                if (! (s.length()==1)) return 0;
                op = s.charAt(0);
            }
        else return 0;
        return  ("UNDO".indexOf(op)+1);
    }

    // $ENTRY TABLE_LIST
    public static final Object TABLE_LIST = Term.getExternal("TABLE_LIST",
        new org.refal.j.Function("Table.TABLE_LIST") {
          public Object[] eval(Object[] e) throws Exception { return TABLE_LIST(e); }
        });

    public static Object[] TABLE_LIST(Object[] a) {
        if (! (a.length==1)) return null;
        Table t = getTable(a[0]);
        if (t==null) return null;
        return t.igetv();
    }

    // $ENTRY TABLE_LINK
    public static final Object TABLE_LINK = Term.getExternal("TABLE_LINK",
        new org.refal.j.Function("Table.TABLE_LINK") {
          public Object[] eval(Object[] e) throws Exception { return TABLE_LINK(e); }
        });

    public static Object[] TABLE_LINK(Object[] a) {
        if (! (a.length %2==1)) return null;
        Table t = getTable(a[0]);
        if (t==null) return null;
        t.link(a,1, a.length);
        return new Object[] {a[0]};
    }

    void link(Object name, Object value) {
        isetn(igetlen(),name);
        isetn(igetlen(),value);
        }

    void link(Object[] nvs, int from, int to) {
        sets(-1,to-from, nvs, from, to);
        }

    private Object tableMap(int op, Object name, int d /* 0: name->value; 1: value->name */) {
        int pos;
        ValueExists: {
            NameExists: {
                /* Search name */
                for (pos=0; pos<length; pos+=2) {
                    if (expr[pos+d].equals(name)) { // found!
                        switch (op) {
                            case DELETE:
                                ishift(pos,-2);  // deleting
                                return null;
                            case NEW:
                                break NameExists;
                            default:
                                break ValueExists;
                        }
                    }
                }
                // Not found
                switch (op) {
                    case UPDATE:
                    case NEW:
                        pos = length;
                        ishift(pos,2);
                        isetn(pos+d, name);
                        break;
                    default: return null; // == not found
                }
            } // Name Exists at pos
            // insert value
            if (d!=0) throw new IllegalArgumentException("Name not found for value "+name);
            isetn(pos+1, new Reference(name));
        } // ValueExists at pos
        return expr[pos+1-d];
    }

    Object nameToValue(int op, Object name) {
            return (tableMap(op,name,0));
        }

    Object valueToName(int op, Object value) {
            return (tableMap(op,value,1));
        }

    Reference defineReference(Object name, Referable value) {
        Reference r = (Reference) nameToValue(UPDATE,name);
        if (value!=null) {
            if (r.getReferable()!=null) System.err.println("Table.defineReference: redefined symbol "+name);
            r.setReferable(value);
        }
        return r;
    }

    /**
     * Defines constant &name==value
     * Links &name to value unless it is already defined
     * If old value not exists the new value (value!=null ? value : new Reference(name)) is saved under name
     * If  value!=null and old value exist, Error is thrown
     * @returns old value if it exists otherwise new value
     */
    Object define(Object name, Object value) throws Error {
        Object r = nameToValue(OLD,name);
        if (r!=null)
            if (value==null) return r;
            else throw new Error("Table.linkValue: can't link "+name+"="+value+" in "+this);
        else {
            r = (value!=null ? value : new Reference(name));
            link(name,r);
        }
        return r;
    }

    // $ENTRY TABLE_VALUE
    public static final Object TABLE_VALUE = Term.getExternal("TABLE_VALUE",
        new org.refal.j.Function("Table.TABLE_VALUE") {
          public Object[] eval(Object[] e) throws Exception { return TABLE_VALUE(e); }
        });

    public static Object[] TABLE_VALUE(Object[] a) {
        if (! (a.length==3)) return null;
        Table t = getTable(a[0]);
        if (t==null) return null;
        int op = getOper(a[1]);
        if (op<=0) return null;
        Object res = t.tableMap(op,a[2],0);
        if (res==null) return EMPTY;
        return new Object[] {res};
    }

    // $ENTRY TABLE_NAME
    public static final Object TABLE_NAME = Term.getExternal("TABLE_NAME",
        new org.refal.j.Function("Table.TABLE_NAME") {
          public Object[] eval(Object[] e) throws Exception { return TABLE_NAME(e); }
        });

    public static Object[] TABLE_NAME(Object[] a) {
        if (! (a.length==3)) return null;
        Table t = getTable(a[0]);
        if (t==null) return null;
        int op = getOper(a[1]);
        if (op<=0) return null;
        Object res = t.tableMap(op,a[2],1);
        if (res==null) return EMPTY;
        return new Object[] {res};
    }
}