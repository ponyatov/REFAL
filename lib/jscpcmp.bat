set res=res%1%2%3%4%5%6%7%8%9

if not exist %res% mkdir %res%

set savenopause=%nopause%
set nopause=YES

if exist classpath.bat call classpath

if exist s.bat call s    -ve2               -d %res% -log %res%\jscp.out %*
if exist s.bat goto CHK

if exist Task.xml call jscp -ve2 -xml Task.xml -d %res% -log %res%\jscp.out %*
if exist Task.xml goto CHK

call jscp -ve2 -i Test.java -m test -d %res% -log %res%\jscp.out %*

:CHK
if %errorlevel% NEQ 0 goto END

if exist %res%\jscp.out.saved diff %res%\jscp.out.saved %res%\jscp.out
if errorlevel 1 windiff %res%\jscp.out.saved %res%\jscp.out

rem if not exist %res%\jscp.out.saved copy %res%\jscp.out %res%\jscp.out.saved

if not exist crun.bat goto END

if exist result goto EXECRES
call crun >result
echo on

:EXECRES
@echo Correct output file and continue
pause
call crun %res% >%res%\result
echo on

diff result %res%\result
if errorlevel 1 windiff result %res%\result

:END

set nopause=%savenopause%
if %nopause%.==. pause

