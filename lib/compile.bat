if %1.==. %0 .
call javac -d %1 org\refal\j\*.java
copy ..\bin\builtinj.fls %1\org\refal\j
copy ..\bin\version.txt %1\org\refal\j
if %nopause%.==. pause