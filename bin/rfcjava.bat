call %REFALJ_HOME%\bin\rfcjj %1.ref /D
call %REFALJ_HOME%\bin\rfjavac %1.java
call %REFALJ_HOME%\bin\rfjava %*
pause