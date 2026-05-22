package com.saucedemo.pruebas;

import com.saucedemo.utilidades.GestorConfiguracion;
import com.saucedemo.utilidades.GestorNavegador;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * PRUEBA BASE
 * Clase de la que heredan todos los tests.
 * Maneja la apertura y cierre del navegador antes y despues de cada prueba.
 *
 *   @BeforeMethod → abre el navegador y navega al sitio antes de cada @Test
 *   @AfterMethod  → cierra el navegador al terminar cada @Test (siempre, incluso si falla)
 */
public abstract class PruebaBase {

    protected GestorConfiguracion configuracion = GestorConfiguracion.obtenerInstancia();

    @BeforeMethod(alwaysRun = true)
    @Parameters({"navegador"})
    public void configurarAntesDeLaPrueba(@Optional("chrome") String navegador) {
        System.out.println("\n── Iniciando prueba con: " + navegador + " ──");
        GestorNavegador.iniciarNavegador(navegador);
        GestorNavegador.obtenerDriver().get(configuracion.obtenerUrlBase());
    }

    @AfterMethod(alwaysRun = true)
    public void limpiarDespuesDeLaPrueba() {
        System.out.println("── Cerrando navegador ──\n");
        GestorNavegador.cerrarNavegador();
    }
}
