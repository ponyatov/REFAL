@echo off
if %1.==. %0 .
if exist classpath.bat call classpath
call javac -d . -sourcepath %1;. %1/org/refal/j/*.java TestLib.java
call java TestLib
if %nopause%.==. pause