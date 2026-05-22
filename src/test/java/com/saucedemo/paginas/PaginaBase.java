package com.saucedemo.paginas;

import com.saucedemo.utilidades.GestorConfiguracion;
import com.saucedemo.utilidades.GestorNavegador;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * PAGINA BASE
 * Clase abstracta de la que heredan todas las paginas del framework.
 * Centraliza el driver, las esperas explicitas y los metodos de interaccion comunes.
 */
public abstract class PaginaBase {

    protected WebDriver driver;
    protected WebDriverWait espera;
    protected GestorConfiguracion configuracion;

    public PaginaBase() {
        this.driver = GestorNavegador.obtenerDriver();
        this.configuracion = GestorConfiguracion.obtenerInstancia();
        this.espera = new WebDriverWait(
            driver,
            Duration.ofSeconds(configuracion.obtenerEsperaExplicita())
        );
    }

    // ── Esperas ──────────────────────────────────────────────────────

    /** Espera hasta que el elemento sea visible en pantalla */
    protected WebElement esperarQueSeaVisible(By localizador) {
        return espera.until(ExpectedConditions.visibilityOfElementLocated(localizador));
    }

    /** Espera hasta que el elemento sea visible Y habilitado para hacer clic */
    protected WebElement esperarQueSeaClicable(By localizador) {
        return espera.until(ExpectedConditions.elementToBeClickable(localizador));
    }

    /** Espera hasta que la URL del navegador contenga el fragmento indicado */
    protected void esperarUrlContiene(String fragmento) {
        espera.until(ExpectedConditions.urlContains(fragmento));
    }

    // ── Interacciones ────────────────────────────────────────────────

    /** Limpia el campo y escribe el texto indicado */
    protected void escribirEn(By localizador, String texto) {
        WebElement campo = esperarQueSeaVisible(localizador);
        campo.clear();
        campo.sendKeys(texto);
    }

    /** Hace clic en el elemento esperando que este clicable */
    protected void hacerClicEn(By localizador) {
        esperarQueSeaClicable(localizador).click();
    }

    /** Devuelve el texto visible del elemento */
    protected String obtenerTexto(By localizador) {
        return esperarQueSeaVisible(localizador).getText();
    }

    /** Verifica si el elemento existe en el DOM (puede no ser visible) */
    protected boolean elementoEstaPresente(By localizador) {
        return !driver.findElements(localizador).isEmpty();
    }

    /** Navega a la URL base configurada en config.properties */
    public void abrirPaginaPrincipal() {
        driver.get(configuracion.obtenerUrlBase());
    }

    /** Devuelve la URL actual del navegador */
    protected String obtenerUrlActual() {
        return driver.getCurrentUrl();
    }
}
