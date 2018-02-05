package org.refal.j;

/******************************************************************************
 *       <NAME s.Ref> => Word
 *       <NEW> => s.Ref
 *       <NEW s.Word> => s.Ref
 *       <MAKE  s.Mode> => s.Ref
 *       <MAKE  s.Mode s.Ref> => s.Ref
 ******************************************************************************/

public class Reference {
    //static { System.out.println("Start initializing Reference"); }

    static final char DEREFER = '&';
    protected final Object name;
    protected Referable object = null;

    public Reference (Object name) {
        if (name==null)
            this.name = getUniqueName();
        else
            this.name = name;
        }

    public String getUniqueName() {
        return "X$"+Integer.toHexString(System.identityHashCode(this));
        }

    public Reference (Object name, Referable object) {
            this(name);
            this.object = object;
        }

    public String toString() { return /*DEREFER+*/name.toString(); } // for PRINT

    /* VFunc getName this = s */
    /* This name may be not unique */
    public Object getName() { return name; }

    // $ENTRY NAME
    public static final Object NAME = Term.getExternal("NAME",
        new org.refal.j.Function("Reference.NAME") {
          public Object[] eval(Object[] e) throws Exception { return NAME(e); }
        });

    /* $Func NAME */
    public static Object[] NAME(Object [] a) {
        if (a.length != 1) return null;
        if (! (a[0] instanceof Reference)) return null;
        return new Object[] { ((Reference)a[0]).getName() };
        }

/******************STATIC OBJECT FACTORY*********************************/

    static final String[] classNames = {
            "org.refal.j.Box",
            "org.refal.j.Mask",
            "org.refal.j.Real",
            "org.refal.j.Chars",
            "org.refal.j.Table"
        };

    static final ObjectFactory objectFactory = new ObjectFactory(/* classNames */);

    public static Referable readObject(java.io.PushbackReader in, Table t) throws java.io.IOException {
        return objectFactory.readObject(in,t);
        }

    public static Referable make(String mode) {
        Referable r=null;
//        for (int i=0; i<10000; i++)
            r = objectFactory.makeObject(mode);
        return r;
        }

    public static Referable make0(String mode) {
            Referable r;
            if (mode=="BOX" || mode=="CHAIN" || mode=="VECTOR")  {
                return new Box().setMode(mode);
                }
            else  if (mode=="MASK"  ) r = new Mask();
            else  if (mode=="STRING") r = new Chars();
            else  if (mode=="TABLE" ) r =  new Table();
            else  if (mode=="REAL") r =  new Real();
            else if (mode=="FILEIO") r = new Stream();
            else r=null;
            return r;
        }
/************************************************************************/
    // $ENTRY MAKE
    public static final Object MAKE = Term.getExternal("MAKE",
        new org.refal.j.Function("Reference.MAKE") {
          public Object[] eval(Object[] e) throws Exception { return MAKE(e); }
        });

/**
  * <MAKE s.mode> => s.Ref
  * <MAKE s.mode s.Ref> => s.Ref
  */
    public static Object[] MAKE(Object[] a) {
        int la= a.length;
        if (la == 1 || la==2) {
            if (! (a[0] instanceof String)) return null;
            Referable obj = make((String)a[0]);
            if (obj==null) return null;
            Reference r;
            if (la==1) r=new Reference(null);
            else {
                if (! (a[1] instanceof Reference)) return null;
                r = (Reference) a[1];
            }
            r.setReferable(obj);
            return new Object[] {r};
        }
        else return null;
    }

    // $ENTRY NEW
    public static final Object NEW = Term.getExternal("NEW",
        new org.refal.j.Function("Reference.NEW") {
          public Object[] eval(Object[] e) throws Exception { return NEW(e); }
        });

    public static Object[] NEW(Object[] a) {
        String name;
        int la= a.length;
        if (la == 0) {
            name = null;
        }
        else if (la==1) {
            name = Convert.toString( a[0] );
        }
        else return null;
        Reference r = new Reference(name,null);
        return new Object[] {r};
    }

    // $ENTRY COPY
    public static final Object COPY = Term.getExternal("COPY",
        new org.refal.j.Function("Reference.COPY") {
          public Object[] eval(Object[] e) throws Exception { return COPY(e); }
        });

    public static Object[] COPY(Object[] a) {
        if (a.length!=2) return null;
        if (!(a[0] instanceof Reference)) return null;
        if (!(a[1] instanceof Reference)) return null;
        return new Object[] {COPY((Reference)a[0],(Reference)a[1])};
    }
    public static Reference COPY(Reference a, Reference b) {
        b.setReferable(a.cloneObject());
        return b;
    }

    // $ENTRY MOVE
    public static final Object MOVE = Term.getExternal("MOVE",
        new org.refal.j.Function("Reference.MOVE") {
          public Object[] eval(Object[] e) throws Exception { return MOVE(e); }
        });

    public static Object[] MOVE(Object[] a) {
        if (a.length!=2) return null;
        if (!(a[0] instanceof Reference)) return null;
        if (!(a[1] instanceof Reference)) return null;
        return new Object[] {MOVE((Reference)a[0],(Reference)a[1])};
    }
    public static Reference MOVE(Reference a, Reference b) {
        b.setReferable(a.getReferable());
        a.setReferable(null);
        return b;
    }

    // $ENTRY SET
    public static final Object SET = Term.getExternal("SET",
        new org.refal.j.Function("Reference.SET") {
          public Object[] eval(Object[] e) throws Exception { return SET(e); }
        });

    public static Object[] SET(Object[] a) throws Error {
        if (a.length!=2) return null;
        if (!(a[0] instanceof Reference)) return null;
        Object r = SET((Reference)a[0],a[1]);
        return (r==null ? null : new Object[] {r}) ;
    }
    public static Reference SET(Reference a, Object b) throws Error {
        if (b instanceof Reference) {
            return COPY((Reference)b,a);
        } else {
            String mode = Term.MODE(b);
            Referable obj = make(mode);
            if (obj == null) return null;
            obj.set(b);
            a.setReferable(obj);
        }
        return a;
    }

    // $ENTRY GET
    public static final Object GET = Term.getExternal("GET",
        new org.refal.j.Function("Reference.GET") {
          public Object[] eval(Object[] e) throws Exception { return GET(e); }
        });

    public static Object[] GET(Object[] a) throws Error {
        if (a.length!=1) return null;
        if (!(a[0] instanceof Reference)) return a;
        Object r = GET((Reference)a[0]);
        return (r==null ? null : new Object[] {r}) ;
    }
    public static Object GET(Reference a) throws Error {
        Referable obj = a.getReferable();
        if (obj==null) throw new Error("REF-TO-SIMPLE");
        return obj.get();
    }

    public Referable getReferable() {return object;}

    public static Referable getReferable(Object r) {
        if (r instanceof Referable) return (Referable) r;
        if (r instanceof Reference) return ((Reference) r).getReferable();
        throw new IllegalArgumentException(""+r);
    }

    public void setReferable(Referable object) { this.object = object; }

    public static void setReferable(Object r, Referable object) {
        if (r instanceof Reference)
            ((Reference) r).setReferable(object);
        else throw new IllegalArgumentException(""+r);
    }

    public void defineReferable(Referable value) {
        if (value!=null) {
            if (getReferable()!=null)
                System.err.println("Table.defineReferable: redefined symbol "+name+". Old value = "+getReferable());
            setReferable(value);
        }
    }

    public static void defineReferable(Object r, Referable object) {
        if (r instanceof Reference)
            ((Reference) r).defineReferable(object);
        else {
            throw new IllegalArgumentException(""+r);
        }
    }

    Referable cloneObject() {
        try { return (Referable) object.clone(); }
        catch (CloneNotSupportedException e) { return object; }
        }

    public void init(Object[] a) throws Error {
        Referable obj = getReferable();
        if(obj==null) { // Make EMPTY reference BOX
            obj = make("BOX");
            setReferable(obj);
            }
        obj.init(a);
        }

static private Object[] empty = new Object[] {};
}
