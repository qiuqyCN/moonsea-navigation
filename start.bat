@echo off
echo ======================================
echo 月海导航项目启动脚本
echo ======================================
echo.

echo 检查 PostgreSQL 数据库...
echo 请确保 PostgreSQL 已启动，并且已创建 moonsea_navigation 数据库
echo.

echo 正在启动项目...
echo.

mvn clean spring-boot:run

pause
