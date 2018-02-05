package org.refal.j;
import java.util.*;
/**
  * RefalJ-callable functions for access to Collection, List or Map.
     * Func VALUE      t.ListOrMap t.key = t.value
     * Func LINK       t.ListOrMap t.key t.Value =
     * Func VALUES     t.CollectionOrMap = e.values
     * Func KEYS       t.ListOrMap = e.values
     * Func SIZE       t.CollectionOrMap = s.size
  */

public class Access {

  // $ENTRY VALUE
    public static final Object VALUE = Term.getExternal("VALUE",
        new org.refal.j.Function("Access.VALUE") {
          public Object[] eval(Object[] e) throws Exception { return VALUE(e); }
        });
    /**
     * Func VALUE t.ListOrMap t.key = t.value
     */
    public static Object[] VALUE(Object[] a) {
        int la = a.length;
        if (! (la==2)) return null;
        Object r = value(a[0], a[1]);
        return (r==null? null : new Object[] {r} );
    }
    /**
     * Returns the element for the specified index or key.
     * Returns null
     * if either index does not fit the range of List indices
     * or element with this key does not exist in the Map.
     *
     * @param  Object c either List or Map.
     * @param  Object k index or key of element to return.
     * @return the value for this index or key
     */
    public static Object value(Object c, Object k) {
        if (c instanceof List) {
            List list = (List) c;
            Number n = Arithm.getNumber(k);
            if (n==null) return null;
            try {
                return list.get(n.intValue());
            } catch (IndexOutOfBoundsException ex) {
                return null;
            }
        } else
        if (c instanceof Map) {
            Map map = (Map) c;
            return map.get(k);
        }
        else return null;
    }

  // $ENTRY LINK
    public static final Object LINK = Term.getExternal("LINK",
        new org.refal.j.Function("Access.LINK") {
          public Object[] eval(Object[] e) throws Exception { return LINK(e); }
        });
    /**
     * Func LINK t.ListOrMap t.key t.Value =
     */
    public static Object[] LINK(Object[] a) {
        int la = a.length;
        if (! (la==3)) return null;
        boolean r = link(a[0], a[1], a[2]);
        return (r? Expr.EMPTY: null );
    }
    /**
     * Links the given key to the given value in the Map
     * When object is a List the key must be Integer
     * in the range 0..size inclusive.
     *
     * @param  c either List or Map.
     * @param  k index or key to be mapped to the given value.
     * @param  v the given value.
     * @ return true if operation is legal, false otherwise
     */
    public static boolean link(Object c, Object k, Object v) {
        if (c instanceof List) {
            List list = (List) c;
            Number n = Arithm.getNumber(k);
            if (n==null) return false;
            try {
                int idx = n.intValue();
                if (idx<list.size()) list.set(idx,v);
                else list.add(idx,v);
                return true;
            } catch (IndexOutOfBoundsException ex) {
                return false;
            }
        } else
        if (c instanceof Map) {
            Map map = (Map) c;
            map.put(k,v);
            return true;
        }
        else return false;
    }

  // $ENTRY VALUES
    public static final Object VALUES = Term.getExternal("VALUES",
        new org.refal.j.Function("Access.VALUES") {
          public Object[] eval(Object[] e) throws Exception { return VALUES(e); }
        });
    /**
     * Func VALUES t.CollectionOrMap = e.values
     */
    public static Object[] VALUES(Object[] a) {
        int la = a.length;
        if (! (la==1)) return null;
        Object[] r = values(a[0]);
        if (r==null) return null;
        else return r;
    }
    /**
     * Returns the sequence of values contained in this Collection or Map.
     * If object is Map it is first transformed to Collection view of values contained in the Map.
     *
     * @param  obj either Collection or Map.
     * @return the values contained in this Collection or Map
     * @throws NullPointerException if Map or Collection contains null as an element
     */
    public static Object[] values(Object obj) {
        Collection c;
        if (obj instanceof Map) {
            c = ((Map)obj).values();
        } else
        if (obj instanceof Collection) {
            c = (Collection) obj;
        }
        else return null;
        Object[] r = c.toArray();
        for (int i=0; i<r.length; i++) {
            if(r[i]==null) throw new NullPointerException();
        }
        return r;
    }

  // $ENTRY KEYS
    public static final Object KEYS = Term.getExternal("KEYS",
        new org.refal.j.Function("Access.KEYS") {
          public Object[] eval(Object[] e) throws Exception { return KEYS(e); }
        });
    /**
     * Func KEYS t.ListOrMap = e.values
     */
    public static Object[] KEYS(Object[] a) {
        int la = a.length;
        if (! (la==1)) return null;
        Object[] r = keys(a[0]);
        if (r==null) return null;
        else return r;
    }
    /**
     * Returns the sequence of keys mapped by this Map to some element
     * or sequece of indices 0 1 2 ... size-1 of elements of this List
     *
     * @param  c either List or Map.
     * @return the keys for which <VALUE s.Obj s.Key> yields some term
     * @throws NullPointerException if this Map contains null as a key
     */
    public static Object[] keys(Object c) {
        Object[] r;
        if (c instanceof List) {
            List list = (List) c;
            int n = list.size();
            r = new Object[n];
            for (int i=0; i<n; i++) {
                r[i] = new Integer(i);
            }
        } else
        if (c instanceof Map) {
            Map map = (Map) c;
            r = map.keySet().toArray();
            for (int i=0; i<r.length; i++) {
                if(r[i]==null) throw new NullPointerException();
            }
        }
        else return null;
        return r;
    }

  // $ENTRY SIZE
    public static final Object SIZE = Term.getExternal("SIZE",
        new org.refal.j.Function("Access.SIZE") {
          public Object[] eval(Object[] e) throws Exception { return SIZE(e); }
        });
    /**
     * Func SIZE t.CollectionOrMap = s.size
     */
    public static Object[] SIZE(Object[] a) {
        int la = a.length;
        if (! (la==1)) return null;
        try {
            return new Object[] {new Integer(size(a[0]))};
        } catch (IllegalArgumentException ex) { return null; }
    }
    /**
     * Returns the size of this Collection or Map.
     *
     * @param  obj either Collection or Map.
     * @return the size of the Collection or Map
     * @throws IllegalArgumentException if obj is neither Map nor Collection
     */
    public static int size(Object obj) {
        int res;
        if (obj instanceof Map) {
            res = ((Map)obj).size();
        } else
        if (obj instanceof Collection) {
            res = ((Collection) obj).size();
        }
        else throw new IllegalArgumentException();
        return res;
    }

}
