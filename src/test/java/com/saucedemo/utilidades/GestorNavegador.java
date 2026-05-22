package com.saucedemo.utilidades;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * GESTOR DEL NAVEGADOR
 * Crea, configura y cierra el WebDriver (Chrome o Firefox).
 * Usa ThreadLocal para soportar ejecucion en paralelo en el futuro.
 */
public class GestorNavegador {

    private static final ThreadLocal<WebDriver> almacenDriver = new ThreadLocal<>();

    public static void iniciarNavegador(String nombreNavegador) {
        WebDriver driver;

        switch (nombreNavegador.toLowerCase().trim()) {
            case "firefox":
                driver = crearDriverFirefox();
                System.out.println("[Navegador] Firefox iniciado.");
                break;
            case "chrome":
            default:
                driver = crearDriverChrome();
                System.out.println("[Navegador] Chrome iniciado.");
                break;
        }

        driver.manage().window().maximize();
        almacenDriver.set(driver);
    }

    private static WebDriver crearDriverChrome() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions opciones = new ChromeOptions();
        opciones.addArguments("--start-maximized");
        opciones.addArguments("--disable-notifications");
        opciones.addArguments("--disable-popup-blocking");
        opciones.addArguments("--remote-allow-origins=*");

        if (GestorConfiguracion.obtenerInstancia().obtenerPropiedadBooleano("modo.headless")) {
            opciones.addArguments("--headless=new");
            opciones.addArguments("--window-size=1920,1080");
            opciones.addArguments("--no-sandbox");
            opciones.addArguments("--disable-dev-shm-usage");
        }

        return new ChromeDriver(opciones);
    }

    private static WebDriver crearDriverFirefox() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions opciones = new FirefoxOptions();

        if (GestorConfiguracion.obtenerInstancia().obtenerPropiedadBooleano("modo.headless")) {
            opciones.addArguments("--headless");
        }

        return new FirefoxDriver(opciones);
    }

    public static WebDriver obtenerDriver() {
        return almacenDriver.get();
    }

    public static void cerrarNavegador() {
        WebDriver driver = obtenerDriver();
        if (driver != null) {
            driver.quit();
            almacenDriver.remove();
            System.out.println("[Navegador] Navegador cerrado.");
        }
    }
}
