package com.saucedemo.pruebas;

import com.saucedemo.paginas.CarritoPage;
import com.saucedemo.paginas.InventarioPage;
import com.saucedemo.paginas.LoginPage;
import com.saucedemo.utilidades.GestorReportes;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PRUEBAS DEL CARRITO DE COMPRAS
 * Casos de prueba automatizados para el modulo de carrito.
 * Todos los tests comienzan con un login valido mediante
 * el metodo privado iniciarSesionComoUsuarioEstandar().
 */
public class CarritoTest extends PruebaBase {

    private static final String PRODUCTO_MOCHILA  = "sauce-labs-backpack";
    private static final String PRODUCTO_LUZ_BICI = "sauce-labs-bike-light";
    private static final String NOMBRE_MOCHILA    = "Sauce Labs Backpack";
    private static final String NOMBRE_LUZ_BICI   = "Sauce Labs Bike Light";

    // ════════════════════════════════════════════════════════════
    // CP-C-01: AGREGAR UN PRODUCTO
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-C-01: Agregar un producto al carrito actualiza el contador a 1",
        priority = 1
    )
    public void agregarUnProductoAlCarrito() {
        InventarioPage inventario = iniciarSesionComoUsuarioEstandar();

        Assert.assertEquals(inventario.obtenerCantidadEnCarrito(), 0,
            "El carrito deberia estar vacio al inicio.");

        GestorReportes.registrarInformacion("Agregando Sauce Labs Backpack al carrito");
        inventario.agregarProductoAlCarrito(PRODUCTO_MOCHILA);

        int cantidad = inventario.obtenerCantidadEnCarrito();
        GestorReportes.registrarInformacion("Contador del carrito: " + cantidad);

        Assert.assertEquals(cantidad, 1,
            "El contador debia ser 1 despues de agregar un producto.");

        Assert.assertTrue(inventario.productoEstaEnCarrito(PRODUCTO_MOCHILA),
            "El boton debia cambiar a 'Remove' despues de agregar.");

        GestorReportes.registrarPasoExitoso("Producto agregado. Contador: " + cantidad);
    }

    // ════════════════════════════════════════════════════════════
    // CP-C-02: AGREGAR MULTIPLES PRODUCTOS
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-C-02: Agregar dos productos actualiza el contador a 2",
        priority = 2
    )
    public void agregarMultiplesProductosAlCarrito() {
        InventarioPage inventario = iniciarSesionComoUsuarioEstandar();

        GestorReportes.registrarInformacion("Agregando Mochila al carrito");
        inventario.agregarProductoAlCarrito(PRODUCTO_MOCHILA);

        GestorReportes.registrarInformacion("Agregando Luz de bicicleta al carrito");
        inventario.agregarProductoAlCarrito(PRODUCTO_LUZ_BICI);

        int cantidad = inventario.obtenerCantidadEnCarrito();
        GestorReportes.registrarInformacion("Contador del carrito: " + cantidad);

        Assert.assertEquals(cantidad, 2,
            "El contador debia ser 2 despues de agregar dos productos.");

        GestorReportes.registrarPasoExitoso("Dos productos agregados. Contador: " + cantidad);
    }

    // ════════════════════════════════════════════════════════════
    // CP-C-03: VER CONTENIDO DEL CARRITO
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-C-03: El carrito muestra correctamente los productos agregados",
        priority = 3
    )
    public void verContenidoDelCarrito() {
        InventarioPage inventario = iniciarSesionComoUsuarioEstandar();

        inventario.agregarProductoAlCarrito(PRODUCTO_MOCHILA);
        inventario.agregarProductoAlCarrito(PRODUCTO_LUZ_BICI);

        GestorReportes.registrarInformacion("Navegando al carrito");
        inventario.irAlCarrito();

        CarritoPage carrito = new CarritoPage();

        Assert.assertTrue(carrito.estaEnPaginaCarrito(),
            "Se esperaba estar en /cart.html.");

        Assert.assertEquals(carrito.obtenerCantidadDeProductos(), 2,
            "El carrito debia tener 2 productos.");

        Assert.assertTrue(carrito.contieneProducto(NOMBRE_MOCHILA),
            "La mochila debia aparecer en el carrito.");

        Assert.assertTrue(carrito.contieneProducto(NOMBRE_LUZ_BICI),
            "La luz de bicicleta debia aparecer en el carrito.");

        GestorReportes.registrarPasoExitoso(
            "Carrito verificado: " + carrito.obtenerNombresDeProductos()
        );
    }

    // ════════════════════════════════════════════════════════════
    // CP-C-04: ELIMINAR PRODUCTO DESDE EL INVENTARIO
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-C-04: Eliminar un producto desde el inventario disminuye el contador",
        priority = 4
    )
    public void eliminarProductoDesdeInventario() {
        InventarioPage inventario = iniciarSesionComoUsuarioEstandar();

        inventario.agregarProductoAlCarrito(PRODUCTO_MOCHILA);
        Assert.assertEquals(inventario.obtenerCantidadEnCarrito(), 1,
            "El carrito debia tener 1 producto antes de eliminar.");

        GestorReportes.registrarInformacion("Haciendo clic en Remove");
        inventario.eliminarProductoDelCarrito(PRODUCTO_MOCHILA);

        int cantidadFinal = inventario.obtenerCantidadEnCarrito();
        GestorReportes.registrarInformacion("Contador luego de eliminar: " + cantidadFinal);

        Assert.assertEquals(cantidadFinal, 0,
            "El contador debia volver a 0 despues de eliminar el producto.");

        Assert.assertFalse(inventario.productoEstaEnCarrito(PRODUCTO_MOCHILA),
            "El boton debia volver a 'Add to cart'.");

        GestorReportes.registrarPasoExitoso("Producto eliminado correctamente.");
    }

    // ════════════════════════════════════════════════════════════
    // CP-C-05: CONTINUAR COMPRANDO
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-C-05: Continue Shopping vuelve al inventario conservando el carrito",
        priority = 5
    )
    public void continuarComprandoDesdeElCarrito() {
        InventarioPage inventario = iniciarSesionComoUsuarioEstandar();

        inventario.agregarProductoAlCarrito(PRODUCTO_MOCHILA);
        inventario.irAlCarrito();

        CarritoPage carrito = new CarritoPage();
        Assert.assertTrue(carrito.estaEnPaginaCarrito(), "Debia estar en el carrito.");

        GestorReportes.registrarInformacion("Haciendo clic en Continue Shopping");
        carrito.continuarComprando();

        InventarioPage inventarioNuevo = new InventarioPage();
        Assert.assertTrue(inventarioNuevo.estaEnPaginaInventario(),
            "Debia volver al inventario.");

        Assert.assertEquals(inventarioNuevo.obtenerCantidadEnCarrito(), 1,
            "El carrito debia conservar los productos al volver al inventario.");

        GestorReportes.registrarPasoExitoso("Continue Shopping funciona correctamente.");
    }

    // ════════════════════════════════════════════════════════════
    // METODO DE AYUDA PRIVADO
    // ════════════════════════════════════════════════════════════

    /**
     * Realiza el login como usuario estandar y devuelve el inventario listo.
     * Se llama al inicio de cada test de carrito para no repetir el codigo de login.
     */
    private InventarioPage iniciarSesionComoUsuarioEstandar() {
        GestorReportes.registrarInformacion(
            "Pre-condicion: login como " + configuracion.obtenerUsuarioEstandar()
        );

        LoginPage login = new LoginPage();
        login.iniciarSesion(
            configuracion.obtenerUsuarioEstandar(),
            configuracion.obtenerContrasenaValida()
        );

        InventarioPage inventario = new InventarioPage();
        Assert.assertTrue(inventario.estaEnPaginaInventario(),
            "Debia estar en el inventario despues del login.");

        GestorReportes.registrarInformacion("Login exitoso. En pagina de inventario.");
        return inventario;
    }
}
