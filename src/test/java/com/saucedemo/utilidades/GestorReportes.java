package com.saucedemo.utilidades;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GESTOR DE REPORTES
 * Crea y gestiona el reporte HTML con ExtentReports.
 * Cada prueba tiene su nodo con logs y capturas embebidas en caso de fallo.
 */
public class GestorReportes {

    private static ExtentReports reporte;
    private static final ThreadLocal<ExtentTest> testActual = new ThreadLocal<>();

    /**
     * Inicializa el reporte solo si todavia no fue inicializado.
     * Util cuando se corre una clase de test directamente sin pasar por el @BeforeSuite de la suite.
     */
    public static synchronized void inicializarSiEsNecesario() {
        if (reporte == null) {
            inicializar();
        }
    }

    /**
     * Inicializa el sistema de reportes. Llamar una sola vez al inicio de la suite.
     */
    public static void inicializar() {
        GestorConfiguracion config = GestorConfiguracion.obtenerInstancia();

        String carpeta = config.obtenerCarpetaReportes();
        new File(carpeta).mkdirs();

        String rutaReporte = carpeta + File.separator + config.obtenerNombreReporte();

        ExtentSparkReporter reportadorHtml = new ExtentSparkReporter(rutaReporte);
        reportadorHtml.config().setTheme(Theme.STANDARD);
        reportadorHtml.config().setDocumentTitle("Reporte de Automatizacion - SauceDemo");
        reportadorHtml.config().setReportName("Resultados de Pruebas Automatizadas");
        reportadorHtml.config().setEncoding("UTF-8");
        reportadorHtml.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

        reporte = new ExtentReports();
        reporte.attachReporter(reportadorHtml);

        reporte.setSystemInfo("Aplicacion bajo prueba", "https://www.saucedemo.com");
        reporte.setSystemInfo("Autor", "Nestor German");
        reporte.setSystemInfo("Navegador", config.obtenerNavegador());
        reporte.setSystemInfo("Sistema Operativo", System.getProperty("os.name"));
        reporte.setSystemInfo("Version Java", System.getProperty("java.version"));
        reporte.setSystemInfo("Fecha",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        System.out.println("[Reporte] Iniciado. Se guardara en: " + rutaReporte);
    }

    /**
     * Crea un nodo en el reporte para la prueba que comienza.
     */
    public static void crearPrueba(String nombrePrueba, String descripcion) {
        ExtentTest test = reporte.createTest(nombrePrueba, descripcion);
        testActual.set(test);
    }

    public static void registrarPasoExitoso(String mensaje) {
        ExtentTest test = testActual.get();
        if (test != null) test.log(Status.PASS, mensaje);
    }

    public static void registrarInformacion(String mensaje) {
        ExtentTest test = testActual.get();
        if (test != null) test.log(Status.INFO, mensaje);
    }

    /**
     * Registra un fallo y embebe la captura de pantalla en el reporte HTML.
     */
    public static void registrarFallo(String mensaje, Throwable excepcion) {
        ExtentTest test = testActual.get();
        if (test == null) return;

        String capturaBase64 = GestorCapturas.tomarCapturaBase64();

        if (capturaBase64 != null) {
            test.fail(mensaje,
                MediaEntityBuilder.createScreenCaptureFromBase64String(capturaBase64).build());
        } else {
            test.log(Status.FAIL, mensaje);
        }

        if (excepcion != null) {
            test.log(Status.FAIL, excepcion);
        }
    }

    public static void registrarOmitida(String mensaje) {
        ExtentTest test = testActual.get();
        if (test != null) test.log(Status.SKIP, mensaje);
    }

    /**
     * Guarda el reporte HTML en disco. Llamar una sola vez al final de la suite.
     */
    public static void finalizar() {
        if (reporte != null) {
            reporte.flush();
            System.out.println("[Reporte] Guardado en disco.");
        }
    }

    public static ExtentTest obtenerTestActual() {
        return testActual.get();
    }
}
