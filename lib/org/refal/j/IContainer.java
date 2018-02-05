package org.refal.j;

public interface IContainer extends Referable, Writable {

    // Virtual methods:
    // pos>=0 counts from left, positions after end are valid
    // pos<0 counts from right, positions before beginning are treated as pos=0
    //

     Object[] igetv();
//   void   isetv(Object[] a); // init may be used instead
     Object igetn(int pos); // null if pos>=length
     void   isetn(int pos, Object a);
     int    igetlen();
     void   isetlen(int len);          // fill with BLANK
     void   ishift(int pos, int shift); // fill with BLANK

    static final char BLANK_CHAR = ' ';
    static final Object BLANK = new Character(BLANK_CHAR);
}
