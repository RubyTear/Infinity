@echo off
@rem start cmd
set CSC_PATH=C:\Windows\Microsoft.NET\Framework\v4.0.30319
set path=%path%;%CSC_PATH%;
cmd /k echo "C#コンパイラー起動"
@rem cmd /k path