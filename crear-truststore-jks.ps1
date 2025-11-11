# Crea/actualiza el truststore del servidor (JKS) importando los CRT existentes de clientes
param(
  [string]$Truststore = "keystore/server-truststore.jks",
  [string]$StorePass = "changeit",
  [string]$CertsDir = "keystore",
  [string]$ClientKeystore = "keystore/client-keystore.p12",
  [string]$ClientStorePass = "changeit"
)

$ErrorActionPreference = "Stop"
$here = $PSScriptRoot
if (-not $here) { $here = Get-Location }
Set-Location $here

function Find-Keytool {
  if (Get-Command keytool -ErrorAction SilentlyContinue) { return "keytool" }
  if ($env:JAVA_HOME) {
    $candidate = Join-Path $env:JAVA_HOME "bin/keytool.exe"
    if (Test-Path $candidate) { return $candidate }
  }
  $locations = @(
    'C:\\Program Files\\Java',
    'C:\\Program Files\\Eclipse Adoptium',
    'C:\\Program Files\\Zulu',
    'C:\\Program Files'
  )
  foreach($loc in $locations){
    if(Test-Path $loc){
      $candidate = Get-ChildItem -Path $loc -Recurse -Filter keytool.exe -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName
      if($candidate){ return $candidate }
    }
  }
  throw "No se encontró 'keytool'. Instale un JDK y asegure PATH o JAVA_HOME."
}

$kt = Find-Keytool

# Asegurar carpeta keystore
New-Item -ItemType Directory -Force -Path $CertsDir | Out-Null

# Importar todos los certificados .crt
if (-not (Test-Path $ClientKeystore)) {
  Write-Host "No se encontró el keystore de cliente en $ClientKeystore" -ForegroundColor Red
  exit 2
}

# Obtener lista de aliases del keystore PKCS12 de cliente (soportar ES/EN)
$rawList = & $kt -list -keystore $ClientKeystore -storepass $ClientStorePass -storetype PKCS12 2>$null
$aliasLines = $rawList | Select-String -Pattern "Alias name:|Nombre de alias:"
$aliases = @()
foreach($line in $aliasLines){
  $text = $line.ToString()
  $m1 = [regex]::Match($text, 'Alias name:\s*(.+)$')
  $m2 = [regex]::Match($text, 'Nombre de alias:\s*(.+)$')
  if ($m1.Success) { $aliases += $m1.Groups[1].Value.Trim() }
  elseif ($m2.Success) { $aliases += $m2.Groups[1].Value.Trim() }
}

# Fallback: probar con aliases conocidos si no se detectan
if (-not $aliases -or $aliases.Count -eq 0) {
  Write-Host "No se detectaron aliases automáticamente. Probando con CTA-1001, CTA-1002, CTA-1003..." -ForegroundColor Yellow
  $aliases = @("CTA-1001","CTA-1002","CTA-1003")
}

foreach($alias in $aliases){
  $crtPath = Join-Path $CertsDir ("$alias.crt")
  Write-Host "Exportando certificado de alias $alias a $crtPath" -ForegroundColor Yellow
  try {
    & $kt -exportcert -alias $alias -keystore $ClientKeystore -storepass $ClientStorePass -storetype PKCS12 -rfc -file $crtPath | Out-Null
    if (Test-Path $crtPath) {
      Write-Host "Importando $crtPath como alias $alias en truststore JKS" -ForegroundColor Yellow
      & $kt -importcert -noprompt -alias $alias -file $crtPath -keystore $Truststore -storepass $StorePass -storetype JKS | Out-Null
    }
  } catch {
    $msg = $_.Exception.Message
  Write-Host ("(Aviso) No se pudo exportar/importar alias {0}: {1}" -f $alias, $msg) -ForegroundColor DarkYellow
  }
}

if (Test-Path $Truststore) {
  Write-Host "[OK] Truststore creado/actualizado: $Truststore" -ForegroundColor Green
  try {
    & $kt -list -keystore $Truststore -storepass $StorePass -storetype JKS
  } catch { Write-Host "(Aviso) No se pudo listar el truststore: $($_.Exception.Message)" -ForegroundColor Yellow }
} else {
  Write-Host "[ERROR] No se pudo crear el truststore en $Truststore" -ForegroundColor Red
  exit 2
}
