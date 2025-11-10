# Script para ejecutar el cliente GUI (ruta independiente)
$here = $PSScriptRoot
if (-not $here) { $here = Get-Location }
Set-Location $here

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Iniciando Cliente GUI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "" 

# Opcional: pasar ruta del keystore del cliente si existe
$ckPath = Join-Path $PSScriptRoot "keystore/client-keystore.p12"
if (Test-Path $ckPath) {
	Write-Host "Usando keystore del cliente: $ckPath" -ForegroundColor Yellow
	java -Dclient.keystore.path="$ckPath" -cp bin com.banco.cliente.gui.ClienteBancarioGUI
} else {
	java -cp bin com.banco.cliente.gui.ClienteBancarioGUI
}
