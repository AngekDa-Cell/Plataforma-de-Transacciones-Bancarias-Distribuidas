# Script para ejecutar el cliente GUI
cd "c:\Users\Ramse\OneDrive\Escritorio\proyectos\Plataforma-de-Transacciones-Bancarias-Distribuidas"

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Iniciando Cliente GUI" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

java -cp bin com.banco.cliente.gui.ClienteBancarioGUI
