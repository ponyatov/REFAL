setlocal
if %RFJC_CLASSES%.==. set RFJC_CLASSES=%REFALJ_HOME%\rfjc.jar
set classpath=%RFJC_CLASSES%
%REFALJ_HOME%\bin\rfjava org.refal.j.compiler.Rcmainj %*
