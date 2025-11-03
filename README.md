# Plataforma de Transacciones Bancarias Distribuidas (Java RMI)

Este proyecto contiene un esqueleto básico de una plataforma bancaria distribuida usando Java RMI.

## Estructura

- `src/banco/BancoServidor.java`: Interfaz remota.
- `src/banco/BancoServidorImpl.java`: Implementación del servidor.
- `src/banco/ServidorRMI.java`: Clase para arrancar el servidor RMI.
- `src/banco/ClienteBancario.java`: Cliente de ejemplo.

## Requisitos
- JDK 8+ instalado y en el PATH (`javac`, `java`).

## Compilar

En PowerShell, desde la carpeta raíz del proyecto:

```powershell
# Crear carpeta de salida
New-Item -ItemType Directory -Force -Path .\out | Out-Null

# Compilar todas las clases
javac -d .\out .\src\banco\*.java
```

## Ejecutar

Primero, inicie el servidor (manténgalo abierto):

```powershell
# Desde la raíz del proyecto
java -cp .\out banco.ServidorRMI
```

En otra ventana de PowerShell, ejecute el cliente:

```powershell
# Desde la raíz del proyecto
java -cp .\out banco.ClienteBancario
```

Debería ver mensajes de consulta de saldo, transferencia y un intento de retiro que falla por falta de fondos.

## Notas
- El servidor crea el registro RMI en el puerto 1099 y hace `rebind` con el nombre `BancoServidor`.
- La "base de datos" es un `HashMap` en memoria. Los métodos que modifican saldos son `synchronized`.
