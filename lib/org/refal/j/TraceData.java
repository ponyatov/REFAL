package  org.refal.j;

import java.util.*;

/*
$package org.refal.j;

$FROM TERM   $IMPORT APPEND,POS,GETN,SHIFT,GETV,SETV;
$FROM ARITHM $IMPORT ADD;

$EXPORT Trace,GetStep,NextStep,Push,Pop,ChkName;

$BOX Stack;
$BOX Names;
$BOX Step 0;

Trace e.names = <APPEND &Names e.names>;

GetStep = <GETV &Step>;

NextStep = <SETV &Step <ADD <Step> 1>>;

Push ea = <APPEND &Stack (ea)> =;

Pop = <GETN &Stack -2> : (ea) = <SHIFT &Stack -2 -1> = ea;

ChkName sf , <POS &Names sf> : sf;
*/

public class TraceData {

//private static final BOX Stack = new BOX();
  private static final Stack stack = new Stack();

//private static final BOX Names = new BOX();
  private static final Vector names = new Vector();

//private static final BOX Step = new BOX();
  private static long step = 0;

//Trace e.names = <APPEND &Names e.names>;

  public static Object[] Trace(Object[] a) {
    for (int i=0; i<a.length; i++) {
        if (a[i] instanceof Object[]) {
            // Process negative part
            Object[] n = (Object[]) a[i];
            for (int j=0; j<n.length; j++) {
                boolean b = names.removeElement(n[j]);
                if (b) System.out.println("Tracing point at "+n[j]+" is removed");
                else   System.out.println("No tracing point at "+n[j]+" was set");
            }
        }
        else {
            names.addElement(a[i]);
            System.out.println("Tracing point at "+a[i]+" is set");
        }
    }
    return empty;
  } /* Trace */

//GetStep = <GETV &Step>;

  public static Object[] GetStep(Object[] a) {
    return new Object[]{ Arithm.norm_long(step) };
  } /* GetStep */

//NextStep = <SETV &Step <ADD <Step> 1>>;

  public static Object[] NextStep(Object[] a) {
    step++;
    return empty;
  } /* NextStep */

//Push ea = <APPEND &Stack (ea)>;

  public static Object[] Push(Object[] a) {
    stack.addElement(a);
    return empty;
  } /* Push */

//Pop = <GETN &Stack -2> : (ea) = <SHIFT &Stack -2 -1> = ea;

  public static Object[] Pop(Object[] a) {
    Object r = stack.pop();
    if (! (r instanceof Object[])) return null;
    return (Object[])r;
  } /* Pop */

//ChkName sf , <POS &Names sf> : sf;

  public static Object[] ChkName(Object[] a) {
      int la = a.length;
      Fail: {
          if (!( la==1 )) break Fail;
          if (names.contains(a[0]))
            return a;
          else
            a=null;             // Comment this line to get full tracing
            break Fail;
      } /* Fail: */
      return a;
  } /* ChkName */

  private static final Object[] empty = new Object[]{}; /*  */

}
