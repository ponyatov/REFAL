package org.refal.j;

public class Error extends java.lang.Exception {
    private Object[] value;
    public Error() { super(); value = Expr.EMPTY; }
    public Error(String s) { super(s); value=new Object[] {s}; }
    public Error(Object[] e) { super(); value=e; }
    public Object[] getValue() { return value; }
}