# Script para compilar el proyecto
cd "c:\Users\Ramse\OneDrive\Escritorio\proyectos\Plataforma-de-Transacciones-Bancarias-Distribuidas"

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Compilando Proyecto Banco RMI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan

# Limpiar y crear directorio bin
if (Test-Path "bin") {
    Remove-Item -Recurse -Force "bin"
}
New-Item -ItemType Directory -Path "bin" | Out-Null

# Compilar
Write-Host "`nCompilando archivos Java..." -ForegroundColor Yellow
javac -d bin -sourcepath src src\main\java\com\banco\servidor\*.java src\main\java\com\banco\cliente\gui\*.java src\main\java\com\banco\cliente\gui\views\*.java

if ($LASTEXITCODE -eq 0) {
    Write-Host "? Compilación exitosa!" -ForegroundColor Green
} else {
    Write-Host "? Error en la compilación" -ForegroundColor Red
    exit 1
}
