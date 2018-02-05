package org.refal.j;

public interface Referable extends Cloneable {
    Object clone() throws CloneNotSupportedException;
    String getMode();
    void set(Object v) throws Error;  // Object v must have compatible value mode -- otherwise Error
    Object get();                     // Object has compatible value mode
    void init(Object[] a) throws Error;

    static final Object[] EMPTY = {};
}