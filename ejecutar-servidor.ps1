# Script para ejecutar el servidor RMI (ruta independiente)
$here = $PSScriptRoot
if (-not $here) { $here = Get-Location }
Set-Location $here

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Iniciando Servidor RMI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

java -cp bin com.banco.servidor.ServidorRMI
