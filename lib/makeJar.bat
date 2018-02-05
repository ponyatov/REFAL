if not exist classes mkdir classes
set nopause=yes
call compile classes
cd classes
call jar cvf ../../reflib.jar *
@pause