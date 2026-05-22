package com.saucedemo.paginas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * CARRITO PAGE
 * Representa la pagina del carrito de compras (/cart.html).
 */
public class CarritoPage extends PaginaBase {

    // ── Localizadores ────────────────────────────────────────────────
    private static final By ITEMS_DEL_CARRITO        = By.cssSelector(".cart_item");
    private static final By NOMBRES_PRODUCTOS        = By.cssSelector(".inventory_item_name");
    private static final By BOTON_CHECKOUT           = By.id("checkout");
    private static final By BOTON_CONTINUAR_COMPRANDO = By.id("continue-shopping");

    // ── Acciones ─────────────────────────────────────────────────────

    public void irAlCheckout() {
        hacerClicEn(BOTON_CHECKOUT);
    }

    public void continuarComprando() {
        hacerClicEn(BOTON_CONTINUAR_COMPRANDO);
    }

    public int obtenerCantidadDeProductos() {
        return driver.findElements(ITEMS_DEL_CARRITO).size();
    }

    public List<String> obtenerNombresDeProductos() {
        return driver.findElements(NOMBRES_PRODUCTOS).stream()
            .map(WebElement::getText)
            .toList();
    }

    public boolean estaEnPaginaCarrito() {
        return obtenerUrlActual().contains("cart.html");
    }

    public boolean carritoEstaVacio() {
        return driver.findElements(ITEMS_DEL_CARRITO).isEmpty();
    }

    public boolean contieneProducto(String nombreProducto) {
        return obtenerNombresDeProductos().contains(nombreProducto);
    }
}
