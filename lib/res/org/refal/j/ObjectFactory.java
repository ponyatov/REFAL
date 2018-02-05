package org.refal.j;
import java.io.*;
import java.lang.reflect.*;
//--------------------------------------   0 sec - scp class org.refal.j.ObjectFactory

public final class ObjectFactory {
    private static final java.lang.String[] classNames = new java.lang.String[] {"org.refal.j.Box", "org.refal.j.Mask", "org.refal.j.Real", "org.refal.j.Chars", "org.refal.j.Table"};
    private final java.lang.Class[] classes;
    private java.lang.reflect.Method[] mMakeObject;
    private java.lang.reflect.Method[] mReadObject;

    ObjectFactory() {
        this (org.refal.j.ObjectFactory.classNames);
    }

    private ObjectFactory(java.lang.String[] classNames) {
        this.classes = new java.lang.Class[classNames.length];
        try {
          for (int i = 0; i < classNames.length; i++) {
            try {
              this.classes[i] = java.lang.Class.forName(classNames[i]);}
            catch (java.lang.ClassNotFoundException e) {
              java.lang.System.out.println("ObjectFactory: " + e);}
            }
          java.lang.Class[] pars1 = new java.lang.Class[] {java.lang.String.class, java.io.PushbackReader.class, org.refal.j.Table.class};
          this.mReadObject = this.getMethods("readObject", pars1);
          java.lang.Class[] pars2 = new java.lang.Class[] {java.lang.String.class};
          this.mMakeObject = this.getMethods("makeObject", pars2);
          return;}
        catch (java.lang.Exception e) {
          java.lang.System.out.println("ObjectFactory: " + e);}
    }

    private java.lang.reflect.Method[] getMethods(java.lang.String name, java.lang.Class[] pars) throws java.lang.NoSuchMethodException {
        java.lang.reflect.Method[] methods = new java.lang.reflect.Method[this.classes.length];
        for (int i = 0; i < org.refal.j.ObjectFactory.classNames.length; i++) {
          java.lang.Class c = this.classes[i];
          if (c == null) {
            continue;}
          try {
            methods[i] = c.getDeclaredMethod(name, pars);}
          catch (java.lang.NoSuchMethodException e) {}
          }
        return methods;
    }
//--------------------------------------   0 sec - constructor org.refal.j.ObjectFactory()
//--------------------------------------   1 sec - method org.refal.j.ObjectFactory.readObject(java.io.PushbackReader, org.refal.j.Table)
//--------------------------------------   4 sec - method org.refal.j.ObjectFactory.makeObject(java.lang.String)
//--------------------------------------   5 sec - method org.refal.j.ObjectFactory.readObject(java.io.PushbackReader, org.refal.j.Table) postprocessing...
    public org.refal.j.Referable readObject (final java.io.PushbackReader in_229, final org.refal.j.Table t_230)
      throws java.io.IOException
    {
      try {
        final java.lang.String header_231 = org.refal.j.Stream.readIdent(in_229) /*static*/;
        if (header_231.length() /*virtual*/ == 0
        ||  header_231.charAt(0) /*virtual*/ == 'C') {
          final org.refal.j.Box cont_255 = new org.refal.j.Box(header_231.length() /*virtual*/ <= 1 ? 0 : java.lang.Integer.parseInt(header_231.substring(1) /*virtual*/) /*static*/);
          if (cont_255.readBody(in_229, t_230) /*virtual*/) {
            return cont_255;}}
        if (header_231.length() /*virtual*/ == 0) {
          final int c_271 = in_229.read() /*virtual*/;
          in_229.unread(c_271) /*virtual*/;
          if (java.lang.Character.isDigit((char)c_271) /*static*/
          ||  c_271 == 45
          ||  c_271 == 43) {
            final java.lang.Number number_290 = org.refal.j.Stream.readNumber(in_229) /*static*/;
            final org.refal.j.Referable res_298 =
                number_290 instanceof java.lang.Double ? (org.refal.j.Referable)
                  new org.refal.j.Real()
                : new org.refal.j.Mask();
            res_298.set((java.lang.Object)number_290) /*interface*/;
            return res_298;}}
        else {
          if (header_231.charAt(0) /*virtual*/ == 'M') {
            final org.refal.j.Mask cont_306 = new org.refal.j.Mask();
            if (cont_306.readBody(in_229, t_230) /*virtual*/) {
              return cont_306;}}}
        if (header_231.length() /*virtual*/ == 0) {
          final int c_328 = in_229.read() /*virtual*/;
          if (c_328 == 34) {
            return new org.refal.j.Chars(org.refal.j.Stream.readQuote(in_229, c_328) /*static*/);}
          else {
            in_229.unread(c_328) /*virtual*/;}}
        else {
          if (header_231.charAt(0) /*virtual*/ == 'S') {
            final org.refal.j.Chars cont_340 = new org.refal.j.Chars();
            if (cont_340.readBody(in_229, t_230) /*virtual*/) {
              return cont_340;}}}
        if (header_231.length() /*virtual*/ > 0
        &&  header_231.charAt(0) /*virtual*/ == 'T') {
          final org.refal.j.Table cont_378 = new org.refal.j.Table(header_231.length() /*virtual*/ <= 1 ? 0 : java.lang.Integer.parseInt(header_231.substring(1) /*virtual*/) /*static*/);
          if (cont_378.readBody(in_229, t_230) /*virtual*/) {
            return cont_378;}}}
      catch (final java.io.IOException e_398) {
        throw e_398;}
      catch (final java.lang.Throwable e_400) {
        e_400.printStackTrace() /*virtual*/;}
      return null;
    }
//--------------------------------------   5 sec - method org.refal.j.ObjectFactory.makeObject(java.lang.String) postprocessing...
    public org.refal.j.Referable makeObject (final java.lang.String mode_404)
    {
      try {
        final java.lang.String mode_405 = mode_404.intern() /*virtual*/;
        if (mode_405 == "BOX"
        ||  mode_405 == "CHAIN"
        ||  mode_405 == "VECTOR") {
          final org.refal.j.Box obj_424 = new org.refal.j.Box();
          obj_424.mode = mode_405;
          return obj_424;}
        else {
          if (mode_405 == "MASK") {
            return new org.refal.j.Mask();}
          if (mode_405 == "REAL") {
            return new org.refal.j.Real();}
          if (mode_405 == "STRING") {
            return new org.refal.j.Chars();}
          if (mode_405 == "TABLE") {
            return new org.refal.j.Table();}}}
      catch (final java.lang.Throwable e_470) {
        java.lang.System.out.println("ObjectFactory.makeObject:") /*virtual*/;
        e_470.printStackTrace() /*virtual*/;}
      return null;
    }
}
//--------------------------------------   5 sec - scp class org.refal.j.ObjectFactory finished
//--------------------------------------   5 sec - JScp version 0.1.66  ---
