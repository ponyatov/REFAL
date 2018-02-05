package  org.refal.j;
public class Trace {

// $ENTRY TRACE
public static final Object TRACE = Term.getExternal("TRACE",
    new org.refal.j.Function("Trace.TRACE") {
      public Object[] eval(Object[] e) throws Exception { return TRACE(e); }
    });

// $Func TRACE e.names = ;
//  TRACE e.names = <Trace e.names>;
  public static Object[] TRACE(Object[] a) { return TraceData.Trace(a); }

//$Func Begin s.mod s.fun (e.arg) = ;
//Begin s:String.mod s:String.fun (e.arg) = <"Trace_r.Begin" s.mod s.fun (e.arg)> =;
  public static void Begin(String mod, String fun, Object[] a) throws Exception {
      try {
        Trace_r.Begin(new Object[] {mod,fun,a});
      } catch (Exception e) { throw new Error("Trace.Begin"); };
  }

//$Func End e.res = e.res;
//End e.res = <"Trace_r.End" e.res> = e.res;
  public static Object[] End(Object[] a) throws Exception {
      try {
              Trace_r.End(a);
      } catch (Exception e) { throw new Error("Trace.End"); };
      return a;
  }

//$Func Fail = $Fail;
//Fail e.arg = <"Trace_r.Fail" e.arg> = $Fail;
  public static Object[] Fail() throws Exception {
      try {
          Trace_r.Fail(EMPTY);
      } catch (Exception e) { throw new Error("Trace.Fail"); };
      return null;
  }

//$Func Error s.mod s.fun (e.arg) = ;
//Error s:String.mod s:String.fun (e.arg) = <"Trace_r.Error" s.mod s.fun (e.arg)> =;
  public static Object[] Error(String mod, String fun, Object[] a) throws Exception {
      try {
          Trace_r.Error(new Object[] {mod,fun,a});
      } catch (Exception e) { throw new Error("Trace.Error"); };
       throw new Error("Unexpected fail in "+mod+"."+fun);
  }

  public static Object[] Catch(String mod, String fun, Object[] a, Object[] r) throws Exception {
      if (r==null) {
          try {
              Trace_r.Catch(new Object[] {mod,fun,a});
          } catch (Exception e) { throw new Error("Trace.Catch"); };
          throw new Error("Unexpected fail from "+mod+"."+fun);
      }
      else
          return r;
  }

  private static final Object[] EMPTY = {};

  static Object[] fail(int dbglvl, String msg) {
      if (dbglvl<5) System.out.println(msg);
     // throw new java.lang.Error();
      return null;
      }

}


