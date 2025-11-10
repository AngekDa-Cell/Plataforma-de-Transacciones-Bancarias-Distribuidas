# Plataforma de Transacciones Bancarias Distribuidas (Java RMI + Firmas Digitales)

Este proyecto implementa una plataforma bancaria distribuida usando Java RMI y agrega seguridad mediante:

- Autenticación opcional de usuarios con certificados digitales.
- Firmado digital (RSA + SHA-256) de todas las operaciones y transferencias.
- Registro de auditoría encadenado (hash SHA-256) en `audit.log`.
- Uso de Java Keystore / Truststore para almacenar claves y certificados.

## Estructura principal

```
src/main/java/com/banco/
	servidor/
		BancoServidor.java              # Interfaz remota (incluye métodos firmados)
		BancoServidorImpl.java          # Implementación + verificación de firmas + auditoría
		ServidorRMI.java                # Punto de entrada del servidor
	cliente/gui/
		ClienteBancarioGUI.java         # Ventana principal Swing
		RMIConnector.java               # Conexión RMI y carga de keystore
		views/                          # Vistas Swing (Login, Dashboard, Operación, Transferencia)
	security/
		SecurityUtil.java               # Utilidades de firma, hash y carga de keystore
		AuditLog.java                   # Registro de auditoría encadenado
```

Código legacy de demostración permanece en `src/banco` (versión simple sin firmas) y puede eliminarse si no se requiere.

## Requisitos

- JDK 11+ (recomendado) instalado y en el PATH (`javac`, `java`, `keytool`).
- PowerShell (scripts `.ps1` listos para Windows). Para otros SO, usar comandos `javac`/`java` equivalentes.

## Compilar

```powershell
./compilar.ps1
```

Genera clases en `bin/` usando la estructura tipo Maven `src/main/java`.

## Ejecutar

1. (Opcional, si desea usar certificados) Cree el keystore y truststore:
	```powershell
	# Ejemplo para generar claves (asegúrese de que 'keytool' esté disponible)
	New-Item -ItemType Directory -Force -Path keystore | Out-Null
	keytool -genkeypair -alias CTA-1001 -keyalg RSA -keysize 2048 -validity 365 -keystore keystore\client-keystore.p12 -storetype PKCS12 -storepass changeit -keypass changeit -dname "CN=CTA-1001, OU=Banco, O=Banco, L=City, S=State, C=ES"
	# Repetir para CTA-1002, CTA-1003
	keytool -exportcert -alias CTA-1001 -keystore keystore\client-keystore.p12 -storepass changeit -rfc > keystore\CTA-1001.crt
	keytool -importcert -noprompt -alias CTA-1001 -file keystore\CTA-1001.crt -keystore keystore\server-truststore.jks -storepass changeit
	```

2. Inicie el servidor:
	```powershell
	./ejecutar-servidor.ps1
	```

3. Inicie el cliente GUI:
	```powershell
	./ejecutar-cliente.ps1
	```

4. En la pantalla de Login puede:
	- Usar usuario/contraseña (modo clásico), o
	- Marcar "Usar autenticación con certificado" y proporcionar: ruta del keystore, password del keystore, alias y password de la clave.

5. Operaciones (saldo, depósitos, retiros, transferencias) se firmarán automáticamente si se autenticó con certificado.

## Seguridad Implementada

- Firma: `SHA256withRSA` sobre mensaje canónico específico por operación.
- Verificación: El servidor valida firma y que el certificado exista en su truststore.
- Auditoría: Cada transacción crea una línea en `audit.log` con el hash de la anterior (cadena inmutable básica).
- Hash: SHA-256 aplicado al bloque auditado.

## Mensajes Canónicos
- Login: `LOGIN|{idCuenta}|{timestamp}`
- Consultar saldo: `CONSULTAR|{idCuenta}|{timestamp}`
- Transferir: `TRANSFERIR|{idCuentaOrigen}|{idCuentaDestino}|{monto}|{timestamp}`
- Operación (Depósito/Retiro): `OPERACION|{idCuenta}|{tipo}|{monto}|{timestamp}`

## Archivos Clave
- `SecurityUtil.java`: carga de keystore, firma, verificación, SHA-256.
- `AuditLog.java`: registro encadenado.
- `BancoServidorImpl.java`: métodos firmados y validaciones.

## Limitaciones / Próximos Pasos
- No se implementa expiración ni revocación de certificados.
- No se cifra el canal RMI (puede añadirse con SSL socket factories).
- Truststore y keystore ejemplos usan contraseña débil (`changeit`). Cambiar en producción.
- Sugerido migrar a Maven/Gradle para gestionar dependencias y perfiles de entorno.

## Limpieza Legacy
Si no se necesita la versión simple en `src/banco`, se puede eliminar para evitar duplicidad.
