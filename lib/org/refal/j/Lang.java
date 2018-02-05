package org.refal.j;

public abstract class Lang {
    /**
     *   Compare terms for equality
     */
    public static boolean termsEqual(Object t1, Object t2) {
        if (t1==t2) return true;
        else
        if (t1 instanceof Object[])
            if (t2 instanceof Object[]) {
                Object[] e1 = (Object[])t1;
                Object[] e2 = (Object[])t2;
                if (! (e1.length==e2.length)) {
                    //trace("lengths differ: " + e1.length + "=/=" + e2.length);
                    return false; }
                else return exprsEqual(e1,0,e2,0,e1.length);
            }
            else { //trace("bracket term vs symbol");
                return false; }
        else if (t2 instanceof Object[]) { //trace("symbol vs bracket term");
            return false; }
        else if (t1==null) { //trace("t1 null");
            return false; }
        else
            return t1.equals(t2);
        }

    /**
     *   Compare (sub)expressions for equality
     */
    public static boolean exprsEqual(Object[] e1, int i1, Object[] e2, int i2, int len) {
        if (e1==null || e2==null) throw new NullPointerException();
        if (len <0 ||  i1<0         || i2<0         ) throw new ArrayIndexOutOfBoundsException();
        if (len==0 && (i1>e1.length || i2>e2.length)) throw new ArrayIndexOutOfBoundsException();
        for (int k=0; k<len; k++) {
            if (termsEqual(e1[i1+k],e2[i2+k])) continue;
            else { //trace("term "+k);
                return false; }
            }
        return true;
        }

    private static void trace(String s) {
        System.out.println("Lang.equals: "+s);
        }

}
