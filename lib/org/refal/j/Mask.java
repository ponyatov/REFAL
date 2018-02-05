package org.refal.j;
import java.math.BigInteger;

public class Mask extends Container {

    public static final Number ZERO = new Integer(0);

    public static final Character NOL = new Character('0');
    public static final Character ONE = new Character('1');

    protected Number value = ZERO;

//    protected int length=0;

//    public static final int MAXLEN = 64;

    public Mask() {
        init(EMPTY);
    }

    public Mask(Object[] expr) {
        init(expr);
    }

    static public Mask create(Number n) {
        if (n instanceof Float) return null;
        if (n instanceof Double) return null;
        Mask mask = new Mask();
        mask.value = n;
        return mask;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMode() {
        return "MASK";
    }

    public void set(Object v) throws Error {
        if (v instanceof Number && !(v instanceof Float) && !(v instanceof Double)) {
            value = (Number)v;
//          length = MAXLEN;
        }
        else throw new Error("Improper object value "+v+" for mode MASK");
    }

    public Object get() {
        return value;
    }

    public void init(Object[] a) {
        int la = a.length;
        BigInteger b;
        if (la>0 && a[la-1].equals(ONE)) {
            b = BigInteger.valueOf(-1L);
            for (int i=0; i<la; i++) {
                if (! (a[i].equals(ONE))) b=b.clearBit(i);
            }
        } else {
            b = BigInteger.valueOf(0L);
            for (int i=0; i<la; i++) {
                if (a[i].equals(ONE)) b=b.setBit(i);
            }
        }
        value=Arithm.norm_Big(b);
    }

/****************** Creation "interface" *********************/

    public static Object makeObject(String mode) {
        if (mode=="MASK")  {
            return new Mask();
        } else return null;
    }

    public static Referable readObject(String header, java.io.PushbackReader in, Table t)
                                    throws Exception
    {
        if (header.length()==0) {
            int c = in.read();
            in.unread(c);
            if (Character.isDigit((char)c) || c=='-' || c == '+') {
                Number number = Stream.readNumber(in);
                Referable res = (number instanceof Double? (Referable) new Real() : (Referable) new Mask());
                res.set(number);
                return res;
            }
        } else
        if (header.charAt(0)=='M') {
            Container cont = new Mask();
            if (cont.readBody(in,t))  // defined in Container
                return cont;
        }
        return null;
    }

/******************************************************************/

    public void writeObject(java.io.Writer out, Table t) throws java.io.IOException {
        out.write(value.toString());
    }

/************************************************* Virtual methods from Container: *****/
/*
    NOTE!
    The bit sequence of a number is considered as infinite,
    that is expanded infinitely by the last (sign) bit.
    Hence negative pos is considered as not fitting the range
    (rather than count from right).
    However igetlen returns number of essential bits, that is all but
    that result from sign expansion.
    '0' is used as neutral symbol.
*/
    public Object[] igetv() {
        BigInteger b = Arithm.toBig(value);
        int len = b.bitLength()+1;
        Object[] res = new Object[len];
        for (int i=0; i<len; i++) {
            res[i] = (b.testBit(i)? ONE : NOL);
        }
        return res;
        }

    Object igetn(int pos) {
        BigInteger b = Arithm.toBig(value);
//      if (pos<0) pos = b.bitLength()+2+pos;
        if(pos>=0)
            return (b.testBit(pos)? ONE : NOL);
        else
            return null;
    }

    void   isetn(int pos, Object a) {
        BigInteger b = Arithm.toBig(value);
//      if (pos<0) pos = b.bitLength()+2+pos;
        if (pos<0) return;
        b = (a.equals(ONE)? b.setBit(pos) : b.clearBit(pos));
        value=Arithm.norm_Big(b);
    }

    int    igetlen() {
        BigInteger b = Arithm.toBig(value);
        return b.bitLength()+1;
        }

    void   isetlen(int len) {
        if (len<1) len=1;
        BigInteger b = Arithm.toBig(value);
        int blen = b.bitLength();
        if (len > blen) return;
        boolean set = b.testBit(blen);
        for (int i=len-1; i<blen; i++)
            b = (set? b.setBit(i) : b.clearBit(i));
        value=Arithm.norm_Big(b);
    }

    void ishift(int pos, int shift) {
        if (pos<0) pos=0;
        BigInteger b = Arithm.toBig(value);
        int length = b.bitLength()+1;
        if(pos>length) length=pos;
        if(shift<0) {
            for(int i=pos; i<length; i++)
                b = (b.testBit(i-shift) ? b.setBit(i) : b.clearBit(i));
        } else {
            for(int i=length-1+shift; i>=pos; i--)
                b = (i-shift >= pos && b.testBit(i-shift) ? b.setBit(i) : b.clearBit(i));
        }
        value=Arithm.norm_Big(b);
    }
}