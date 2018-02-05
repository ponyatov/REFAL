package org.refal.j;
import java.util.Date;
import java.io.*;

public class Env {

    static void setArgs(String[] a) throws Error {
        Container c = Container.getContainer(SYSARG);
        if (c==null) Reference.setReferable(SYSARG,c=(Container)Reference.make("BOX"));
        c.set(Term.EMPTY);
        for (int i=0; i<a.length; i++) {
            c.isetn(i, Convert.explode(a[i]));
        }
    }

  // $EXTBOX SYSARG
    public static final Object SYSARG = Term.getExternal("SYSARG",Reference.make("BOX"));

  // $ENTRY ARG
    public static final Object ARG = Term.getExternal("ARG",
        new org.refal.j.Function("Env.ARG") {
          public Object[] eval(Object[] e) throws Exception { return ARG(e); }
        });

    public static Object[] ARG(Object[] a) {
        if (a.length!=1) return null;
        Number n = Arithm.getNumber(a[0]);
        if (n==null) return null;
        Container c = Container.getContainer(SYSARG);
        if (c==null) return null;
        Object v = c.getn(n.intValue());
        if (v instanceof Object[]) return (Object[])v;
        else return Expr.EMPTY;
    }

  // $ENTRY TIME
    public static final Object TIME = Term.getExternal("TIME",
        new org.refal.j.Function("Env.TIME") {
          public Object[] eval(Object[] e) throws Exception { return TIME(e); }
        });
    public static Object[] TIME(Object[] a) {
        String stime = new Date().toString();
        return Convert.explode(
                    stime.substring(8,11)+
                    stime.substring(4, 8)+
                    stime.substring(24, 28)+
                    stime.substring(10,19));
    }

  // $ENTRY TIME_ELAPSED
    public static final Object TIME_ELAPSED = Term.getExternal("TIME_ELAPSED",
        new org.refal.j.Function("Env.TIME_ELAPSED") {
          public Object[] eval(Object[] e) throws Exception { return TIME_ELAPSED(e); }
        });
    private static long startTime = System.currentTimeMillis();
    static void setStartTime(long t) { startTime = t; }
    public static Object[] TIME_ELAPSED(Object[] a) {
        long t = System.currentTimeMillis();
        return new Object[] {Arithm.norm_long((t-startTime)/10)};
    }

  // $ENTRY EXIT
    public static final Object EXIT = Term.getExternal("EXIT",
        new org.refal.j.Function("Env.EXIT") {
          public Object[] eval(Object[] e) throws Exception { return EXIT(e); }
        });
    public static Object[] EXIT(Object[] a) {
        int la = a.length;
        if (la==0) System.exit(0);
        if (la>1) return null;
        Number n = Arithm.getNumber(a[0]);
        if (n==null) return null;
        System.exit(((Number)n).intValue());
        return null;
    }

  // $ENTRY GETENV
    public static final Object GETENV = Term.getExternal("GETENV",
        new org.refal.j.Function("Env.GETENV") {
          public Object[] eval(Object[] e) throws Exception { return GETENV(e); }
        });
    public static Object[] GETENV(Object[] a) {
        String name = Convert.toString(a);
        String value = System.getProperty(name);
        if (value==null) return Expr.EMPTY;
        return Convert.explode (value);
    }

  // $ENTRY LOAD
    public static final Object LOAD = Term.getExternal("LOAD",
        new org.refal.j.Function("Env.LOAD") {
          public Object[] eval(Object[] e) throws Exception { return LOAD(e); }
        });

    public static Object[] LOAD(Object[] a) {
        int la = a.length;
        if (la!=1) return null;
        if (! (a[0] instanceof String)) return null;
        String name = (String) a[0];
        try {
            Class.forName(name);
            return a;
        } catch (ClassNotFoundException e) {
            System.out.println("Env.LOAD: Module " + name + " not found");
            return Expr.EMPTY;
        }
    }

    static private String[] toStringArray(Object[] a) {
        String[] s = new String[a.length];
        for (int i=0; i<a.length; i++) {
            s[i] = (String) a[i];
        }
        return s;
    }

  // $ENTRY COMMAND t.cmd [s.inp [(e.envpars)]]
    public static final Object COMMAND = Term.getExternal("COMMAND",
        new org.refal.j.Function("Env.COMMAND") {
          public Object[] eval(Object[] e) throws Exception { return COMMAND(e); }
        });

    /**
    * $Func COMMAND t.cmd [s.inp [(e.envpars)]] = s.out s.err
    * Execute command line t.cmd with input s.inp and parameters e.envpars
    * where
    *     t.cmd is either
    *          s.cmd - a full command, e.g. "dir C:\"
    *         (s.cmdname e.args) - command name and parameters separately
    *    s.inp is a string (word) to be fed as standard input
    *    e.envpars is a list of strings (words) each like "name=value"
    *    s.out is a string (word) representing the resulting output to stdout
    *    s.err is a string (word) representing the resulting output to stderr
    */
    public static Object[] COMMAND(Object[] a) {
        int la=a.length;
        Runtime runtime = Runtime.getRuntime();
        Process process=null;
        String inp=null;
        if (la==0 || la>3) return null;
        try {
            inp = (la>=2 ? (String) a[1] : null);
            String[] envp = (la==3? toStringArray((Object[]) a[2]) : null);
            if (a[0] instanceof String) {
                process = runtime.exec((String) a[0], envp);
            } else {
                process = runtime.exec(toStringArray((Object[]) a[0]), envp);
            }
        }
        catch (ClassCastException e) { e.printStackTrace(); return null; }
        catch (IndexOutOfBoundsException e) { e.printStackTrace(); return null; } // case of empty array a[0]
        catch (IOException e) { System.out.println("Env.SYSTEM: "); e.printStackTrace(); }

        if (process==null) return null;

        ReaderThread errReader = new ReaderThread(process.getErrorStream());
        ReaderThread outReader = new ReaderThread(process.getInputStream());
        //WriterThread inWriter = new WriterThread(process.getOutputStream());
        Writer stdin = new OutputStreamWriter(process.getOutputStream());

        errReader.start();
        outReader.start();
        //inWriter.start();
        try {
            if (inp!=null)
                stdin.write(inp,0,inp.length());
            stdin.close();
            process.waitFor();
            errReader.join();
            outReader.join();
        } catch (InterruptedException ex) {
            System.out.println("Env.COMMAND: "+ex);
        }
        catch (IOException e) {
            System.out.println("Env.SYSTEM: stdin: "); e.printStackTrace();
            }
        return new Object[] { outReader.getResult(), errReader.getResult() };
    }

    static class ReaderThread extends Thread {
        StringBuffer result = new StringBuffer();
        InputStreamReader isr;
        boolean ended = false;
        ReaderThread(InputStream instrm) {
            isr = new InputStreamReader(instrm);
        }
        public void run() {
            try {
                while(! ended) {
                    int c = isr.read();
                    if (c<0) ended=true;
                    else {
                        result.append((char)c);
                        //System.out.print((char)c);
                    }
                }
            } catch (IOException ex) {
                System.out.println("Exception in ReaderThread: "+ex);
            }
        }
        String getResult() { return result.toString(); }
    }
}