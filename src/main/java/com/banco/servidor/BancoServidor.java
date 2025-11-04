package com.banco.servidor;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz RMI que define las operaciones remotas del banco.
 * Esta interfaz es necesaria tanto para el servidor como para el cliente.
 */
public interface BancoServidor extends Remote {
    
    /**
     * Autentica a un usuario.
     * @param idCuenta ID de la cuenta.
     * @param password Contraseña.
     * @return true si la autenticación es exitosa, false en caso contrario.
     */
    boolean autenticar(String idCuenta, String password) throws RemoteException;

    /**
     * Consulta el saldo de una cuenta.
     * @param idCuenta ID de la cuenta.
     * @return El saldo de la cuenta.
     */
    double consultarSaldo(String idCuenta) throws RemoteException;

    /**
     * Transfiere fondos entre dos cuentas.
     * @param idCuentaOrigen Cuenta de origen.
     * @param idCuentaDestino Cuenta de destino.
     * @param monto Monto a transferir.
     * @return true si la transferencia fue exitosa, false si no.
     */
    boolean transferirFondos(String idCuentaOrigen, String idCuentaDestino, double monto) throws RemoteException;

    /**
     * Registra una operación (Depósito o Retiro).
     * @param idCuenta ID de la cuenta.
     * @param tipoOperacion "DEPOSITO" o "RETIRO".
     * @param monto Monto de la operación.
     * @return true si la operación fue exitosa, false si no.
     */
    boolean registrarOperacion(String idCuenta, String tipoOperacion, double monto) throws RemoteException;
}
