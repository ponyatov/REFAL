package org.refal.j;
import java.io.*;
import java.lang.reflect.*;

public final class ObjectFactory {

    private static final String classNames[]= {
        "org.refal.j.Box",
        "org.refal.j.Mask",
        "org.refal.j.Real",
        "org.refal.j.Chars",
        "org.refal.j.Table"
        };

    private final Class[] classes;

    ObjectFactory() {
        this(classNames);
    }

    private ObjectFactory(String[] classNames) {
        classes = new Class[classNames.length];
        try {
            for (int i=0; i<classNames.length; i++) {
                try {
                    classes[i] = Class.forName(classNames[i]);
                } catch (ClassNotFoundException e) {
                    System.out.println ("ObjectFactory: " + e);
                }
            }
            Class[] pars1 = { String.class, PushbackReader.class, Table.class };
            mReadObject = getMethods("readObject", pars1 );
            Class[] pars2 = { String.class };
            mMakeObject = getMethods("makeObject", pars2);
            return;
        } catch (Exception e) { System.out.println ("ObjectFactory: " + e); }
    }

    private Method[] getMethods (String name, Class[] pars) throws NoSuchMethodException {
        Method[] methods = new Method[classes.length];
        for (int i=0; i<classNames.length; i++) {
            Class c = classes[i];
            if (c==null) continue;
            try {
                methods[i] = c.getDeclaredMethod(name,pars);
            } catch (NoSuchMethodException e) {} // Skip this class silently
        }
        return methods;
    }

    private Method[] mReadObject;
    private Method[] mMakeObject;

    public Referable readObject(PushbackReader in, Table t) throws IOException {
        try {
            String header = Stream.readIdent(in);
            //System.out.println ("ObjectFactory.readObject: header="+header);
            Referable obj = null;
            final Object[] args = { header, in, t };
            for (int i=0; i<mReadObject.length; i++) {
                Method m = mReadObject[i];
                if (m!=null) {
                    obj = (Referable) m.invoke(null,args);
                    if (obj!=null) return obj;
//                  else System.out.println ("ObjectFactory.readObject: recognition failed: "+classNames[i]);
                }
            }
//        } catch (IllegalAccessException e) {
//            System.out.println ("ObjectFactory.readObject: " + e);
//        } catch (InvocationTargetException e) {
//            System.out.println ("ObjectFactory.readObject: " + e);
        } catch (IOException e) {
            throw e;
        } catch (Throwable e) {
//          System.out.println ("ObjectFactory.readObject:");
            e.printStackTrace();
        }
        return null;
    }

    public Referable makeObject(String mode) {
        try {
            final Object[] args = { mode.intern() };
            Referable obj = null;
            for (int i=0; i<classNames.length; i++) {
                Method m = mMakeObject[i];
                if (m!=null) {
                    obj = (Referable) m.invoke(null,args);
                    if (obj!=null) return obj;
                }
            }
//        } catch (IllegalAccessException e) {
//            System.out.println ("ObjectFactory.makeObject: " + e);
//        } catch (InvocationTargetException e) {
//            System.out.println ("ObjectFactory.makeObject: " + e);
        } catch (Throwable e) {
            System.out.println ("ObjectFactory.makeObject:");
            e.printStackTrace();
        }
        return null;
    }
}
