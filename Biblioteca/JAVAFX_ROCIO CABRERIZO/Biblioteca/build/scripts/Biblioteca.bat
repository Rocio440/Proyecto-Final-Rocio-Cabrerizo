@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  Biblioteca startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and BIBLIOTECA_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\Biblioteca.jar;%APP_HOME%\lib\javafx-fxml-21-win.jar;%APP_HOME%\lib\javafx-web-21-win.jar;%APP_HOME%\lib\javafx-controls-21-win.jar;%APP_HOME%\lib\javafx-controls-21.jar;%APP_HOME%\lib\javafx-media-21-win.jar;%APP_HOME%\lib\javafx-media-21.jar;%APP_HOME%\lib\javafx-swing-21-win.jar;%APP_HOME%\lib\javafx-graphics-21-win.jar;%APP_HOME%\lib\javafx-graphics-21.jar;%APP_HOME%\lib\javafx-base-21-win.jar;%APP_HOME%\lib\javafx-base-21.jar;%APP_HOME%\lib\guava-31.0.1-jre.jar;%APP_HOME%\lib\jasperreports-6.18.0.jar;%APP_HOME%\lib\itext-2.1.7.jar;%APP_HOME%\lib\groovy-4.0.15.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.12.0.jar;%APP_HOME%\lib\error_prone_annotations-2.7.1.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\mysql-connector-j-8.0.32.jar;%APP_HOME%\lib\bcmail-jdk14-138.jar;%APP_HOME%\lib\bcprov-jdk14-138.jar;%APP_HOME%\lib\commons-digester-2.1.jar;%APP_HOME%\lib\commons-beanutils-1.9.4.jar;%APP_HOME%\lib\castor-xml-1.4.1.jar;%APP_HOME%\lib\castor-core-1.4.1.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-collections4-4.2.jar;%APP_HOME%\lib\jfreechart-1.0.19.jar;%APP_HOME%\lib\jcommon-1.0.23.jar;%APP_HOME%\lib\ecj-3.21.0.jar;%APP_HOME%\lib\jackson-annotations-2.12.2.jar;%APP_HOME%\lib\jackson-databind-2.12.2.jar;%APP_HOME%\lib\jackson-core-2.12.2.jar;%APP_HOME%\lib\protobuf-java-3.21.9.jar;%APP_HOME%\lib\bctsp-jdk14-1.38.jar;%APP_HOME%\lib\commons-collections-3.2.2.jar;%APP_HOME%\lib\commons-lang3-3.4.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\bcmail-jdk14-1.38.jar;%APP_HOME%\lib\bcprov-jdk14-1.38.jar


@rem Execute Biblioteca
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %BIBLIOTECA_OPTS%  -classpath "%CLASSPATH%" crud.Main %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable BIBLIOTECA_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%BIBLIOTECA_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
