@echo off
cd /d "%~dp0"
set "MVN=mvn"
where mvn >nul 2>&1
if errorlevel 1 (
  set "MVN=%USERPROFILE%\Downloads\apache-maven-3.10.0-bin\apache-maven\bin\mvn.cmd"
  if not exist "%MVN%" (
    echo Maven was not found. Open the project in VS Code and use your Maven full path.
    pause
    exit /b 1
  )
)
call "%MVN%" clean test javafx:run
pause
