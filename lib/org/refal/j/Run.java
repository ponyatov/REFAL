package org.refal.j;
import java.util.Date;
import java.io.IOException;
public class Run {

    static String[] modlist = {
        "Term",         // CONST MODE
        "Apply",        // APPLY
        "Env",          // ARG TIME TIME_ELAPSED EXIT GETENV
        "Reference",    // NAME MAKE NEW MOVE COPY SET GET
        "Container",    // GETV SETV POS GETN SETN GETS SETS SHIFT LENCONT CONCAT APPEND
        "Arithm",       // ADD SUB MUL DIV MOD DIVMOD
        "Convert",      // IMPLODE EXPLODE SYMB NUMB ORD CHR UPPER LOWER
        "Expr",         // LENGTH FIRST LAST APP MAP TYPE COMPARE
        "Stream",       // OPEN CLOSE ERASE_FILE PRINTLN PRINT PRINTLN! PRINT! TO_CHARS WRITE WRITELN WRITELN! WRITE! READ_LINE! READ_LINE READ_CHAR! READ_CHAR EOF READ READ! READLN READLN! ENCODE DECODE
        "Table",        // TABLE_LIST TABLE_LINK TABLE_VALUE TABLE_NAME
        "Trace",        // TRACE
        "Util"
        };

    static private void loadLib() {
        for (int i=0; i<modlist.length; i++) {
            try {
                //System.out.println("Loading library module " + modlist[i]);
                Class.forName("org.refal.j."+modlist[i]);
            } catch (Exception e) {
                System.out.println("Library module " + modlist[i] + " not found");
            }
        }
    }

    static private Object funByName(String fname) {
        Object fun = Term.sysTable.nameToValue(Table.OLD, fname);
        if (fun == null && fname.toLowerCase().equals(fname) ) {
            fun = Term.sysTable.nameToValue(Table.OLD, fname.toUpperCase());
        }
        return fun;
    }

    private static boolean loadNames(String list, char sep) {
        int sepIndex=0;
        for (int index = 0; sepIndex>=0; index=sepIndex+1) {
            sepIndex = list.indexOf('+',index);
            String name = list.substring(index, sepIndex>=0 ? sepIndex : list.length());
            if (name.length()>0) {
                if (name.charAt(0)=='*' || name.charAt(0)=='&') {
                    String fname = name.substring(1);
                    Object fun = funByName(fname);
                    if (fun==null) System.out.println("Unknown function name "+fname);
                    try {
                        Apply.APPLY(fun, Expr.EMPTY);
                    } catch (Exception e) {
                        System.out.println("Run: exception occurred in evaluation of function "+fname);
                        e.printStackTrace();
                    }
                    //System.out.println("name="+name+", sepIndex="+sepIndex);
                    if (sepIndex<0) return true; // When the last element in the list sarts with * or &
                } else {
                    try {
                        Class.forName(name);
                        //System.out.println("Loaded "+name);
                    } catch (Exception e) {
                        String fname = name+".rex";
                        try {
                            System.out.println("File "+name+".class not found. Trying "+fname);
                            Object[] r = Stream.readFile(fname, Term.sysTable);
                        } catch (IOException e1) {
                            System.out.println("Module " + name + " not found");
                        } catch (Exception e2) {

                        }
                    }
                }
            }
        }
        return false;
    }
/*
    static {
        System.out.println("Contents of &SYSTABLE:");
        try {
            Stream.WRITELN(Term.sysTable.igetv());
        } catch (Exception e) { e.printStackTrace(); }
    }
*/
    public static void main (String[] args) throws Error {
        // load modules: args[0] is a "+"-separated list of module names
        if (args.length==0) {
            System.out.println("REFAL-JAVA Interactive Evaluator");
            System.out.println("   Usage:");
            System.out.println("java org.refal.j.Run name1+name2+...+nameK arg2 arg3 ...");
            System.out.println("");
            System.out.println("where namei - class names of preloaded refal modules");
            System.out.println("      which are loaded successively by means of Class.forName method");
            System.out.println("      argN - will be available as <ARG sN>");
            System.out.println("Instead of 'nameI' you may write '*Fun' in which case function Fun");
            System.out.println("is evaluated (prio to loading the next class)");
            System.out.println("Function FUN (uppercased) is invoked unless function Fun exist");
            System.out.println("");
            System.out.println("Function ASK can be used to start interactive session");
            System.out.println("");
            System.out.println("   Example:");
            System.out.println("java org.refal.j.Run *ask");
            System.out.println("   Only built-in modules are loaded");
            System.out.println("");
            System.out.println("After prompt (#>) you may enter arbitrary expression in refal-like form");
            System.out.println("(variables not allowed)");
            System.out.println("Outermost < and > must be omitted");
            System.out.println("First term must be function name Fun (or reference to function &Fun)");
            System.out.println("Function FUN (uppercased) is invoked unless function Fun exist");
            System.out.println("  Example:");
            System.out.println("add 1 2");
            return;
            }

        Env.setArgs(args);

        loadLib();

        loadNames(args[0],'+');
    }

  // $ENTRY ASK
    public static final Object ASK = Term.getExternal("ASK",
        new org.refal.j.Function("Env.ASK") {
          public Object[] eval(Object[] e) throws Exception { return ASK(e); }
        });

    public static Object[] ASK(Object[] a) {
        while(true) {
            boolean reading = false;
            try {
                System.out.print("#>");
                if( Stream.eof(Stream.stdin) ) break;
                Box b = new Box();
                reading = true;
                Stream.readExpr(b, Stream.stdin,Term.sysTable, Stream.LINE_FEED);
                reading = false;
                if (b.igetlen()>0) {
                    Object fname = b.igetn(0);
                    if (fname instanceof String) {
                        Object fun = funByName((String)fname);
                        if (fun == null) {
                            System.out.println("Unknown function name "+fname);
                            continue;
                        } else {
                            b.isetn(0,fun);
                        }
                    }
                    Object[] res = Apply.APPLY(b.igetv());
                    if (res == null) {
                        System.out.print("FAIL: Invalid argument(s): ");
                        try {
                            Stream.WRITELN(b.igetv());
                        } catch (Exception e) { e.printStackTrace(); }
                    } else {
                        java.io.PrintWriter w = Stream.strmOut.out;
                        Stream.writeExpr(w, Term.sysTable, res);
                        w.println();
                        w.flush();
                    }
                }
                else System.out.println("Enter function name and argument");
            }
            catch (Throwable e) {
                System.out.println("Exception occurred: "+e);
                e.printStackTrace();
                if (reading)
                    try {
                        Stream.READ_LINE(Expr.EMPTY);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
            }
        }
        return Expr.EMPTY;
    }

}