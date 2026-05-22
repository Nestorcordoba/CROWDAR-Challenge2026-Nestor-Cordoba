package com.saucedemo.pruebas;

import com.saucedemo.paginas.LoginPage;
import com.saucedemo.utilidades.GestorNavegador;
import com.saucedemo.utilidades.GestorReportes;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * PRUEBAS DE LOGIN
 * Casos de prueba automatizados para el modulo de inicio de sesion.
 * Cubre: login exitoso, contrasena incorrecta, usuario bloqueado,
 *        campos vacios y un fallo intencional para demostrar la captura.
 */
public class LoginTest extends PruebaBase {

    // ════════════════════════════════════════════════════════════
    // CP-L-01: LOGIN EXITOSO
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-L-01: Login exitoso con usuario estandar y contrasena correcta",
        priority = 1
    )
    public void loginExitosoConUsuarioEstandar() {
        LoginPage paginaLogin = new LoginPage();

        GestorReportes.registrarInformacion("Ingresando credenciales validas");
        paginaLogin.ingresarUsuario(configuracion.obtenerUsuarioEstandar());
        paginaLogin.ingresarContrasena(configuracion.obtenerContrasenaValida());

        GestorReportes.registrarInformacion("Haciendo clic en Login");
        paginaLogin.hacerClicEnLogin();

        GestorReportes.registrarInformacion("Verificando redireccion al inventario");
        Assert.assertTrue(
            paginaLogin.loginFueExitoso(),
            "Se esperaba ser redirigido a /inventory.html pero no ocurrio."
        );

        GestorReportes.registrarPasoExitoso("Login exitoso: usuario redirigido al inventario.");
    }

    // ════════════════════════════════════════════════════════════
    // CP-L-02: CONTRASENA INCORRECTA
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-L-02: Login con contrasena incorrecta muestra mensaje de error",
        priority = 2
    )
    public void loginConContrasenaIncorrecta() {
        LoginPage paginaLogin = new LoginPage();

        GestorReportes.registrarInformacion("Ingresando contrasena incorrecta");
        paginaLogin.iniciarSesion(
            configuracion.obtenerUsuarioEstandar(),
            "contrasena_incorrecta_123"
        );

        Assert.assertTrue(
            paginaLogin.errorEstaVisible(),
            "Se esperaba un mensaje de error visible."
        );

        String textoError = paginaLogin.obtenerTextoError();
        GestorReportes.registrarInformacion("Error recibido: " + textoError);

        Assert.assertTrue(
            textoError.contains("Username and password do not match"),
            "Texto del error incorrecto. Recibido: " + textoError
        );

        GestorReportes.registrarPasoExitoso("Error controlado correctamente: " + textoError);
    }

    // ════════════════════════════════════════════════════════════
    // CP-L-03: USUARIO BLOQUEADO
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-L-03: Login con usuario bloqueado muestra mensaje de bloqueo",
        priority = 3
    )
    public void loginConUsuarioBloqueado() {
        LoginPage paginaLogin = new LoginPage();

        GestorReportes.registrarInformacion(
            "Intentando login con: " + configuracion.obtenerUsuarioBloqueado()
        );
        paginaLogin.iniciarSesion(
            configuracion.obtenerUsuarioBloqueado(),
            configuracion.obtenerContrasenaValida()
        );

        Assert.assertTrue(paginaLogin.errorEstaVisible(), "Se esperaba un error de bloqueo.");

        String textoError = paginaLogin.obtenerTextoError();
        GestorReportes.registrarInformacion("Error recibido: " + textoError);

        Assert.assertTrue(
            textoError.contains("locked out"),
            "El error no indica bloqueo. Recibido: " + textoError
        );

        GestorReportes.registrarPasoExitoso("Usuario bloqueado manejado correctamente.");
    }

    // ════════════════════════════════════════════════════════════
    // CP-L-04: CAMPOS VACIOS
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-L-04: Login con campos vacios muestra validacion de campo requerido",
        priority = 4
    )
    public void loginConCamposVacios() {
        LoginPage paginaLogin = new LoginPage();

        GestorReportes.registrarInformacion("Haciendo clic en Login sin completar campos");
        paginaLogin.hacerClicEnLogin();

        Assert.assertTrue(
            paginaLogin.errorEstaVisible(),
            "Se esperaba error de validacion con campos vacios."
        );

        String textoError = paginaLogin.obtenerTextoError();
        GestorReportes.registrarInformacion("Mensaje de validacion: " + textoError);

        Assert.assertTrue(
            textoError.contains("Username is required"),
            "Se esperaba 'Username is required'. Recibido: " + textoError
        );

        GestorReportes.registrarPasoExitoso("Validacion de campo requerido funciona.");
    }

    // ════════════════════════════════════════════════════════════
    // CP-L-05: FALLO INTENCIONAL (Punto 4 del challenge)
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "CP-L-05 (FALLO INTENCIONAL): Demuestra captura automatica al fallar un test",
        priority = 5
    )
    public void loginFalloIntencional() {
        LoginPage paginaLogin = new LoginPage();

        GestorReportes.registrarInformacion(
            "PRUEBA INTENCIONAL DE FALLO: se verifica un titulo inexistente."
        );

        // Hacemos login exitoso para llegar al inventario
        paginaLogin.iniciarSesion(
            configuracion.obtenerUsuarioEstandar(),
            configuracion.obtenerContrasenaValida()
        );

        String tituloReal = GestorNavegador.obtenerDriver().getTitle();
        GestorReportes.registrarInformacion("Titulo real de la pagina: " + tituloReal);

        // FALLO INTENCIONAL: verificamos un titulo que no existe.
        // Esto dispara el AssertionError, el listener toma la captura
        // y la embebe en el reporte HTML automaticamente.
        Assert.assertEquals(
            tituloReal,
            "Este titulo no existe - FALLO INTENCIONAL",
            "FALLO ESPERADO para demostrar la captura automatica. " +
            "Titulo real: '" + tituloReal + "'"
        );
    }
}
