package org.refal.j;
import java.math.BigInteger;

public final class Arithm {

    static class Func extends org.refal.j.Function {
        private int fcode;
        private Func (String name, int fcode) {
            super(name);
            this.fcode = fcode;
            }
        public Object[] eval(Object[] e) {
            switch (fcode) {
                case ADD   : return ADD   (e);
                case SUB   : return SUB   (e);
                case MUL   : return MUL   (e);
                case DIV   : return DIV   (e);
                case REM   : return REM   (e);
                case DIVREM: return DIVREM(e);
                case ABS   : return ABS   (e);
                case SIGN  : return SIGN  (e);
                case POW   : return POW   (e);
                case GCD   : return GCD   (e);
                case SQRT  : return SQRT  (e);
                case EXP   : return EXP   (e);
                case LOG   : return LOG   (e);
                case SIN   : return SIN   (e);
                case ASIN  : return ASIN  (e);
                case COS   : return COS   (e);
                case ACOS  : return ACOS  (e);
                case TAN   : return TAN   (e);
                case ATAN  : return ATAN  (e);
                case ATAN2 : return ATAN2 (e);
                case AND   : return AND   (e);
                case XOR   : return XOR   (e);
                case OR    : return OR    (e);
                case RANDOM: return RANDOM(e);
            }
            return null;
        }
        static final int ADD   = 0;
        static final int SUB   = 1;
        static final int MUL   = 2;
        static final int DIV   = 3;
        static final int REM   = 4;
        static final int DIVREM= 5;
        static final int ABS   = 6;
        static final int SIGN  = 7;
        static final int POW   = 8;
        static final int GCD   = 9;
        static final int SQRT  = 10;
        static final int EXP   = 11;
        static final int LOG   = 12;
        static final int SIN   = 13;
        static final int ASIN  = 14;
        static final int COS   = 15;
        static final int ACOS  = 16;
        static final int TAN   = 17;
        static final int ATAN  = 18;
        static final int ATAN2 = 19;
        static final int AND   = 20;
        static final int XOR   = 21;
        static final int OR    = 22;
        static final int RANDOM= 23;
    }

    static Number getNumber(Object a) {
        //System.out.println("Arithm.getNumber:start: class of a is "+a.getClass().getName());
        if (a instanceof Reference) a=((Reference)a).getReferable();
        //System.out.println("Arithm.getNumber:mid: class of a is "+a.getClass().getName());
        if (a instanceof Referable) a=((Referable)a).get();
        if (a instanceof Number)
            return (Number)a;
        else {
        //    System.out.println("Arithm.getNumber:end: class of a is "+a.getClass().getName());
            return null;
        }
    }

    static final int NON = 0;
    static final int FOD = 1;
    static final int BIG = 2;
    static final int INT = 3;

    static int sort(Number z) {
        if (z instanceof Float) return FOD;
        if (z instanceof Double) return FOD;
        if (z instanceof BigInteger) return BIG;
        if (z==null) return NON;
        return INT;
        }

    static boolean isIntegral(Object z) {
        if (z instanceof Float) return false;
        if (z instanceof Double) return false;
        return true;
        }

    static Number norm_Big(BigInteger b) {
        // if b fits int
        if (b.bitLength()<32) return new Integer(b.intValue());
        return b;
    }

    static Number norm_long(long n) {
        // if n fits int
        if (((n+0x80000000L) & 0xffffffff00000000L) == 0L)
            return new Integer((int)n);
        else return BigInteger.valueOf(n);
    }

    static BigInteger toBig(Number n) {
        if (n instanceof BigInteger) return (BigInteger)n;
        else return BigInteger.valueOf(n.longValue());
        }

    static public Number valueOf(String s) {
        try {
            return numberValueOf(s);
        }
        catch (Error e) {
            throw new java.lang.Error(e.getMessage());
        }
    }

    static public Number numberValueOf(String s) throws Error {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e0) { }
        try {
            return new java.math.BigInteger(s);
        } catch (NumberFormatException e0) { }
        try {
            return Double.valueOf(s);
        } catch (NumberFormatException e1) {
            if (s.equals("+NaN") ||
                s.equals("NaN"))       return new Double(Double.NaN);
            if (s.equals("+Infinity") ||
                s.equals("Infinity" )) return new Double(Double.POSITIVE_INFINITY);
            if (s.equals("-Infinity")) return new Double(Double.NEGATIVE_INFINITY);
        }
        throw new Error("Parse error: \""+s+"\" not a number");
    }

    // $ENTRY ADD
    public static final Object ADD = Term.getExternal("ADD","+",
                                    new Func("Arithm.ADD", Func.ADD));

    public static Object[] ADD (Object[] a) {
            Object z = (a.length==2 ? ADD(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object ADD (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return norm_long    (x.longValue() + y.longValue())  ;
            case FOD: return new Double (x.doubleValue() + y.doubleValue());
            case BIG: return norm_Big        (toBig(x). add (toBig(y)))    ;
             default: return null; // Fail
        }
    }

    // $ENTRY SUB
    public static final Object SUB = Term.getExternal("SUB","-",
        new Func("Arithm.SUB", Func.SUB));

    public static Object[] SUB (Object[] a) {
            Object z = (a.length==2 ? SUB(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object SUB (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return norm_long    (x.longValue() - y.longValue())  ;
            case FOD: return new Double (x.doubleValue() - y.doubleValue());
            case BIG: return norm_Big     (toBig(x). subtract (toBig(y)))  ;
             default: return null; // Fail
        }
    }

    // $ENTRY MUL
    public static final Object MUL = Term.getExternal("MUL","*",
        new Func("Arithm.MUL", Func.MUL));

    public static Object[] MUL (Object[] a) {
            Object z = (a.length==2 ? MUL(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object MUL (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return norm_long    (x.longValue() * y.longValue())  ;
            case FOD: return new Double (x.doubleValue() * y.doubleValue());
            case BIG: return norm_Big     (toBig(x). multiply (toBig(y)))  ;
             default: return null; // Fail
        }
    }

    // $ENTRY DIV
    public static final Object DIV = Term.getExternal("DIV","/",
        new Func("Arithm.DIV", Func.DIV));

    public static Object[] DIV (Object[] a) {
            Object z = (a.length==2 ? DIV(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object DIV (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return norm_long    (x.longValue() / y.longValue())  ;
            case FOD: return new Double (x.doubleValue() / y.doubleValue());
            case BIG: return norm_Big      (toBig(x). divide (toBig(y)))   ;
            default:  return null; // Fail
        }
    }

    // $ENTRY REM
    public static final Object REM = Term.getExternal("REM","%",
        new Func("Arithm.REM", Func.REM));

    public static Object[] REM (Object[] a) {
            Object z = (a.length==2 ? REM(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object REM (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return new Integer   (x.intValue() % y.intValue())   ;
            case FOD: return new Double (x.doubleValue() % y.doubleValue());
            case BIG: return norm_Big     (toBig(x). remainder (toBig(y))) ;
             default: return null; // Fail
        }
    }

    // $ENTRY DIVREM
    public static final Object DIVREM = Term.getExternal("DIVREM",
        new Func("Arithm.DIVREM", Func.DIVREM));

    public static Object[] DIVREM (Object a[]) {
        if (!(a.length==2)) return null;
        Number x = getNumber(a[0]);
        Number y = getNumber(a[1]);
        Number z;
        Number w;
        switch (Math.min(sort(x), sort(y))) {
            case INT:
                z = norm_long    (x.longValue() / y.longValue())  ;
                w = norm_long    (x.longValue() % y.longValue())  ;
                break;
            case FOD:
                z = new Double (x.doubleValue() / y.doubleValue());
                w = new Double (x.doubleValue() % y.doubleValue());
                break;
            case BIG:
                z = norm_Big        (toBig(x). divide    (toBig(y)))    ;
                w = norm_Big        (toBig(x). remainder (toBig(y)))    ;
                break;
            default: return null; // Fail
        }
        return new Object[] {z,w};
    }

    // $ENTRY ABS
    public static final Object ABS = Term.getExternal("ABS",
        new Func("Arithm.ABS", Func.ABS));

    public static Object[] ABS (Object[] a) {
            Object z = (a.length==1 ? ABS(a[0]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object ABS (Object a) {
        Number x = getNumber(a);
        switch (sort(x)) {
            case INT:
            case BIG: return norm_Big (toBig(x).abs()) ;
            case FOD: return new Double (Math.abs (x.doubleValue()));
             default: return null; // Fail
        }
    }

    // $ENTRY SIGN
    public static final Object SIGN = Term.getExternal("SIGN",
        new Func("Arithm.SIGN", Func.SIGN));

    public static Object[] SIGN (Object[] a) {
            Object z = (a.length==1 ? SIGN(a[0]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object SIGN (Object a) {
        Number x = getNumber(a);
        switch (sort(x)) {
            case FOD: double u = x.doubleValue(); return new Integer(u>0? 1 : (u<0? -1 : 0));
            case INT: int    v = x.   intValue(); return new Integer(v>0? 1 : (v<0? -1 : 0));
            case BIG: return new Integer (((BigInteger)x).signum()) ;
             default: return null; // Fail
        }
    }

    // $ENTRY POW
    public static final Object POW = Term.getExternal("POW",
        new Func("Arithm.POW", Func.POW));


    public static Object[] POW (Object[] a) {
            Object z = (a.length==2 ? POW(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    private static double pow(double x, int d) {
        double z = 1.0;
        while (d>0) {
            if (d%2==0) { x=x*x; d=d/2;}
            else { z=z*x; d=d-1; }
        }
        return z;
    }

    public static Object POW (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        if (!(x instanceof Integer)) return null;
        int d = x.intValue();
        if (d<0) return null;
        switch (sort(y)) {
            case FOD: return new Double (pow(y.doubleValue(),d));
            case INT:
            case BIG: return norm_Big (toBig(y).pow(d));
            default: return null; // Fail
        }
    }

    // $ENTRY GCD
    public static final Object GCD = Term.getExternal("GCD",
        new Func("Arithm.GCD", Func.GCD));

    public static Object[] GCD (Object[] a) {
            Object z = (a.length==2 ? GCD(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    static private int gcd (int a, int b) {
        if (a<0) a = -a;
        if (b<0) b = -b;
        while (b!=0) {
            int t = b;
            b = a%b;
            a = t;
        }
        return a;
    }

    public static Object GCD (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return new Integer (gcd (x.intValue(),y.intValue())) ;
            case BIG: return norm_Big     (toBig(x).gcd (toBig(y))) ;
             default: return null; // Fail
        }
    }
/*
( ABS             (C "rb_abs"    ) )
( SIGN            (C "rb_sign"   ) )
( SQRT            (C "rb_sqrt"   ) )
( EXP             (C "rb_exp"    ) )
( LOG             (C "rb_log"    ) )
( SIN             (C "rb_sin"    ) )
( ASIN            (C "rb_asin"   ) )
( SINH            (C "rb_sinh"   ) )
( COS             (C "rb_cos"    ) )
( ACOS            (C "rb_acos"   ) )
( COSH            (C "rb_cosh"   ) )
( TAN             (C "rb_tan"    ) )
( ATAN            (C "rb_atan"   ) )
( TANH            (C "rb_tanh"   ) )
*/
    // $ENTRY SQRT
    public static final Object SQRT = Term.getExternal("SQRT",
        new Func("Arithm.SQRT", Func.SQRT));

    public static Object[] SQRT (Object[] a) {
        Object z = (a.length==1 ? SQRT(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object SQRT (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.sqrt (x.doubleValue()));
    }

    // $ENTRY EXP
    public static final Object EXP = Term.getExternal("EXP",
        new Func("Arithm.EXP", Func.EXP));

    public static Object[] EXP (Object[] a) {
        Object z = (a.length==1 ? EXP(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object EXP (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.exp (x.doubleValue()));
    }

    // $ENTRY LOG
    public static final Object LOG = Term.getExternal("LOG",
        new Func("Arithm.LOG", Func.LOG));

    public static Object[] LOG (Object[] a) {
        Object z = (a.length==1 ? LOG(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object LOG (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.log (x.doubleValue()));
    }

    // $ENTRY SIN
    public static final Object SIN = Term.getExternal("SIN",
        new Func("Arithm.SIN", Func.SIN));

    public static Object[] SIN (Object[] a) {
        Object z = (a.length==1 ? SIN(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object SIN (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.sin (x.doubleValue()));
    }

    // $ENTRY ASIN
    public static final Object ASIN = Term.getExternal("ASIN",
        new Func("Arithm.ASIN", Func.ASIN));

    public static Object[] ASIN (Object[] a) {
        Object z = (a.length==1 ? ASIN(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object ASIN (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.asin (x.doubleValue()));
    }

    // $ENTRY COS
    public static final Object COS = Term.getExternal("COS",
        new Func("Arithm.COS", Func.COS));

    public static Object[] COS (Object[] a) {
        Object z = (a.length==1 ? COS(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object COS (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.cos (x.doubleValue()));
    }

    // $ENTRY ACOS
    public static final Object ACOS = Term.getExternal("ACOS",
        new Func("Arithm.ACOS", Func.ACOS));

    public static Object[] ACOS (Object[] a) {
        Object z = (a.length==1 ? ACOS(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object ACOS (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.acos (x.doubleValue()));
    }

    // $ENTRY TAN
    public static final Object TAN = Term.getExternal("TAN",
        new Func("Arithm.TAN", Func.TAN));

    public static Object[] TAN (Object[] a) {
        Object z = (a.length==1 ? TAN(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object TAN (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.tan (x.doubleValue()));
    }

    // $ENTRY ATAN
    public static final Object ATAN = Term.getExternal("ATAN",
        new Func("Arithm.ATAN", Func.ATAN));

    public static Object[] ATAN (Object[] a) {
        Object z = (a.length==1 ? ATAN(a[0]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object ATAN (Object a) {
        Number x = getNumber(a);
        if (x==null) return null;
        return new Double (Math.atan (x.doubleValue()));
    }

    // $ENTRY ATAN2
    public static final Object ATAN2 = Term.getExternal("ATAN2",
        new Func("Arithm.ATAN2", Func.ATAN2));

    public static Object[] ATAN2 (Object[] a) {
        Object z = (a.length==2 ? ATAN2(a[0],a[1]) : null);
        return (z==null? null : new Object[] {z});
    }

    public static Object ATAN2 (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        if (x==null || y==null) return null;
        return new Double (Math.atan2 (x.doubleValue(),y.doubleValue()));
    }

/************************************************************************/

    // $ENTRY AND
    public static final Object AND = Term.getExternal("AND","&",
        new Func("Arithm.AND", Func.AND));

    public static Object[] AND (Object[] a) {
            Object z = (a.length==2 ? AND(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object AND (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return new Integer (x.intValue() & y.intValue()) ;
            case BIG: return norm_Big     (toBig(x).and (toBig(y))) ;
             default: return null; // Fail
        }
    }

    // $ENTRY XOR
    public static final Object XOR = Term.getExternal("XOR","^",
        new Func("Arithm.XOR", Func.XOR));

    public static Object[] XOR (Object[] a) {
            Object z = (a.length==2 ? XOR(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object XOR (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return new Integer (x.intValue() ^ y.intValue()) ;
            case BIG: return norm_Big     (toBig(x).xor (toBig(y))) ;
             default: return null; // Fail
        }
    }

    // $ENTRY OR
    public static final Object OR = Term.getExternal("OR","|",
        new Func("Arithm.OR", Func.OR));

    public static Object[] OR (Object[] a) {
            Object z = (a.length==2 ? OR(a[0],a[1]) : null);
            return (z==null? null : new Object[] {z});
        }

    public static Object OR (Object a, Object b) {
        Number x = getNumber(a);
        Number y = getNumber(b);
        switch (Math.min(sort(x), sort(y))) {
            case INT: return new Integer (x.intValue() | y.intValue()) ;
            case BIG: return norm_Big     (toBig(x).or (toBig(y))) ;
             default: return null; // Fail
        }
    }

    // $ENTRY RANDOM
    public static final Object RANDOM = Term.getExternal("RANDOM",
        new Func("Arithm.RANDOM", Func.RANDOM));

    public static Object[] RANDOM (Object[] a) {
            Object z = (a.length==0 ? new Double(Math.random()) : null);
            return (z==null? null : new Object[] {z});
        }
}