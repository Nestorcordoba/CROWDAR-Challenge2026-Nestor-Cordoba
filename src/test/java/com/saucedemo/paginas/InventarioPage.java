package com.saucedemo.paginas;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * INVENTARIO PAGE
 * Representa la pagina de productos (/inventory.html).
 * Contiene los metodos para agregar/eliminar productos y navegar al carrito.
 *
 * IDs de productos disponibles en SauceDemo:
 *   sauce-labs-backpack           -> Mochila        ($29.99)
 *   sauce-labs-bike-light         -> Luz de bici    ($9.99)
 *   sauce-labs-bolt-t-shirt       -> Remera         ($15.99)
 *   sauce-labs-fleece-jacket      -> Campera        ($49.99)
 *   sauce-labs-onesie             -> Mameluco       ($7.99)
 */
public class InventarioPage extends PaginaBase {

    // ── Localizadores fijos ──────────────────────────────────────────
    private static final By ICONO_CARRITO    = By.id("shopping_cart_container");
    private static final By CONTADOR_CARRITO = By.cssSelector(".shopping_cart_badge");
    private static final By BOTON_MENU       = By.id("react-burger-menu-btn");
    private static final By OPCION_LOGOUT    = By.id("logout_sidebar_link");
    private static final By TITULO_PRODUCTOS = By.cssSelector(".title");
    private static final By LISTA_PRECIOS    = By.cssSelector(".inventory_item_price");

    // ── Localizadores dinamicos (dependen del producto) ──────────────
    private By botonAgregar(String idProducto) {
        return By.id("add-to-cart-" + idProducto);
    }

    private By botonEliminar(String idProducto) {
        return By.id("remove-" + idProducto);
    }

    // ── Acciones ─────────────────────────────────────────────────────

    public void agregarProductoAlCarrito(String idProducto) {
        hacerClicEn(botonAgregar(idProducto));
    }

    public void eliminarProductoDelCarrito(String idProducto) {
        hacerClicEn(botonEliminar(idProducto));
    }

    public void irAlCarrito() {
        hacerClicEn(ICONO_CARRITO);
    }

    /** Devuelve la cantidad que muestra el badge del carrito (0 si esta vacio) */
    public int obtenerCantidadEnCarrito() {
        if (!elementoEstaPresente(CONTADOR_CARRITO)) return 0;
        return Integer.parseInt(obtenerTexto(CONTADOR_CARRITO));
    }

    public void cerrarSesion() {
        hacerClicEn(BOTON_MENU);
        esperarQueSeaClicable(OPCION_LOGOUT).click();
    }

    public boolean estaEnPaginaInventario() {
        return elementoEstaPresente(TITULO_PRODUCTOS);
    }

    /** Devuelve true si el producto muestra el boton "Remove" (ya fue agregado) */
    public boolean productoEstaEnCarrito(String idProducto) {
        return elementoEstaPresente(botonEliminar(idProducto));
    }

    /** Devuelve los precios listados como doubles para verificar ordenamiento */
    public List<Double> obtenerPreciosListados() {
        List<WebElement> elementos = driver.findElements(LISTA_PRECIOS);
        return elementos.stream()
            .map(e -> Double.parseDouble(e.getText().replace("$", "")))
            .toList();
    }
}
