set srcj=srcj\org\refal\j\compiler
set RFCJ_CLASSES=..\classes
if not exist %srcj% mkdir %srcj%
call %REFALJ_HOME%\bin\rfcjj /in %REFALJ_HOME%\Compiler /out %srcj% Rcmainj Rsserv Rclist Rccompj Rcoutj Rslex Rssynj Rcmainj
if not exist classes mkdir classes
if %errorlevel% EQU 0 call %REFALJ_HOME%\bin\rfjavac -d classes %srcj%\*.java
if not exist S mkdir S
copy MakeJJcj.bat S
pause
