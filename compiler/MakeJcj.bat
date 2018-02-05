set srcj=srcj\org\refal\j\compiler
call %REFALJ_HOME%\bin\rfc dbg
if not exist %srcj% mkdir %srcj%
call %REFALJ_HOME%\bin\rfcj /out %srcj% Rcmainj Rsserv Rclist Rccompj Rcoutj Rslex Rssynj
if not exist classes mkdir classes
if %errorlevel% EQU 0 call %REFALJ_HOME%\bin\rfjavac -d classes %srcj%\*.java
if not exist S mkdir S
copy MakeJJcj.bat S
pause