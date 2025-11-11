# Genera keystore de cliente (PKCS12) y truststore de servidor (JKS) para cuentas de ejemplo
# Requisitos: keytool (viene con el JDK). Si no está en PATH, se usará $env:JAVA_HOME\bin\keytool.exe

param(
    [string[]]$Cuentas = @("CTA-1001","CTA-1002","CTA-1003"),
    [string]$ClienteKeystore = "keystore/client-keystore.p12",
    [string]$ClienteStorePass = "changeit",
    [string]$ClienteKeyPass = "changeit",
    [string]$ServidorTruststore = "keystore/server-truststore.jks",
    [string]$ServidorStorePass = "changeit",
    [int]$ValidezDias = 365
)

$ErrorActionPreference = "Stop"

function Find-Keytool {
    if (Get-Command keytool -ErrorAction SilentlyContinue) { return "keytool" }
    if ($env:JAVA_HOME) {
        $candidate = Join-Path $env:JAVA_HOME "bin/keytool.exe"
        if (Test-Path $candidate) { return $candidate }
    }
    throw "No se encontró 'keytool'. Instale un JDK y asegure PATH o JAVA_HOME."
}

$kt = Find-Keytool

# Asegurar carpeta
$root = $PSScriptRoot
if (-not $root) { $root = Get-Location }
Set-Location $root
New-Item -ItemType Directory -Force -Path "keystore" | Out-Null

Write-Host "==> Generando claves de cliente en $ClienteKeystore" -ForegroundColor Cyan
foreach ($cta in $Cuentas) {
    Write-Host "  - Generando par de claves para $cta ..." -ForegroundColor Yellow
    & $kt -genkeypair -alias $cta -keyalg RSA -keysize 2048 -validity $ValidezDias `
        -keystore $ClienteKeystore -storetype PKCS12 -storepass $ClienteStorePass `
        -keypass $ClienteKeyPass -dname "CN=$cta, OU=Banco, O=Banco, L=City, S=State, C=ES" | Out-Null
}

Write-Host "==> Exportando certificados públicos" -ForegroundColor Cyan
foreach ($cta in $Cuentas) {
    $crt = "keystore/$cta.crt"
    & $kt -exportcert -alias $cta -keystore $ClienteKeystore -storepass $ClienteStorePass -rfc > $crt
}

Write-Host "==> Creando truststore del servidor en $ServidorTruststore" -ForegroundColor Cyan
foreach ($cta in $Cuentas) {
    $crt = "keystore/$cta.crt"
    Write-Host "  - Importando $crt con alias $cta" -ForegroundColor Yellow
    & $kt -importcert -noprompt -alias $cta -file $crt -keystore $ServidorTruststore -storepass $ServidorStorePass | Out-Null
}

Write-Host "\nListo. Valores a usar en el cliente (Login con certificado):" -ForegroundColor Green
Write-Host "  Keystore (.p12): $ClienteKeystore"
Write-Host "  Password KS:    $ClienteStorePass"
Write-Host "  Alias:          (uno de) $($Cuentas -join ', ')"
Write-Host "  Password Clave: $ClienteKeyPass"

Write-Host "\nServidor: se detectará $ServidorTruststore automáticamente al ejecutar .\\ejecutar-servidor.ps1" -ForegroundColor Green
