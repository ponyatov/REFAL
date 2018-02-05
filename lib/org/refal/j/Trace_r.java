package  org.refal.j;
import org.refal.j.*;
public class Trace_r {

  public static final Reference Begin = new Reference("Begin");
  static { Begin.defineReferable(
    new org.refal.j.Function("Trace_r.Begin") {
      public Object[] eval(Object[] e) throws Exception {
        return Begin(e);
      } /* eval */
    });
  }

  public static Object[] Begin(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==3 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          if (!( !(a[1] instanceof Object[]) )) break Fail;
          if (!( (a[2] instanceof Object[]) )) break Fail;
          Object[] p = (Object[])a[2];
          int lp = p.length;
          Object[] r = TraceData.GetStep(cExpr);
          if ( r==null ) break Fail;
          int lr = r.length;
          if (!( lr==1 )) break Fail;
          if (!( !(r[0] instanceof Object[]) )) break Fail;
          Object[] r_1 = TraceData.NextStep(cExpr);
          if ( r_1==null ) break Fail;
          Object[] r_2 = new Object[la+1];
          r_2[0] = r[0];
          System.arraycopy(a, 0, r_2, 1, la);
          Object[] r_3 = TraceData.Push(r_2);
          if ( r_3==null ) break Fail;
          Object[] r_4 = new Object[]{a[1]};
          Object[] r_5 = TraceData.ChkName(r_4);
          if ( r_5==null ) break Fail;
          Object[] r_6 = new Object[lp+1];
          r_6[0] = "M";
          System.arraycopy(p, 0, r_6, 1, lp);
          Object[] r_7 = new Object[]{r[0], ": <", a[1], " ", r_6, ">"};
          Object[] r_8 = TraceMsg(r_7);
          if ( r_8==null ) break Error;
          result = r_8;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Begin */

  public static final Reference End = new Reference("End");
  static { End.defineReferable(
    new org.refal.j.Function("Trace_r.End") {
      public Object[] eval(Object[] e) throws Exception {
        return End(e);
      } /* eval */
    });
  }

  public static Object[] End(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          Object[] r = TraceData.Pop(cExpr);
          if ( r==null ) break Fail;
          int lr = r.length;
          if (!( lr==4 )) break Fail;
          if (!( !(r[0] instanceof Object[]) )) break Fail;
          if (!( !(r[1] instanceof Object[]) )) break Fail;
          if (!( !(r[2] instanceof Object[]) )) break Fail;
          if (!( (r[3] instanceof Object[]) )) break Fail;
          Object[] p = (Object[])r[3];
          int lp = p.length;
          Object[] r_1 = new Object[]{r[2]};
          Object[] r_2 = TraceData.ChkName(r_1);
          if ( r_2==null ) break Fail;
          Object[] r_3 = TraceData.GetStep(cExpr);
          if ( r_3==null ) break Error;
          int lr_3 = r_3.length;
          if (!( lr_3==1 )) break Error;
          if (!( !(r_3[0] instanceof Object[]) )) break Error;
          Object[] r_4 = new Object[la+1];
          r_4[0] = "M";
          System.arraycopy(a, 0, r_4, 1, la);
          Object[] r_5 = new Object[]{r[0], "-", r_3[0], ": <", r[2], " ...> -> ", r_4};
          Object[] r_6 = TraceMsg(r_5);
          if ( r_6==null ) break Error;
          result = r_6;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* End */

  public static final Reference Fail = new Reference("Fail");
  static { Fail.defineReferable(
    new org.refal.j.Function("Trace_r.Fail") {
      public Object[] eval(Object[] e) throws Exception {
        return Fail(e);
      } /* eval */
    });
  }

  public static Object[] Fail(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==0 )) break Fail;
          Object[] r = TraceData.Pop(cExpr);
          if ( r==null ) break Fail;
          int lr = r.length;
          if (!( lr==4 )) break Fail;
          if (!( !(r[0] instanceof Object[]) )) break Fail;
          if (!( !(r[1] instanceof Object[]) )) break Fail;
          if (!( !(r[2] instanceof Object[]) )) break Fail;
          if (!( (r[3] instanceof Object[]) )) break Fail;
          Object[] p = (Object[])r[3];
          int lp = p.length;
          Object[] r_1 = new Object[]{r[2]};
          Object[] r_2 = TraceData.ChkName(r_1);
          if ( r_2==null ) break Fail;
          Object[] r_3 = TraceData.GetStep(cExpr);
          if ( r_3==null ) break Error;
          int lr_3 = r_3.length;
          if (!( lr_3==1 )) break Error;
          if (!( !(r_3[0] instanceof Object[]) )) break Error;
          Object[] r_4 = new Object[]{r[0], "-", r_3[0], ": <", r[2], " ...> -> $FAIL"};
          Object[] r_5 = TraceMsg(r_4);
          if ( r_5==null ) break Error;
          result = r_5;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Fail */

  public static final Reference Error = new Reference("Error");
  static { Error.defineReferable(
    new org.refal.j.Function("Trace_r.Error") {
      public Object[] eval(Object[] e) throws Exception {
        return Error(e);
      } /* eval */
    });
  }

  public static Object[] Error(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==3 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          if (!( !(a[1] instanceof Object[]) )) break Fail;
          if (!( (a[2] instanceof Object[]) )) break Fail;
          Object[] p = (Object[])a[2];
          int lp = p.length;
          Object[] r = TraceData.Pop(cExpr);
          if ( r==null ) break Error;
          int lr = r.length;
          if (!( lr>=1 )) break Error;
          if (!( !(r[0] instanceof Object[]) )) break Error;
          Object[] r_1 = TraceData.GetStep(cExpr);
          if ( r_1==null ) break Error;
          int lr_1 = r_1.length;
          if (!( lr_1==1 )) break Error;
          if (!( !(r_1[0] instanceof Object[]) )) break Error;
          Object[] r_2 = new Object[lp+1];
          r_2[0] = "M";
          System.arraycopy(p, 0, r_2, 1, lp);
          Object[] r_3 = new Object[]{"ERROR: STEP ", r_1[0], ": <", a[1], " ", r_2, ">"};
          Object[] r_4 = TraceMsg(r_3);
          if ( r_4==null ) break Error;
          result = r_4;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Error */

  public static final Reference Catch = new Reference("Catch");
  static { Catch.defineReferable(
    new org.refal.j.Function("Trace_r.Catch") {
      public Object[] eval(Object[] e) throws Exception {
        return Catch(e);
      } /* eval */
    });
  }

  public static Object[] Catch(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          if (!( la==3 )) break Fail;
          if (!( !(a[0] instanceof Object[]) )) break Fail;
          if (!( !(a[1] instanceof Object[]) )) break Fail;
          if (!( (a[2] instanceof Object[]) )) break Fail;
          Object[] p = (Object[])a[2];
          int lp = p.length;
          Object[] r = TraceData.GetStep(cExpr);
          if ( r==null ) break Error;
          int lr = r.length;
          if (!( lr==1 )) break Error;
          if (!( !(r[0] instanceof Object[]) )) break Error;
          Object[] r_1 = new Object[lp+1];
          r_1[0] = "M";
          System.arraycopy(p, 0, r_1, 1, lp);
          Object[] r_2 = new Object[]{"ERROR: STEP ", r[0], ": Unexpected fail in <", a[1], " ", r_1, ">"};
          Object[] r_3 = TraceMsg(r_2);
          if ( r_3==null ) break Error;
          result = r_3;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
      return null;
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* Catch */

  private static Object[] TraceMsg(Object[] a) throws Exception {
    Error: {
      Object[] result;
      int la = a.length;
      Fail: {
        Norm: {
          M: {
            M_1: for (int jA = 0; la-jA>=1; jA++) {
              if (!( (a[jA] instanceof Object[]) )) continue M_1;
              Object[] p = (Object[])a[jA];
              int lp = p.length;
              if (!( lp>=1 )) continue M_1;
              if (!( Lang.termsEqual(p[0], "M") )) continue M_1;
              Object[] r = new Object[jA];
              System.arraycopy(a, 0, r, 0, jA);
              Object[] r_1 = Stream.PRINT(r);
              if ( r_1==null ) break Error;
              int z = lp-1;
              Object[] r_2 = new Object[z];
              System.arraycopy(p, 1, r_2, 0, z);
              Object[] r_3 = Stream.WRITE(r_2);
              if ( r_3==null ) break Error;
              int z_1 = la-jA-1;
              Object[] r_4 = new Object[z_1];
              System.arraycopy(a, jA+1, r_4, 0, z_1);
              Object[] r_5 = TraceMsg(r_4);
              if ( r_5==null ) break Error;
              result = r_5;
              break Norm;
            } /* M_1: */
            break M;
          } /* M: */
          Object[] r_6 = Stream.PRINTLN(a);
          if ( r_6==null ) break Error;
          result = r_6;
          break Norm;
        } /* Norm: */
        return result;
      } /* Fail: */
    } /* Error: */
    throw new org.refal.j.Error("Unexpected fail");
  } /* TraceMsg */

  private static final Object[] cExpr = new Object[]{}; /*  */
}
