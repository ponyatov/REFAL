echo Making executable Refal-to-RASL compiler c.rex
call %REFALJ_HOME%\bin\rfc shiftargs rcmain rsserv rclist rccompn rcgen rcout rslex rssynj
if %errorlevel% EQU 0 copy shiftargs.rex+rcmain.rex+rsserv.rex+rclist.rex+rslex.rex+rssynj.rex+rccompn.rex+rcgen.rex+rcout.rex c.rex

pause