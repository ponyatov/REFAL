package  org.refal.j;
import org.refal.j.*;
public class Util {
  private static final Object I = Term.getExternal("I");
  private static final Object CTIME = Term.getExternal("CTIME");
  private static final Object DEF = Term.getExternal("DEF");
  private static final Object NEWLIST = Term.getExternal("NEWLIST");
  private static final Object NEWMAP = Term.getExternal("NEWMAP");

  static { Reference.defineReferable(I,
    new org.refal.j.Function("Util.I") {
      public Object[] eval(Object[] e) throws Exception {
        return I(e);
      } /* eval */
    });
  }

  private static Object[] I(Object[] a) throws Exception {
    Trace.Begin("Util", "I", a);
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          result = a;
          break Norm;
        } /* Norm: */
        return Trace.End(result);
      } /* Fail: */
    } /* Error: */
  } /* I */

  static { Reference.defineReferable(CTIME,
    new org.refal.j.Function("Util.CTIME") {
      public Object[] eval(Object[] e) throws Exception {
        return CTIME(e);
      } /* eval */
    });
  }

  private static Object[] CTIME(Object[] a) throws Exception {
    Trace.Begin("Util", "CTIME", a);
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la>=1 )) break Fail;
          Object[] r = Env.TIME_ELAPSED(cExpr);
          if ( r==null ) break Fail;
          Object[] r_1 = Apply.APPLY(a);
          if ( r_1==null ) break Fail;
          Object[] r_2 = Env.TIME_ELAPSED(cExpr);
          if ( r_2==null ) break Fail;
          Object[] r_3 = new Object[r.length+r_1.length+r_2.length];
          System.arraycopy(r, 0, r_3, 0, r.length);
          System.arraycopy(r_1, 0, r_3, r.length, r_1.length);
          int z = r.length+r_1.length;
          System.arraycopy(r_2, 0, r_3, z, r_2.length);
          int lr_3 = r_3.length;
          if (!( lr_3>=2 )) break Fail;
          if (!( !(r_3[0] instanceof Object[]) )) break Fail;
          if (!( !(r_3[lr_3-1] instanceof Object[]) )) break Fail;
          Object[] r_4 = new Object[]{r_3[lr_3-1], r_3[0]};
          Object[] r_5 = Trace.Catch("Arithm", "SUB", r_4, Arithm.SUB(r_4));
          Object[] r_6 = Trace.Catch("Real", "REAL", r_5, Real.REAL(r_5));
          Object[] r_7 = new Object[r_6.length+1];
          System.arraycopy(r_6, 0, r_7, 0, r_6.length);
          r_7[r_6.length] = cSym;
          Object[] r_8 = Trace.Catch("Arithm", "DIV", r_7, Arithm.DIV(r_7));
          Object[] r_9 = new Object[r_8.length+2];
          r_9[0] = "Computation time = ";
          System.arraycopy(r_8, 0, r_9, 1, r_8.length);
          r_9[r_8.length+1] = " sec";
          Object[] r_10 = Trace.Catch("Stream", "PRINTLN", r_9, Stream.PRINTLN(r_9));
          int z_1 = lr_3-2;
          result = new Object[z_1];
          System.arraycopy(r_3, 1, result, 0, z_1);
          break Norm;
        } /* Norm: */
        return Trace.End(result);
      } /* Fail: */
      return Trace.Fail();
    } /* Error: */
  } /* CTIME */

  static { Reference.defineReferable(DEF,
    new org.refal.j.Function("Util.DEF") {
      public Object[] eval(Object[] e) throws Exception {
        return DEF(e);
      } /* eval */
    });
  }

  private static Object[] DEF(Object[] a) throws Exception {
    Trace.Begin("Util", "DEF", a);
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==2 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          Object[] r = new Object[]{Term.SYSTABLE, cSym_1, a[0]};
          Object[] r_1 = Trace.Catch("Table", "TABLE_VALUE", r, Table.TABLE_VALUE(r));
          Object[] r_2 = new Object[]{Term.SYSTABLE, a[0], a[1]};
          Object[] r_3 = Trace.Catch("Table", "TABLE_LINK", r_2, Table.TABLE_LINK(r_2));
          result = new Object[1];
          System.arraycopy(a, 1, result, 0, 1);
          break Norm;
        } /* Norm: */
        return Trace.End(result);
      } /* Fail: */
      return Trace.Fail();
    } /* Error: */
  } /* DEF */

  static { Reference.defineReferable(NEWLIST,
    new org.refal.j.Function("Util.NEWLIST") {
      public Object[] eval(Object[] e) throws Exception {
        return NEWLIST(e);
      } /* eval */
    });
  }

  private static Object[] NEWLIST(Object[] a) throws Exception {
    Trace.Begin("Util", "NEWLIST", a);
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==0 )) break Fail;
          Object[] r = Trace.Catch("Term", "NEWOBJ", cExpr_1, Term.NEWOBJ(cExpr_1));
          result = r;
          break Norm;
        } /* Norm: */
        return Trace.End(result);
      } /* Fail: */
      return Trace.Fail();
    } /* Error: */
  } /* NEWLIST */

  static { Reference.defineReferable(NEWMAP,
    new org.refal.j.Function("Util.NEWMAP") {
      public Object[] eval(Object[] e) throws Exception {
        return NEWMAP(e);
      } /* eval */
    });
  }

  private static Object[] NEWMAP(Object[] a) throws Exception {
    Trace.Begin("Util", "NEWMAP", a);
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==0 )) break Fail;
          Object[] r = Trace.Catch("Term", "NEWOBJ", cExpr_2, Term.NEWOBJ(cExpr_2));
          result = r;
          break Norm;
        } /* Norm: */
        return Trace.End(result);
      } /* Fail: */
      return Trace.Fail();
    } /* Error: */
  } /* NEWMAP */

  private static final Object cSym = Arithm.valueOf("100");
  private static final Object cSym_1 = new Character('D');
  private static final Object[] cExpr = new Object[]{}; /*  */
  private static final Object[] cExpr_1 = new Object[]{"java.util.ArrayList"}; /* "java.util.ArrayList" */
  private static final Object[] cExpr_2 = new Object[]{"java.util.HashMap"}; /* "java.util.HashMap" */
}
