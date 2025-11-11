# Busca keytool y ejecuta generar-certificados.ps1 si se encuentra
$locations = @(
  'C:\Program Files\Java',
  'C:\Program Files\Eclipse Adoptium',
  'C:\Program Files\Zulu',
  'C:\Program Files'
)
$found = $null
foreach($loc in $locations){
  if(Test-Path $loc){
    $candidate = Get-ChildItem -Path $loc -Recurse -Filter keytool.exe -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName
    if($candidate){
      $found = $candidate
      break
    }
  }
}
if($found){
  Write-Host "KEYTOOL_FOUND=$found"
  $env:JAVA_HOME = Split-Path (Split-Path $found)
  $env:Path = "$($env:JAVA_HOME)\bin;$env:Path"
  Write-Host "JAVA_HOME=$env:JAVA_HOME"
  & "$PSScriptRoot\generar-certificados.ps1"
}else{
  Write-Host "KEYTOOL_NOT_FOUND"
  exit 3
}
