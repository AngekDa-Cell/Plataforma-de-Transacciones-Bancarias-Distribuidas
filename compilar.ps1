# Script para compilar el proyecto (independiente de la ruta del usuario)
$here = $PSScriptRoot
if (-not $here) { $here = Get-Location }
Set-Location $here

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

# Usar el layout Maven (src/main/java) como sourcepath
$sourcePath = "src/main/java"

# Archivos a compilar (servidor y cliente GUI)
$serverSources = @(
    Join-Path $sourcePath "com/banco/servidor/*.java"
)
$clientSources = @()
$clientSources += (Join-Path $sourcePath "com/banco/cliente/gui/*.java")
$clientSources += (Join-Path $sourcePath "com/banco/cliente/gui/views/*.java")

javac -d bin -sourcepath $sourcePath $serverSources $clientSources

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilación exitosa!" -ForegroundColor Green
} else {
    Write-Host "❌ Error en la compilación" -ForegroundColor Red
    exit 1
}
