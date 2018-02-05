@set JDK=
@if not "%JAVA_HOME%"=="" set JDK=%JAVA_HOME%\bin\
"%JDK%javac.exe" -classpath %CLASSPATH%;.;%REFALJ_HOME%\lib %*

