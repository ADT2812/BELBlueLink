@echo off

cd /d "%~dp0frontend"

java ^
--module-path "..\javafx-sdk-26.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-cp "out;src\main\resources" ^
com.messenger.Main

pause