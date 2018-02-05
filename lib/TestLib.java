import org.refal.j.*;
public class TestLib {
  private static final Reference GO = Term.getExternal("GO");

  private static final Reference A = new Reference("A");
  static { try {
    A.setReferable(Reference.make("BOX"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference N = new Reference("N");
  static { try {
    N.setReferable(Reference.make("EMPTY"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference S = new Reference("S");
  static { try {
    S.setReferable(Reference.make("STRING"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference R = new Reference("R");
  static { try {
    R.setReferable(Reference.make("EMPTY"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference T = new Reference("T");
  static { try {
    T.setReferable(Reference.make("TABLE"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference X = new Reference("X");
  static { try {
    X.setReferable(Reference.make("TABLE"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference Y = new Reference("Y");
  static { try {
    Y.setReferable(Reference.make("TABLE"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static final Reference Z = new Reference("Z");
  static { try {
    Z.setReferable(Reference.make("TABLE"));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }

  private static Object[] Init(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==0 )) break Fail;
          Object[] r = Real.REAL(cExpr);
          if ( r==null ) break Error;
          Object[] r_1 = new Object[r.length+1];
          System.arraycopy(r, 0, r_1, 0, r.length);
          r_1[r.length] = cSym_1;
          Object[] r_2 = Arithm.DIV(r_1);
          if ( r_2==null ) break Error;
          Object[] r_3 = new Object[r_2.length+1];
          r_3[0] = R;
          System.arraycopy(r_2, 0, r_3, 1, r_2.length);
          Object[] r_4 = Reference.SET(r_3);
          if ( r_4==null ) break Error;
          Object[] r_5 = Reference.SET(cExpr_1);
          if ( r_5==null ) break Error;
          Object[] r_6 = Expr.Map(cExpr_2);
          if ( r_6==null ) break Error;
          Object[] r_7 = new Object[r_6.length+1];
          r_7[0] = A;
          System.arraycopy(r_6, 0, r_7, 1, r_6.length);
          Object[] r_8 = Container.SETV(r_7);
          if ( r_8==null ) break Error;
          Object[] r_9 = Table.TABLE_LINK(cExpr_3);
          if ( r_9==null ) break Error;
          Object[] r_10 = Table.TABLE_VALUE(cExpr_4);
          if ( r_10==null ) break Error;
          Object[] r_11 = new Object[r_10.length+1];
          r_11[0] = A;
          System.arraycopy(r_10, 0, r_11, 1, r_10.length);
          Object[] r_12 = Reference.COPY(r_11);
          if ( r_12==null ) break Error;
          Object[] r_13 = Container.SETV(cExpr_5);
          if ( r_13==null ) break Error;
          result = r_13;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Init */

  private static final Reference Desc = new Reference("Desc");
  static { Desc.setReferable(
    new org.refal.j.Function("TestLib.Desc") {
      public Object[] eval(Object[] e) throws Exception {
        return Desc(e);
      } /* eval */
    });
  }

  private static Object[] Desc(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==1 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          Object[] r = Term.MODE(a);
          if ( r==null ) break Error;
          Object[] r_1 = Reference.NAME(a);
          if ( r_1==null ) break Error;
          Object[] r_2 = new Object[r.length+r_1.length+1];
          System.arraycopy(r, 0, r_2, 0, r.length);
          System.arraycopy(r_1, 0, r_2, r.length, r_1.length);
          int z = r.length+r_1.length;
          r_2[z] = a[0];
          Object[] r_3 = new Object[]{r_2};
          result = r_3;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Desc */

  private static Object[] Repeat(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          M: {
            if (!( la>=1 )) break M;
            if (!( Lang.termsEqual(a[0], cSym_12) )) break M;
            int z = la-1;
            Object[] r = new Object[z];
            System.arraycopy(a, 1, r, 0, z);
            Object[] r_1 = Apply.APPLY(r);
            if ( r_1==null ) break Error;
            result = r_1;
            break Norm;
          } /* M: */
          if (!( la>=1 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          Object[] r_2 = new Object[]{a[0], cSym_13};
          Object[] r_3 = Arithm.DIV(r_2);
          if ( r_3==null ) break Error;
          int lr_3 = r_3.length;
          if (!( lr_3==1 )) break Error;
          if (!( !(r_3[0] instanceof Object[]) )) break Error;
          Object[] r_4 = new Object[]{a[0], r_3[0]};
          Object[] r_5 = Arithm.SUB(r_4);
          if ( r_5==null ) break Error;
          Object[] r_6 = new Object[r_5.length+la-1];
          System.arraycopy(r_5, 0, r_6, 0, r_5.length);
          System.arraycopy(a, 1, r_6, r_5.length, la-1);
          Object[] r_7 = Repeat(r_6);
          if ( r_7==null ) break Error;
          Object[] r_8 = (Object[]) a.clone();
          r_8[0] = r_3[0];
          Object[] r_9 = Repeat(r_8);
          if ( r_9==null ) break Error;
          result = r_9;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Repeat */

  private static final Reference Dummy = new Reference("Dummy");
  static { Dummy.setReferable(
    new org.refal.j.Function("TestLib.Dummy") {
      public Object[] eval(Object[] e) throws Exception {
        return Dummy(e);
      } /* eval */
    });
  }

  private static Object[] Dummy(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==0 )) break Fail;
          result = cExpr_6;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
  } /* Dummy */

  private static Object[] EvalTime(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          M: {
            if (!( la>=2 )) break M;
            if (!( Lang.termsEqual(a[0], cSym_14) )) break M;
            if (!( !(a[1] instanceof Object[]) )) break M;
            int z = la-1;
            Object[] r = new Object[z];
            System.arraycopy(a, 1, r, 0, z);
            Object[] r_1 = Apply.APPLY(r);
            if ( r_1==null ) break Error;
            result = r_1;
            break Norm;
          } /* M: */
          if (!( la>=2 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          if (!( !(a[1] instanceof Object[]) )) break Fail;
          Object[] r_2 = Env.TIME_ELAPSED(cExpr_6);
          if ( r_2==null ) break Error;
          int lr_2 = r_2.length;
          if (!( lr_2==1 )) break Error;
          Object[] r_3 = Repeat(a);
          if ( r_3==null ) break Error;
          int lr_3 = r_3.length;
          Object[] r_4 = Env.TIME_ELAPSED(cExpr_6);
          if ( r_4==null ) break Error;
          int lr_4 = r_4.length;
          if (!( lr_4==1 )) break Error;
          Object[] r_5 = new Object[]{r_4[0], r_2[0]};
          Object[] r_6 = Arithm.SUB(r_5);
          if ( r_6==null ) break Error;
          Object[] r_7 = Real.REAL(cExpr_7);
          if ( r_7==null ) break Error;
          Object[] r_8 = new Object[r_6.length+r_7.length];
          System.arraycopy(r_6, 0, r_8, 0, r_6.length);
          System.arraycopy(r_7, 0, r_8, r_6.length, r_7.length);
          Object[] r_9 = Arithm.DIV(r_8);
          if ( r_9==null ) break Error;
          Object[] r_10 = new Object[r_9.length+1];
          System.arraycopy(r_9, 0, r_10, 0, r_9.length);
          r_10[r_9.length] = a[0];
          Object[] r_11 = Arithm.DIV(r_10);
          if ( r_11==null ) break Error;
          int lr_11 = r_11.length;
          if (!( lr_11==1 )) break Error;
          Object[] r_12 = new Object[]{"Eval ", a[0], " times function ", a[1], ". Average time is ", r_11[0], " sec"};
          Object[] r_13 = Stream.PRINTLN(r_12);
          if ( r_13==null ) break Error;
          result = r_3;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* EvalTime */

  private static final Reference Decode = new Reference("Decode");
  static { Decode.setReferable(
    new org.refal.j.Function("TestLib.Decode") {
      public Object[] eval(Object[] e) throws Exception {
        return Decode(e);
      } /* eval */
    });
  }

  private static Object[] Decode(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          Object[] r = Container.SETV(cExpr_8);
          if ( r==null ) break Error;
          Object[] r_1 = new Object[la+1];
          r_1[0] = Y;
          System.arraycopy(a, 0, r_1, 1, la);
          Object[] r_2 = Stream.DECODE(r_1);
          if ( r_2==null ) break Error;
          result = r_2;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Decode */

  private static Object[] TestDecode(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la>=1 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          Object[] r = (Object[]) a.clone();
          r[0] = X;
          Object[] r_1 = Stream.ENCODE(r);
          if ( r_1==null ) break Error;
          int lr_1 = r_1.length;
          Object[] r_2 = Stream.PRINTLN(r_1);
          if ( r_2==null ) break Error;
          Object[] r_3 = new Object[lr_1+2];
          r_3[0] = a[0];
          r_3[1] = Decode;
          System.arraycopy(r_1, 0, r_3, 2, lr_1);
          Object[] r_4 = EvalTime(r_3);
          if ( r_4==null ) break Error;
          int lr_4 = r_4.length;
          if (!( lr_4>=1 )) break Error;
          if (!( !(r_4[0] instanceof Object[]) )) break Error;
          Object[] r_5 = Stream.WRITELN(r_4);
          if ( r_5==null ) break Error;
          Object[] r_6 = (Object[]) r_4.clone();
          r_6[0] = Z;
          Object[] r_7 = Stream.ENCODE(r_6);
          if ( r_7==null ) break Error;
          int lr_7 = r_7.length;
          Object[] r_8 = Stream.PRINTLN(r_7);
          if ( r_8==null ) break Error;
          Object[] r_9 = new Object[r_8.length+lr_1];
          System.arraycopy(r_8, 0, r_9, 0, r_8.length);
          System.arraycopy(r_1, 0, r_9, r_8.length, lr_1);
          M: {
            M_1: {
              int lr_9 = r_9.length;
              if (!( lr_9-lr_7==0 )) break M_1;
              int jt = lr_7;
              if (!( Lang.exprsEqual(r_9, 0, r_7, 0, lr_7) )) break M_1;
              Object[] r_10 = Stream.PRINTLN(cExpr_9);
              if ( r_10==null ) break Error;
              result = r_10;
              break M;
            } /* M_1: */
            int lr_9 = r_9.length;
            Object[] r_11 = Stream.PRINTLN(cExpr_10);
            if ( r_11==null ) break Error;
            result = r_11;
            break M;
          } /* M: */
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* TestDecode */

  private static Object[] TestMake(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la>=1 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          Object[] r = new Object[la+2];
          r[0] = a[0];
          r[1] = Expr.Map;
          r[2] = Reference.MAKE;
          System.arraycopy(a, 1, r, 3, la-1);
          Object[] r_1 = EvalTime(r);
          if ( r_1==null ) break Error;
          int lr_1 = r_1.length;
          Object[] r_2 = new Object[lr_1+1];
          r_2[0] = Term.MODE;
          System.arraycopy(r_1, 0, r_2, 1, lr_1);
          Object[] r_3 = Expr.Map(r_2);
          if ( r_3==null ) break Error;
          Object[] r_4 = new Object[r_3.length+1];
          r_4[0] = "Result";
          System.arraycopy(r_3, 0, r_4, 1, r_3.length);
          Object[] r_5 = Stream.WRITELN(r_4);
          if ( r_5==null ) break Error;
          result = r_5;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* TestMake */

  static { GO.setReferable(
    new org.refal.j.Function("TestLib.GO") {
      public Object[] eval(Object[] e) throws Exception {
        return GO(e);
      } /* eval */
    });
  }

  private static Object[] GO(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==0 )) break Fail;
          Object[] r = Container.SHIFT(cExpr_11);
          if ( r==null ) break Error;
          Object[] r_1 = Container.GETV(r);
          if ( r_1==null ) break Error;
          Object[] r_2 = Main(r_1);
          if ( r_2==null ) break Error;
          Object[] r_3 = new Object[r_2.length+1];
          r_3[0] = Stream.STDIN;
          System.arraycopy(r_2, 0, r_3, 1, r_2.length);
          result = r_3;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* GO */

  public static Object[] Main(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          M: {
            if (!( la>=1 )) break M;
            Object[] r = Container.GETV(cExpr_12);
            if ( r==null ) break Error;
            Object[] r_1 = Stream.WRITELN(r);
            if ( r_1==null ) break Error;
            Object[] r_2 = new Object[1];
            System.arraycopy(a, 0, r_2, 0, 1);
            Object[] r_3 = Container.GETV(r_2);
            if ( r_3==null ) break Error;
            Object[] r_4 = Convert.NUMB(r_3);
            if ( r_4==null ) break Error;
            int lr_4 = r_4.length;
            if (!( lr_4==1 )) break Error;
            if (!( !(r_4[0] instanceof Object[]) )) break Error;
            Object[] r_5 = new Object[]{r_4[0], Dummy};
            Object[] r_6 = EvalTime(r_5);
            if ( r_6==null ) break Error;
            Object[] r_7 = new Object[]{r_4[0], cSym_16, cSym_17, cSym_18, cSym_19, cSym_20, A};
            Object[] r_8 = TestDecode(r_7);
            if ( r_8==null ) break Error;
            Object[] r_9 = new Object[]{r_4[0], "CHAIN", "VECTOR", "MASK", "STRING", "REAL", "TABLE"};
            Object[] r_10 = TestMake(r_9);
            if ( r_10==null ) break Error;
            result = r_10;
            break Norm;
          } /* M: */
          if (!( la==0 )) break Fail;
          Object[] r_11 = Main(cExpr_13);
          if ( r_11==null ) break Error;
          result = r_11;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Main */

  public static void main(String[] args) throws Exception { Main(args); }

  private static final Object cSym = Arithm.valueOf("314");
  private static final Object cSym_1 = Arithm.valueOf("100");
  private static final Object cSym_2 = Arithm.valueOf("17");
  private static final Object cSym_3 = new Character('U');
  private static final Object cSym_4 = new Character('a');
  private static final Object cSym_5 = new Character('b');
  private static final Object cSym_6 = new Character('c');
  private static final Object cSym_7 = new Character('\n');
  private static final Object cSym_8 = new Character('\t');
  private static final Object cSym_9 = new Character('d');
  private static final Object cSym_10 = new Character('e');
  private static final Object cSym_11 = new Character('f');
  private static final Object cSym_12 = Arithm.valueOf("1");
  private static final Object cSym_13 = Arithm.valueOf("2");
  private static final Object cSym_14 = Arithm.valueOf("0");
  private static final Object cSym_15 = Arithm.valueOf("-1");
  private static final Object cSym_16 = new Character('E');
  private static final Object cSym_17 = new Character('x');
  private static final Object cSym_18 = new Character('p');
  private static final Object cSym_19 = new Character('r');
  private static final Object cSym_20 = new Character('=');
  private static final Object[] cExpr = new Object[]{cSym}; /* 314 */
  private static final Object[] cExpr_1 = new Object[]{N, cSym_2}; /* *N 17 */
  private static final Object[] cExpr_2 = new Object[]{Desc, A, N, S, R, T}; /* *Desc *A *N *S *R *T */
  private static final Object[] cExpr_3 = new Object[]{T, "A", A}; /* *T A *A */
  private static final Object[] cExpr_4 = new Object[]{T, cSym_3, "ZZZ"}; /* *T 'U' ZZZ */
  private static final Object[] cExpr_5 = new Object[]{S, cSym_4, cSym_5, cSym_6, cSym_7, cSym_8, cSym_9, cSym_10, cSym_11}; /* *S 'abc
	def' */
  private static final Object[] cExpr_6 = new Object[]{}; /*  */
  private static final Object[] cExpr_7 = new Object[]{cSym_1}; /* 100 */
  private static final Object[] cExpr_8 = new Object[]{Y}; /* *Y */
  private static final Object[] cExpr_9 = new Object[]{"PASSED"}; /* PASSED */
  private static final Object[] cExpr_10 = new Object[]{"FAILED"}; /* FAILED */
  private static final Object[] cExpr_11 = new Object[]{Env.SYSARG, cSym_14, cSym_15}; /* *SYSARG 0 -1 */
  private static final Object[] cExpr_12 = new Object[]{A}; /* *A */
  private static final Object[] cExpr_13 = new Object[]{"0"}; /* "0" */
  static { try {
    Object[] r = Trace.Catch("TestLib", "Init", cExpr_6, Init(cExpr_6));
   } catch (Exception e) { e.printStackTrace(); throw new ExceptionInInitializerError(e); } }
}
