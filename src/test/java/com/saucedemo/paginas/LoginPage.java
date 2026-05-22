package com.saucedemo.paginas;

import org.openqa.selenium.By;

/**
 * LOGIN PAGE
 * Representa la pantalla de inicio de sesion de SauceDemo.
 * Encapsula los localizadores y acciones de esa pagina (Page Object Model).
 */
public class LoginPage extends PaginaBase {

    // ── Localizadores ────────────────────────────────────────────────
    private static final By CAMPO_USUARIO    = By.id("user-name");
    private static final By CAMPO_CONTRASENA = By.id("password");
    private static final By BOTON_LOGIN      = By.id("login-button");
    private static final By MENSAJE_ERROR    = By.cssSelector("[data-test='error']");

    // ── Acciones ─────────────────────────────────────────────────────

    public void ingresarUsuario(String usuario) {
        escribirEn(CAMPO_USUARIO, usuario);
    }

    public void ingresarContrasena(String contrasena) {
        escribirEn(CAMPO_CONTRASENA, contrasena);
    }

    public void hacerClicEnLogin() {
        hacerClicEn(BOTON_LOGIN);
    }

    /** Metodo conveniente: completa el formulario y hace clic en Login */
    public void iniciarSesion(String usuario, String contrasena) {
        ingresarUsuario(usuario);
        ingresarContrasena(contrasena);
        hacerClicEnLogin();
    }

    /** Devuelve true si el banner de error rojo es visible */
    public boolean errorEstaVisible() {
        return elementoEstaPresente(MENSAJE_ERROR);
    }

    /** Devuelve el texto del mensaje de error */
    public String obtenerTextoError() {
        return obtenerTexto(MENSAJE_ERROR);
    }

    /** Devuelve true si el login fue exitoso (URL cambio a /inventory.html) */
    public boolean loginFueExitoso() {
        try {
            esperarUrlContiene("inventory.html");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
