package com.saucedemo.utilidades;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GESTOR DE CAPTURAS DE PANTALLA
 * Toma y guarda capturas cuando una prueba falla.
 * Puede devolver la imagen como archivo, bytes o Base64 para el reporte HTML.
 */
public class GestorCapturas {

    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Toma una captura y la guarda como archivo .png en la carpeta /capturas.
     * @return ruta absoluta del archivo guardado, o null si fallo
     */
    public static String tomarCaptura(String nombrePrueba) {
        WebDriver driver = GestorNavegador.obtenerDriver();
        if (driver == null) return null;

        try {
            byte[] imagenBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            String carpeta = GestorConfiguracion.obtenerInstancia().obtenerCarpetaCapturas();
            Path rutaCarpeta = Paths.get(carpeta);
            if (!Files.exists(rutaCarpeta)) {
                Files.createDirectories(rutaCarpeta);
            }

            String nombreLimpio = nombrePrueba.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            String nombreArchivo = nombreLimpio + "_" + timestamp + ".png";

            Path rutaArchivo = rutaCarpeta.resolve(nombreArchivo);
            Files.write(rutaArchivo, imagenBytes);

            String rutaAbsoluta = rutaArchivo.toAbsolutePath().toString();
            System.out.println("[Captura] Guardada en: " + rutaAbsoluta);
            return rutaAbsoluta;

        } catch (IOException error) {
            System.out.println("[Captura] ERROR: " + error.getMessage());
            return null;
        }
    }

    /**
     * Devuelve la captura en Base64 para embeberla en el reporte HTML.
     * @return imagen en Base64 como String, o null si fallo
     */
    public static String tomarCapturaBase64() {
        WebDriver driver = GestorNavegador.obtenerDriver();
        if (driver == null) return null;

        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception error) {
            System.out.println("[Captura] ERROR Base64: " + error.getMessage());
            return null;
        }
    }
}
