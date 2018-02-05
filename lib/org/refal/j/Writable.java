package org.refal.j;

public interface Writable {

    /* creation subinterface */
    //readObject(String header, PushBackReader in);
    //makeObject(String mode);
    void writeObject(java.io.Writer out, Table t) throws java.io.IOException;
}