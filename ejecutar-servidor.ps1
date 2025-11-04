# Script para ejecutar el servidor RMI
cd "c:\Users\Ramse\OneDrive\Escritorio\proyectos\Plataforma-de-Transacciones-Bancarias-Distribuidas"

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Iniciando Servidor RMI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

java -cp bin com.banco.servidor.ServidorRMI
