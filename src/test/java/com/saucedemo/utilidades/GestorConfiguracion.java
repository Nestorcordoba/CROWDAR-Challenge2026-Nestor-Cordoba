package com.saucedemo.utilidades;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * GESTOR DE CONFIGURACION
 * Lee el archivo config.properties y expone sus valores al resto del framework.
 * Patron Singleton: una sola instancia durante toda la ejecucion.
 */
public class GestorConfiguracion {

    private static GestorConfiguracion instancia;
    private final Properties propiedades;
    private static final String RUTA_ARCHIVO = "src/test/resources/config.properties";

    private GestorConfiguracion() {
        propiedades = new Properties();
        cargarArchivo();
    }

    public static synchronized GestorConfiguracion obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorConfiguracion();
        }
        return instancia;
    }

    private void cargarArchivo() {
        try (FileInputStream flujo = new FileInputStream(RUTA_ARCHIVO)) {
            propiedades.load(flujo);
            System.out.println("[Configuracion] Archivo cargado: " + RUTA_ARCHIVO);
        } catch (IOException error) {
            throw new RuntimeException(
                "[Configuracion] No se pudo leer: " + RUTA_ARCHIVO, error
            );
        }
    }

    public String obtenerPropiedad(String clave) {
        return propiedades.getProperty(clave);
    }

    public int obtenerPropiedadEntero(String clave) {
        return Integer.parseInt(obtenerPropiedad(clave));
    }

    public boolean obtenerPropiedadBooleano(String clave) {
        return Boolean.parseBoolean(obtenerPropiedad(clave));
    }

    public String obtenerUrlBase()            { return obtenerPropiedad("url.base"); }
    public String obtenerUsuarioEstandar()    { return obtenerPropiedad("usuario.estandar"); }
    public String obtenerUsuarioBloqueado()   { return obtenerPropiedad("usuario.bloqueado"); }
    public String obtenerContrasenaValida()   { return obtenerPropiedad("contrasena.valida"); }
    public String obtenerNavegador()          { return obtenerPropiedad("navegador"); }
    public int    obtenerEsperaExplicita()    { return obtenerPropiedadEntero("espera.explicita"); }
    public String obtenerCarpetaCapturas()    { return obtenerPropiedad("carpeta.capturas"); }
    public String obtenerCarpetaReportes()    { return obtenerPropiedad("carpeta.reportes"); }
    public String obtenerNombreReporte()      { return obtenerPropiedad("nombre.reporte"); }
    public String obtenerUrlApiMercadoLibre() { return obtenerPropiedad("api.mercadolibre.departamentos"); }
}
