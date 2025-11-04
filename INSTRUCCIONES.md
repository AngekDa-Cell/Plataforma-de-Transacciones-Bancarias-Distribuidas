# PLATAFORMA DE TRANSACCIONES BANCARIAS DISTRIBUIDAS

## 🚀 Cómo Ejecutar el Proyecto

### 1. Compilar (solo la primera vez o después de cambios)
```powershell
.\compilar.ps1
```

### 2. Ejecutar el Servidor RMI (Terminal 1)
```powershell
.\ejecutar-servidor.ps1
```

El servidor mostrará:
- Puerto de escucha: 1099
- Cuentas de prueba disponibles

### 3. Ejecutar el Cliente GUI (Terminal 2)
```powershell
.\ejecutar-cliente.ps1
```

Se abrirá la ventana de login de la aplicación.

---

## 🔐 Credenciales de Prueba

| ID Cuenta | Contraseña | Saldo Inicial |
|-----------|------------|---------------|
| CTA-1001  | 1234       | $5,000.00     |
| CTA-1002  | 1234       | $2,000.00     |
| CTA-1003  | 1234       | $1,000.00     |

---

## 📋 Funcionalidades Implementadas

### ✅ Vistas Swing (estilo Tkinter)
1. **LoginView** - Inicio de sesión con validación
2. **DashboardView** - Panel principal con saldo y navegación
3. **OperationView** - Depósitos y retiros
4. **TransferView** - Transferencias entre cuentas

### ✅ Operaciones RMI
- Autenticación de usuarios
- Consulta de saldo en tiempo real
- Transferencias entre cuentas
- Depósitos y retiros
- Validaciones de negocio

### ✅ Características de UX
- Validación de formularios
- Mensajes de error y éxito
- Confirmaciones de operaciones
- Actualización automática de saldo
- Navegación fluida entre vistas

---

## 🛠️ Estructura del Proyecto

```
src/
├── main/java/com/banco/
│   ├── servidor/
│   │   ├── BancoServidor.java          # Interfaz RMI
│   │   ├── BancoServidorImpl.java      # Implementación del servidor
│   │   └── ServidorRMI.java            # Punto de entrada del servidor
│   └── cliente/gui/
│       ├── ClienteBancarioGUI.java     # Clase principal cliente
│       ├── RMIConnector.java           # Conector RMI
│       └── views/
│           ├── LoginView.java          # Vista de login
│           ├── DashboardView.java      # Vista principal
│           ├── OperationView.java      # Vista de operaciones
│           └── TransferView.java       # Vista de transferencias
```

---

## 📝 Notas Técnicas

- **Tecnología GUI**: Java Swing (similar a Tkinter de Python)
- **Comunicación**: Java RMI (Remote Method Invocation)
- **Puerto RMI**: 1099
- **Arquitectura**: Cliente-Servidor distribuido
- **Thread-Safe**: Operaciones sincronizadas en el servidor

---

## 🔄 Flujo de Uso

1. Iniciar servidor RMI
2. Iniciar cliente GUI
3. Ingresar con una cuenta de prueba
4. Realizar operaciones:
   - Ver saldo actual
   - Hacer transferencias
   - Realizar depósitos/retiros
   - Actualizar saldo
5. Cerrar sesión

---

## ⚠️ Solución de Problemas

**Si el cliente no se conecta:**
- Verificar que el servidor RMI esté ejecutándose
- Comprobar que el puerto 1099 no esté bloqueado
- Revisar que ambos estén usando el mismo host (localhost)

**Si hay errores de compilación:**
- Ejecutar `.\compilar.ps1` nuevamente
- Verificar que Java JDK esté instalado correctamente

---

## 👨‍💻 Desarrollo

Proyecto implementado con:
- Java SE (Standard Edition)
- Swing para interfaces gráficas
- RMI para comunicación distribuida
- Arquitectura cliente-servidor

---
