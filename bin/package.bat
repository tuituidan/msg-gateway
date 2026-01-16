@echo off

cd ..
echo 编译后端
call mvn clean compile -DskipTests

echo 构建前端
cd web
call npm run build
cd ..

echo 拷贝前端到后端
call xcopy web\dist\* target\classes\static\ /E /I /Y

echo 打包应用
call mvn package -DskipTests

echo. & pause
