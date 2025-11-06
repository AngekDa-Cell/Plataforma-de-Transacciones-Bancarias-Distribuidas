# Script para ejecutar el cliente GUI (ruta independiente)
$here = $PSScriptRoot
if (-not $here) { $here = Get-Location }
Set-Location $here

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Iniciando Cliente GUI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

java -cp bin com.banco.cliente.gui.ClienteBancarioGUI
