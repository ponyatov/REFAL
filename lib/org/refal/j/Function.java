package org.refal.j;

public abstract class Function implements Referable {
        public Function(String name) { /* super(name); */ }
        public abstract java.lang.Object[] eval(java.lang.Object[] e) throws Exception;

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
            } // no fields, only class is copied

        public String getMode() { return "FUNC"; }

        public Object get() {return this; }
        public void set(Object v) throws Error {  throw new Error(); }
        public void init(Object[] v) throws Error { throw new Error(); }

}
