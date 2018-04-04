@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

SET path=%~dp0
SET /P filetype=[Please enter file extention]:

ECHO [FILE_TYPE]:%filetype%

FOR /R %path% %%a IN (*.%filetype%) DO (
SET file=%%a
SET filename=%%~na
FOR /F “tolen=1,* delims==“ %%x IN (%path%filename.properties) DO (
SET targetfilename=%%x
SET newfilename=%%y
IF !filename!==!targetfilename! (
CALL :RENAME_FILE !file! !newfilename! %filetype%
)
)
)
PAUSE
GOTO :EOF

:RENAME_FILE
IF EXIST %1 (
SET oldFile=%1
SET newFile=%2.%3
ECHO [SUCCEED] [FROM]!oldFile! [TO]!newFile!
REN !oldFile! !newFile!
)