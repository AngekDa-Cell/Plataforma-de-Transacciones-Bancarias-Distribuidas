# Script para ejecutar el servidor RMI (ruta independiente)
$here = $PSScriptRoot
if (-not $here) { $here = Get-Location }
Set-Location $here

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Iniciando Servidor RMI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "" 

# Opcional: pasar truststore si existe
$tsPath = Join-Path $PSScriptRoot "keystore/server-truststore.jks"
if (-not (Test-Path $tsPath)) {
	Write-Host "No se encontró truststore. Intentando crearlo desde keystore/*.crt ..." -ForegroundColor Yellow
	$crear = Join-Path $PSScriptRoot "crear-truststore-jks.ps1"
	if (Test-Path $crear) {
		try {
			& $crear
		} catch {
			Write-Host "No se pudo crear el truststore automáticamente: $($_.Exception.Message)" -ForegroundColor Red
		}
	}
}

if (Test-Path $tsPath) {
	Write-Host "Usando truststore del servidor: $tsPath" -ForegroundColor Yellow
	$java = "java"
	& $java "-Dserver.truststore.path=$tsPath" "-Dserver.truststore.password=changeit" "-cp" "bin" "com.banco.servidor.ServidorRMI"
} else {
	Write-Host "ADVERTENCIA: No se encontró keystore\\server-truststore.jks. La verificación de certificados fallará." -ForegroundColor Red
	$java = "java"
	& $java "-cp" "bin" "com.banco.servidor.ServidorRMI"
}
