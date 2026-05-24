package com.saucedemo.utilidades;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * ESCUCHADOR DE PRUEBAS (TestNG Listener)
 * Se registra en testng.xml y reacciona automaticamente a cada evento:
 *   - onStart       : inicializa el reporte al comenzar la suite
 *   - onTestStart   : crea el nodo del test en el reporte
 *   - onTestSuccess : registra el exito
 *   - onTestFailure : toma la captura y registra el fallo con imagen
 *   - onTestSkipped : registra la omision
 *   - onFinish      : guarda el reporte HTML al terminar la suite
 */
public class EscuchadorDePruebas implements ITestListener {

    @Override
    public void onStart(ITestContext contexto) {
        System.out.println("\n══════════════════════════════════════");
        System.out.println("║  INICIANDO SUITE: " + contexto.getName());
        System.out.println("═════════════════════════════════════\n");
        GestorReportes.inicializar();
    }

    @Override
    public void onFinish(ITestContext contexto) {
        System.out.println("\n══════════════════════════════════════");
        System.out.println("║  SUITE FINALIZADA: " + contexto.getName());
        System.out.println("║  Pasadas:  " + contexto.getPassedTests().size());
        System.out.println("║  Fallidas: " + contexto.getFailedTests().size());
        System.out.println("║  Omitidas: " + contexto.getSkippedTests().size());
        System.out.println("═════════════════════════════════════\n");
        GestorReportes.finalizar();
    }

    @Override
    public void onTestStart(ITestResult resultado) {
        String nombre = resultado.getMethod().getMethodName();
        String descripcion = resultado.getMethod().getDescription();
        System.out.println("\nIniciando: " + nombre);

        GestorReportes.crearPrueba(
            nombre,
            descripcion != null && !descripcion.isEmpty()
                ? descripcion
                : "Prueba automatizada"
        );
    }

    @Override
    public void onTestSuccess(ITestResult resultado) {
        System.out.println("PASS: " + resultado.getMethod().getMethodName());
        GestorReportes.registrarPasoExitoso(
            "Prueba finalizada correctamente: " + resultado.getMethod().getMethodName()
        );
    }

    @Override
    public void onTestFailure(ITestResult resultado) {
        String nombre = resultado.getMethod().getMethodName();
        Throwable error = resultado.getThrowable();

        System.out.println("FAIL: " + nombre);
        if (error != null) System.out.println("  Causa: " + error.getMessage());

        // Guarda la captura como archivo en /capturas
        String rutaCaptura = GestorCapturas.tomarCaptura(nombre);
        if (rutaCaptura != null) {
            System.out.println("  Captura guardada: " + rutaCaptura);
        }

        // Registra el fallo con la imagen embebida en el reporte HTML
        GestorReportes.registrarFallo(
            "FALLO en '" + nombre + "': " +
            (error != null ? error.getMessage() : "Error desconocido"),
            error
        );
    }

    @Override
    public void onTestSkipped(ITestResult resultado) {
        System.out.println("SKIP: " + resultado.getMethod().getMethodName());
        GestorReportes.registrarOmitida(
            "Prueba omitida: " + resultado.getMethod().getMethodName()
        );
    }
}
