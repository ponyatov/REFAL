echo Making executable Refal-to-java compiler cj.rex
call %REFALJ_HOME%\bin\rfc shiftargs rcmainj rsserv rclist rccompj rcoutj rslex rssynj
if %errorlevel% EQU 0 call mkcj .
pause
