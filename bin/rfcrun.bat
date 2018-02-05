@if %1.==. goto help
@if %2.==. %0 %1 ask
call %refalj_home%\bin\rfcj %1 /D
call %refalj_home%\bin\rfjavac %~dpn1.java
call %refalj_home%\bin\rfrun %~n1+*%2 %3 %4 %5 %6 %7 %8 %9
@goto end
:help
@echo off
echo Compile-and-Run a single RefalJ module 
echo USAGE:
echo ""
echo "  rfcrun <file.ref> [<function-name> <arguments>]"
echo ""
echo "<function-name> - entry function to start"
echo "<arguments> - arguments for <ARG 1>, <ARG 2> etc."
echo ""
:end
@pause
