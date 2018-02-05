package org.refal.j;
import java.io.*;
import java.util.Hashtable;

public class Stream implements Referable {

/*************************************CHANNEL*******************************************/

    public Stream() {}
    public Stream(Object rw) {
        try {
            this.set(rw);
        } catch (Error e) {
            throw new java.lang.Error(e.getMessage());
        }
    }

    PushbackReader  in  = null;
    PrintWriter out = null;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMode() { return "FILEIO"; }

    public void set(Object v) throws Error {
        in = null;
        out = null;
        if (v instanceof PushbackReader) in = (PushbackReader)v;
        else
        if (v instanceof Reader) in = new PushbackReader ((Reader)v);
        else
        if (v instanceof PrintWriter) out = (PrintWriter)v;
        else
        if (v instanceof Writer) out = new PrintWriter((Writer)v);
        else throw new Error("Improper object value "+v+" for mode FILEIO");
    }

    public Object get() {
        if (in!=null) return in;
        if (out!=null) return out;
        return null;
        }

    public void init(Object[] a) {
        in = null;
        out = null;
    }

    static Stream getStream(Object a) {
        //System.out.println("Stream.getStream:1 a="+a);
        if (a instanceof Object[]) a = (((Object[])a).length==0? null : ((Object[])a) [0]);
        //System.out.println("Stream.getStream:2 a="+a);
        if (a instanceof Reference) {
            Referable r = ((Reference)a).getReferable();
            if (r==null) {
                r=new Stream();
                ((Reference)a).setReferable (r);
            }
            a=r;
        }
        //System.out.println("Stream.getStream:3 a="+a);

        // Number encountered
        if (a instanceof Number) {
            Object ic = intChannels.get(a);
            if (ic==null) {
                ic = new Stream();
                intChannels.put(a,ic);
            }
         //   System.out.println("Stream.getStream:4 a="+a+", strm="+ic);
            a = ic;
        }

        if (a instanceof Stream) return (Stream)a;
        System.out.println("Stream.getStream:5 a="+a);
        return null;
    }

    static Table getTable(Object a) {
        if (a instanceof Object[] && ((Object[])a).length!=2)
            return Table.getTable(((Object[])a) [1]);
        return null;
    }

    static PushbackReader stdin  = new PushbackReader(new InputStreamReader (System.in));  // Use default encoding
    static PrintWriter stdout = new PrintWriter(System.out); // Use default encoding
    static PrintWriter stderr = new PrintWriter(System.err); // Use default encoding
    static Stream strmIn = new Stream(stdin);
    static Stream strmOut = new Stream(stdout);
    static Stream strmErr = new Stream(stderr);
    public static final Object STDIN = Term.getExternal("STDIN", strmIn);
    public static final Object STDOUT= Term.getExternal("STDOUT", strmOut);
    public static final Object STDERR= Term.getExternal("STDERR", strmErr);

    //static { System.err.println("Initializing Stream"); }

    static Hashtable intChannels = new Hashtable(20);
    static {
        intChannels.put(new Integer(0), strmIn);
        intChannels.put(new Integer(1), strmOut);
        intChannels.put(new Integer(2), strmErr);
    }

/*************************************OPEN**CLOSE*******************************************/

    private InputStream getInputStream(String name) throws FileNotFoundException {
        try {
            return new FileInputStream(name);
        } catch (FileNotFoundException e) {
            InputStream s = this.getClass().getResourceAsStream(name);
            if (s==null) throw new FileNotFoundException(name);
            return s;
        }
    }
    void open(String name, char mode) throws IOException {
        close();
        switch(Character.toUpperCase(mode)) {
            case 'R': in  = new PushbackReader(new InputStreamReader(getInputStream(name))); break;
            case 'W': out = new PrintWriter(new FileWriter(name)); break;
            case 'A': out = new PrintWriter(new FileWriter(name,true)); break;
            default: throw new IllegalArgumentException();
            }
        //System.out.println("Stream.open: "+name+", mode="+mode+", in="+in+", out="+out);
        }

    void close() throws IOException {
        if (in!=null) in.close();
        in=null;
        if (out!=null) out.close();
        out=null;
        }

    // $ENTRY OPEN
    public static final Object OPEN = Term.getExternal("OPEN",
        new org.refal.j.Function("Stream.OPEN") {
          public Object[] eval(Object[] e) throws Exception { return OPEN(e); }
        });

    static public Object[] OPEN(Object[] a) throws IOException {
        int la = a.length;
        if (la<2) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        String name = Convert.toString(a,1,la-2);
        String mode = Convert.toString(a,la-1,1);
        try {
            s.open(name,mode.charAt(0));
            }
        catch (FileNotFoundException e) {
            System.out.println("No file "+name+" "+e);
            return null;
            }
//        catch (IllegalArgumentException e) { return null; }
        //System.out.println("Stream.OPEN: File opened: "+name+", mode="+mode);
        return new Object[] { a[0] };
        };

    // $ENTRY CLOSE
    public static final Object CLOSE = Term.getExternal("CLOSE",
        new org.refal.j.Function("Stream.CLOSE") {
          public Object[] eval(Object[] e) throws Exception { return CLOSE(e); }
        });

    static public Object[] CLOSE(Object[] a) throws IOException {
        if (!(a.length==1)) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        s.close();
        return Expr.EMPTY;
    }

    // $ENTRY ERASE_FILE
    public static final Object ERASE_FILE = Term.getExternal("ERASE_FILE",
        new org.refal.j.Function("Stream.ERASE_FILE") {
          public Object[] eval(Object[] e) throws Exception { return ERASE_FILE(e); }
        });

    static public Object[] ERASE_FILE(Object[] a) throws IOException {
        String s = Convert.toString(a);
        boolean b = new File(s).delete();
        return Expr.EMPTY;
    }

/**********************************************OUTPUT***********************************/

    // $ENTRY PRINTLN
    public static final Object PRINTLN = Term.getExternal("PRINTLN",
        new org.refal.j.Function("Stream.PRINTLN") {
          public Object[] eval(Object[] e) throws Exception { return PRINTLN(e); }
        });

    static public Object[] PRINTLN(Object[] a) throws IOException {
        printExpr(stdout,a,0,a.length);
        stdout.println();
        stdout.flush();
        return Expr.EMPTY;
        }

    // $ENTRY PRINT
    public static final Object PRINT = Term.getExternal("PRINT",
        new org.refal.j.Function("Stream.PRINT") {
          public Object[] eval(Object[] e) throws Exception { return PRINT(e); }
        });

    static public Object[] PRINT(Object[] a)  throws IOException {
        printExpr(stdout,a,0,a.length);
        stdout.flush();
        return Expr.EMPTY;
    }

    // $ENTRY PRINTLN!
    public static final Object PRINTLN_X = Term.getExternal("PRINTLN!",
        new org.refal.j.Function("Stream.PRINTLN!") {
          public Object[] eval(Object[] e) throws Exception { return PRINTLN_X(e); }
        });

    static public Object[] PRINTLN_X(Object[] a) throws IOException {
        if (a.length<1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return Trace.fail(3,"Stream.println: s==null");
        if (s.out==null) return Trace.fail(3,"Stream.println: s.out==null");
        printExpr(s.out,a,1,a.length-1);
        s.out.println();
        s.out.flush();
        return Expr.EMPTY;
        }

    // $ENTRY PRINT!
    public static final Object PRINT_X = Term.getExternal("PRINT!",
        new org.refal.j.Function("Stream.PRINT!") {
          public Object[] eval(Object[] e) throws Exception { return PRINT_X(e); }
        });

    static public Object[] PRINT_X(Object[] a) throws IOException {
        if (a.length<1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.out!=null) return null;
        printExpr(s.out,a,1,a.length-1);
        s.out.flush();
        return Expr.EMPTY;
        }

    static void printExpr(Writer out, Object[] e, int offset, int length) throws IOException {
        for(int i=0; i<length; i++)
            printTerm(out,e[offset+i]);
        }

    static void printTerm(Writer out, Object t) throws IOException {
            if (t instanceof Object[]) {
                Object[] e = (Object[]) t;
                out.write('(');
                printExpr(out,e,0,e.length);
                out.write(')');
                }
            else if (t==null)
                out.write("null");
            else
                out.write(t.toString());
        }

/************************* WRITE, WRITELN, WRITE_X, WRITELN_X ******************************/

    static void writeExpr(Writer out, Table table, Object[] expr) throws IOException {
        writeExpr(out,table,expr,0,expr.length);
    }

    static void writeExpr(Writer out, Table table, Object[] expr, int offset, int length) throws IOException {
        boolean quoteOpen = false;
        for(int i=0; i<length; i++) {
            Object t = expr[offset+i];
            if (t instanceof Character) {
                if (!quoteOpen) { if (i>0) out.write(' '); quoteOpen=true; out.write('\''); }
                writeChar(out,((Character)t).charValue());
            }
            else {
                if (quoteOpen) { quoteOpen=false; out.write('\''); }
                if (i>0) out.write(' ');
                writeTerm(out,table,t);
            }
        }
        if (quoteOpen) out.write('\'');
    }

    static void writeChar(Writer out, char c) throws IOException {
                switch (c) {
                    case '\'': out.write("\\\'"); break;
                    case '\"': out.write("\\\""); break;
                    case '\\': out.write("\\\\"); break;
                    case '\n': out.write("\\n"); break;
                    case '\t': out.write("\\t"); break;
                    case '\b': out.write("\\b"); break;
                    case '\f': out.write("\\f"); break;
                    case '\r': out.write("\\r"); break;
                    default:
                        if (c>=32 && c<128) out.write(c); else
                        if (c<256) { out.write("\\" ); for (int d=  64; d>0; d/= 8) out.write(Integer.toOctalString(c/d% 8)); } else
                                   { out.write("\\u"); for (int d=4096; d>0; d/=16) out.write(Integer.  toHexString(c/d%16)); }
                }
        }

    static void writeWord(Writer out, String s) throws IOException {
        boolean quotesNeeded = ! isIdentifier(s);
        if (quotesNeeded) out.write('\"');
        for (int i=0; i<s.length(); i++)
            writeChar(out, s.charAt(i));
        if (quotesNeeded) out.write('\"');
    }

    private static boolean isIdentifier(String s) throws IOException {
        if (s.length()==0) return false;
        if (Character.isLetter(s.charAt(0))) {
            for(int i=0; i<s.length(); i++) {
                if(! (isIdentifierPart(s.charAt(i)))) return false;
            }
            return true;
        }
        return false;
    }

    static void writeTerm(Writer out, Table table, Object term) throws IOException {
            if (term instanceof Object[]) {
                Object[] e = (Object[]) term;
                out.write('(');
                writeExpr(out,table,e);
                out.write(')');
            } else
            if (term instanceof String) {
                String s = (String)term;
                writeWord(out,s);
            } else
            if (term instanceof Reference) {
                Object name = null;
                boolean first = false;
                if (table!=null) {
                    name = table.valueToName('O', term);
                    if (name==null) {
                        // check if name will be unique in the table
                        Object value = table.nameToValue('O',((Reference)term).getName());
                        if (value!=null) { // Other object with the same name exists
                            name = ((Reference)term).getUniqueName();
                        } else name = ((Reference)term).getName();
                        table.link(name,term);
                        first=true;
                    }
                }
                if (name==null) name = ((Reference)term).getName();
                out.write(Reference.DEREFER);
                writeTerm(out,null,name);
                if (first) {
                    Referable re = ((Reference)term).getReferable();
                    if (re instanceof Writable) {
                        out.write('=');
                        ((Writable)re).writeObject(out,table);
                    }
                }
            } else
            if (term instanceof Character) {
                out.write('\'');
                writeChar(out,((Character)term).charValue());
                out.write('\'');
            } else
            if (term instanceof Double) {
                double d = ((Double)term).doubleValue();
                if (Double.isNaN     (d)       ) out.write('+'); else
                if (Double.isInfinite(d) && d>0) out.write('+');
                out.write(term.toString());
            } else
            out.write(term.toString());
    }

    // $ENTRY WRITE
    public static final Object WRITE = Term.getExternal("WRITE",
        new org.refal.j.Function("Stream.WRITE") {
          public Object[] eval(Object[] e) throws Exception { return WRITE(e); }
        });

    public static Object[] WRITE (Object[] a) throws IOException {
        writeExpr(stdout,null, a);
        stdout.flush();
        return Container.EMPTY;
    }

    // $ENTRY WRITELN
    public static final Object WRITELN = Term.getExternal("WRITELN",
        new org.refal.j.Function("Stream.WRITELN") {
          public Object[] eval(Object[] e) throws Exception { return WRITELN(e); }
        });

    public static Object[] WRITELN (Object[] a) throws IOException {
        writeExpr(stdout,null, a);
        stdout.println();
        stdout.flush();
        return Container.EMPTY;
    }

    // $ENTRY WRITELN!
    public static final Object WRITELN_X = Term.getExternal("WRITELN!",
        new org.refal.j.Function("Stream.WRITELN!") {
          public Object[] eval(Object[] e) throws Exception { return WRITELN_X(e); }
        });

    public static Object[] WRITELN_X (Object[] a) throws IOException {
        if (a.length<1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.out==null) return null;
        Table t = getTable(a[0]);
        writeExpr(s.out,t, a,1,a.length-1);
        s.out.println();
        s.out.flush();
        return Expr.EMPTY;
    }

    // $ENTRY WRITE!
    public static final Object WRITE_X = Term.getExternal("WRITE!",
        new org.refal.j.Function("Stream.WRITE!") {
          public Object[] eval(Object[] e) throws Exception { return WRITE_X(e); }
        });

    public static Object[] WRITE_X (Object[] a) throws IOException {
        if (a.length<1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.out==null) return null;
        Table t = getTable(a[0]);
        writeExpr(s.out,t, a,1,a.length-1);
        s.out.flush();
        return Expr.EMPTY;
    }

    // $ENTRY ENCODE
    public static final Object ENCODE = Term.getExternal("ENCODE",
        new org.refal.j.Function("Stream.ENCODE") {
          public Object[] eval(Object[] e) throws Exception { return ENCODE(e); }
        });

    public static Object[] ENCODE (Object[] a) throws IOException {
        if (a.length<1) return null;
        Table t = Table.getTable(a[0]);
        Writer w = new StringWriter();
        writeExpr(w, t, a,1,a.length-1);
        return Convert.explode(w.toString());
    }

/*******************************************INPUT*********************************************************/

    private static String readLine(PushbackReader in) throws IOException {
        StringBuffer sb = new StringBuffer();
        while(true) {
            int c = in.read();
            switch (c) {
                case '\r':
                    int c1 = in.read();
                    if (c1!='\n')
                        if (c1 != -1) in.unread(c1);
                case '\n':
                case -1:
                    return sb.toString();
            }
            sb.append((char)c);
        }
    }

    // $ENTRY READ_LINE!
    public static final Object READ_LINE_X = Term.getExternal("READ_LINE!",
        new org.refal.j.Function("Stream.READ_LINE!") {
          public Object[] eval(Object[] e) throws Exception { return READ_LINE_X(e); }
        });

    static public Object[] READ_LINE_X(Object[] a) throws IOException {
        if (a.length!=1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.in==null) return null;
        String str = readLine(s.in);
        return (str==null? null : Convert.explode(str));
    }

    // $ENTRY READ_LINE
    public static final Object READ_LINE = Term.getExternal("READ_LINE",
        new org.refal.j.Function("Stream.READ_LINE") {
          public Object[] eval(Object[] e) throws Exception { return READ_LINE(e); }
        });

    static public Object[] READ_LINE(Object[] a) throws IOException {
            if (a.length!=0) return null;
            String str = readLine(stdin);
            return (str==null? null : Convert.explode(str));
    }

    // $ENTRY READ_CHAR!
    public static final Object READ_CHAR_X = Term.getExternal("READ_CHAR!",
        new org.refal.j.Function("Stream.READ_CHAR!") {
          public Object[] eval(Object[] e) throws Exception { return READ_CHAR_X(e); }
        });

    static public Object[] READ_CHAR_X(Object[] a) throws IOException {
        if (a.length!=1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.in==null) return null;
        int c = s.in.read();
        return (c==-1? Expr.EMPTY : new Object[] { new Character((char) c) });
    }

    // $ENTRY READ_CHAR
    public static final Object READ_CHAR = Term.getExternal("READ_CHAR",
        new org.refal.j.Function("Stream.READ_CHAR") {
          public Object[] eval(Object[] e) throws Exception { return READ_CHAR(e); }
        });

    static public Object[] READ_CHAR(Object[] a) throws IOException {
        if (a.length!=0) return null;
        int c = stdin.read();
        return (c==-1? Expr.EMPTY : new Object[] { new Character((char) c) });
    }

    // $ENTRY EOF
    public static final Object EOF = Term.getExternal("EOF",
        new org.refal.j.Function("Stream.EOF") {
          public Object[] eval(Object[] e) throws Exception { return EOF(e); }
        });

    static public Object[] EOF(Object[] a) throws IOException {
        if (a.length!=1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        return (eof(s.in)? Expr.T : Expr.F);
    }

    static boolean eof(PushbackReader in) throws IOException {
        if (in==null) return false;
        int c = in.read();
        if (c != -1) in.unread(c);
        return c==-1;
    }
/*
    static public Number numberValueOf(String s) throws Error {
                    //System.out.println("Stream.numberValueOf: "+s);
                    try {
                        return Integer.valueOf(s);
                    }
                    catch (NumberFormatException e0) {
                       // System.out.println("Parse error in Long.valueOf: \""+s+"\" not a whole number" );
                    }
                    try {
                        return new java.math.BigInteger(s);
                    }
                    catch (NumberFormatException e0) {
                       // System.out.println("Parse error in Long.valueOf: \""+s+"\" not a whole number" );
                    }
                    try {
                        return Double.valueOf(s);
                    }
                    catch (NumberFormatException e1) {
                        throw new Error("Parse error: \""+s+"\" not a number");
                    }
        }
*/
    /* Throws org.refal.j.Error if the beginning of the reader 'in' (before blank, ')' or '>') cannot be parsed as a correct integer value) */
    static Number readNumber(PushbackReader in) throws IOException, Error {
        StringBuffer sb = new StringBuffer();
        int c;
        while (true) {
            c=in.read();
            switch (c) {
                case ')': case '>':
                case ' ': case '\t':
                case '\n': case '\r': case '\f':
                    in.unread(c);
                case -1:
                    return Arithm.numberValueOf(sb.toString());
            }
            sb.append((char)c);
        }
    }

    static String readQuote(PushbackReader in, int q) throws IOException,Error {
        StringBuffer sb = new StringBuffer();
        int c;
        while ((c=in.read())!=q) {
            switch (c) {
            case '\n': throw new Error("Unexpected end of line: unclosed quote "+(char)q);
            case '\\':
                c=in.read();
                switch (c) {
                case 'n': c='\n'; break;
                case 't': c='\t'; break;
                case 'b': c='\b'; break;
                case 'f': c='\f'; break;
                case 'r': c='\r'; break;
                case '\\': case '\'': case '\"': break;
                case 'u':
                    char[] h = new char[4];
                    for (int i=0; i<4; i++)
                        h[i]=(char)in.read();
                    try {
                        c=Integer.parseInt(new String(h),16);
                    }
                    catch (NumberFormatException e) { throw new Error("Illegal unicode: \\u"+new String(h)); }
                    break;
                default:
                    int n=0;
                    if (c<'0'|| c>'7') throw new Error("Illegal characters: \\"+(char)c);
                    while(n<32) {
                        n=n*8+(c-'0');
                        c=in.read();
                        if (c<'0'|| c>'7') { if (c != -1) in.unread(c); break; }
                    }
                    c=n;
                }
            }
            sb.append((char)c);
            //System.out.println("Stream.readQuote: "+sb);
        }
        return sb.toString();
    }

    private static boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c== '_';
        }

    static String readIdent(PushbackReader in) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;
        if ( Character.isLetter((char)(c=in.read())) ) {
            sb.append((char)c);
            while ( isIdentifierPart((char)(c=in.read())) ) {
                sb.append((char)c);
            }
        }
        if (c!=-1) in.unread(c);
        return sb.toString();
    }

    static String readWord(PushbackReader in) throws IOException, Error {
        int c=in.read();
        if (c=='\"') return readQuote(in,c);
        else
        if (Character.isLetter((char)c)) {
             in.unread(c);
             return readIdent(in);
        }
        else throw new Error("Illegal word start: " + (char) c);
    }

    static final int TERM_FEED  =  0;
    static final int LINE_FEED  =  1;
    static final int FILE_FEED  =  2;
    static final int ANGLE_BRAC =  3;
    static final int RIGHT_BRAC =  4;

    static void readExpr(Container cont, PushbackReader in, Table t, int term) throws IOException, Error {
        while(true) {
            int c = in.read();
            //System.out.println("Stream.readExpr: start: \'"+(char)c+"\' ["+c+"]");
            switch (c) {
            case ' ': case '\t': case '\f': case '\r': continue; /* Skip space */
            case '\n':  if (term==LINE_FEED) return; else continue;
            case -1:
                switch (term) {
                    case RIGHT_BRAC: throw new Error("Unexpected end of stream: unclosed (");
                    case ANGLE_BRAC: throw new Error("Unexpected end of stream: unclosed <");
                    default: return;
                }
            case '<': {
                    Box b = new Box();
                    readExpr(b,in,t, ANGLE_BRAC);
                    // Convert leading word (if any) into reference
                    if (b.igetlen()>0) {
                        Object f = b.igetn(0);
                        if (f instanceof String) {
                            b.isetn(0,Term.getExternal((String)f));
                            }
                        }
                    Object[] arg = (Object[])b.get();
                    try {
                        cont.addExpr( Apply.APPLY(arg) );
                    }
                    catch (Exception e) {
                        System.err.println("Stream.readExpr: exception during input: "+e);
                        e.printStackTrace();
                        System.err.print("Stream.readExpr: input active term ignored: <");
                        writeExpr(stderr, t, arg);
                        stderr.flush();
                        System.err.println(">");
                        }
                    break;
                }
            case '(': {
                    Box b = new Box();
                    readExpr(b,in,t, RIGHT_BRAC);
                    cont.addTerm( b.get() );
                    break;
                }
            case ')': if (term==RIGHT_BRAC) return; else throw new Error("Unbalanced )");
            case '>': if (term==ANGLE_BRAC) return; else throw new Error("Unbalanced >");
            case '&':case '*': {
                    String name = readWord(in);
                    Object value;
                    char c1=(char)in.read();
                    if(c1=='=') {
                        // TODO: if (in.read()=='=') constant definition (use t.define(name,value))
                        //                                         Note: &name shouldn't occur in the input value
                        Referable obj = Reference.readObject(in,t);
                        value = t.defineReference(name,obj);
                    } else {
                        if (c1!=-1) in.unread(c1);
                        value = t.define(name,null);
                    }
                    cont.addTerm(value);
                    break;
                }
            case '\"': cont.addTerm(readQuote(in,c)); break;
            case '\'': String s = readQuote(in,c);
                        for (int i=0; i<s.length(); i++)
                            cont.addTerm(new Character(s.charAt(i)));
                        break;
            default:
                if (Character.isDigit((char)c) || c=='-' || c == '+') {
                    in.unread(c);
                    cont.addTerm(readNumber(in));
                } else
                if (Character.isLetter((char)c)) {
                    in.unread(c);
                    cont.addTerm(readIdent(in));
                } else {
                    in.unread(c);
                    throw new Error("Illegal element start: \'"+(char)c+"\' (#"+c+")");
                }
            }
            if (term==TERM_FEED) return;
        }
    }

    // $ENTRY READ
    public static final Object READ = Term.getExternal("READ",
        new org.refal.j.Function("Stream.READ") {
          public Object[] eval(Object[] e) throws Exception { return READ(e); }
        });

    public static Object[] READ(Object[] a) throws Exception {
        if (a.length!=0) return null;
        Box b = new Box();
        readExpr(b, stdin, null, TERM_FEED);
        return b.igetv();
    }

    // $ENTRY READ!
    public static final Object READ_X = Term.getExternal("READ!",
        new org.refal.j.Function("Stream.READ!") {
          public Object[] eval(Object[] e) throws Exception { return READ_X(e); }
        });

    public static Object[] READ_X(Object[] a) throws Exception {
        if (a.length!=1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.in==null) return null;
        Table t = getTable(a[0]);
        Box b = new Box();
        readExpr(b, s.in, t, TERM_FEED);
        return b.igetv();
    }

    // $ENTRY READLN
    public static final Object READLN = Term.getExternal("READLN",
        new org.refal.j.Function("Stream.READLN") {
          public Object[] eval(Object[] e) throws Exception { return READLN(e); }
        });

    public static Object[] READLN(Object[] a) throws Exception {
        if (a.length!=0) return null;
        Box b = new Box();
        readExpr(b, stdin, null, LINE_FEED);
        return b.igetv();
    }

    // $ENTRY READLN!
    public static final Object READLN_X = Term.getExternal("READLN!",
        new org.refal.j.Function("Stream.READLN!") {
          public Object[] eval(Object[] e) throws Exception { return READLN_X(e); }
        });

    public static Object[] READLN_X(Object[] a) throws Exception {
        if (a.length!=1) return null;
        Stream s = getStream(a[0]);
        if (s==null) return null;
        if (s.in==null) return null;
        Table t = getTable(a[0]);
        Box b = new Box();
        readExpr(b, s.in, t, LINE_FEED);
        return b.igetv();
    }

    // $ENTRY DECODE
    public static final Object DECODE = Term.getExternal("DECODE",
        new org.refal.j.Function("Stream.DECODE") {
          public Object[] eval(Object[] e) throws Exception { return DECODE(e); }
        });

    public static Object[] DECODE(Object[] a) throws Exception {
        if (a.length<1) return null;
        Table t = Table.getTable(a[0]); // may be null
        String s = Convert.toString(a,1,a.length-1);
        //System.out.println("Stream.DECODE: "+s);
        PushbackReader pbr = new PushbackReader (new StringReader(s));
        try {
            Box b = new Box();
            readExpr(b, pbr, t, LINE_FEED);
            return Expr.conTE("T", b.igetv());
        }
        catch (Error e) {
            //e.printStackTrace();
            StringBuffer sb = new StringBuffer();
            while(true) {
                int c = pbr.read();
                if (c==-1) break;
                sb.append((char)c);
            }
            return Expr.conTE("F", Convert.explode(sb.toString()));
        }
    }

    /* Read file as a single Refal expression in metacode */
    public static Object[] readFile(String name, Table t) throws Exception {
        PushbackReader in =  new PushbackReader(new FileReader(name));
        Container b = new Box();
        while(! eof(in)) {
            readExpr(   b,in,t,TERM_FEED);
        }
        return b.igetv();
    }

    // $ENTRY READ_FILE
    public static final Object READ_FILE = Term.getExternal("READ_FILE",
        new org.refal.j.Function("Stream.READ_FILE") {
          public Object[] eval(Object[] e) throws Exception { return READ_FILE(e); }
        });

    public static Object[] READ_FILE(Object[] a) throws Exception {
        if (a.length!=2) return null;
        if (! (a[0] instanceof String)) return null;
        Table t = Table.getTable(a[1]);
        return readFile((String) a[0], t);
    }

}