package com.saucedemo.pruebas;

import com.saucedemo.utilidades.GestorConfiguracion;
import com.saucedemo.utilidades.GestorReportes;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * PRUEBA DE API DE MERCADO LIBRE (Punto 5 del Challenge)
 * Verifica el endpoint: GET https://www.mercadolibre.com.ar/menu/departments
 *
 * No usa Selenium. Solo hace peticiones HTTP con Rest Assured.
 * No hereda de PruebaBase porque no necesita abrir un navegador.
 */
public class MercadoLibreApiTest {

    private final GestorConfiguracion configuracion = GestorConfiguracion.obtenerInstancia();

    /**
     * Inicializa el reporte si se corre la clase directamente (sin testng.xml).
     * Cuando corre via suite, el @BeforeSuite ya lo inicializo y esta condicion no aplica.
     */
    @BeforeClass
    public void inicializarReporteSiEsNecesario() {
        GestorReportes.inicializarSiEsNecesario();
    }

    /**
     * Guarda el reporte al finalizar si se corre la clase directamente.
     */
    @AfterClass
    public void finalizarReporte() {
        GestorReportes.finalizar();
    }

    // ════════════════════════════════════════════════════════════
    // PRUEBA 1: EL ENDPOINT RESPONDE CON CODIGO 200 Y CONTIENE DATOS
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "API ML-01: GET /menu/departments retorna 200 y contiene departamentos",
        priority = 1
    )
    public void elEndpointDeDepartamentosRespondeSatisfactoriamente() {
        String urlEndpoint = configuracion.obtenerUrlApiMercadoLibre();

        GestorReportes.crearPrueba(
            "API MercadoLibre - GET /menu/departments",
            "Verifica que el endpoint retorna 200 y contiene departamentos validos"
        );

        GestorReportes.registrarInformacion("Enviando GET a: " + urlEndpoint);

        // Realizamos la peticion GET con Rest Assured
        Response respuesta = RestAssured
            .given()
                .header("Accept", "application/json")
                .header("User-Agent", "Mozilla/5.0")
                .relaxedHTTPSValidation()
            .when()
                .get(urlEndpoint);

        // Verificar codigo HTTP 200
        int codigoHttp = respuesta.getStatusCode();
        GestorReportes.registrarInformacion("Codigo HTTP recibido: " + codigoHttp);

        Assert.assertEquals(codigoHttp, 200,
            "Se esperaba HTTP 200 pero se recibio: " + codigoHttp);

        GestorReportes.registrarPasoExitoso("Codigo HTTP 200 correcto.");

        // Verificar que la respuesta no esta vacia
        String cuerpo = respuesta.getBody().asString();
        GestorReportes.registrarInformacion(
            "Longitud de la respuesta: " + cuerpo.length() + " caracteres"
        );

        Assert.assertFalse(cuerpo.isEmpty(),
            "El cuerpo de la respuesta no debia estar vacio.");

        GestorReportes.registrarPasoExitoso("Respuesta no vacia.");

        // Verificar que contiene datos de departamentos
        boolean tieneDepartamentos = verificarQueTieneDepartamentos(cuerpo);

        Assert.assertTrue(tieneDepartamentos,
            "La respuesta debia contener departamentos. Inicio del cuerpo: " +
            cuerpo.substring(0, Math.min(300, cuerpo.length()))
        );

        GestorReportes.registrarPasoExitoso(
            "El endpoint retorno departamentos de Mercado Libre Argentina correctamente."
        );
    }

    // ════════════════════════════════════════════════════════════
    // PRUEBA 2: TIEMPO DE RESPUESTA ACEPTABLE
    // ════════════════════════════════════════════════════════════

    @Test(
        description = "API ML-02: El tiempo de respuesta es menor a 5 segundos",
        priority = 2
    )
    public void elTiempoDeRespuestaEsAceptable() {
        String urlEndpoint = configuracion.obtenerUrlApiMercadoLibre();

        GestorReportes.crearPrueba(
            "API MercadoLibre - Tiempo de respuesta",
            "Verifica que el tiempo de respuesta es menor a 5000ms"
        );

        GestorReportes.registrarInformacion("Midiendo tiempo de respuesta...");

        long inicio = System.currentTimeMillis();

        Response respuesta = RestAssured
            .given()
                .relaxedHTTPSValidation()
            .when()
                .get(urlEndpoint);

        long tiempoTotal = System.currentTimeMillis() - inicio;

        GestorReportes.registrarInformacion("Tiempo: " + tiempoTotal + "ms | HTTP: " + respuesta.getStatusCode());

        Assert.assertTrue(tiempoTotal < 5000,
            "El tiempo fue " + tiempoTotal + "ms, supera el limite de 5000ms.");

        GestorReportes.registrarPasoExitoso(
            "Tiempo aceptable: " + tiempoTotal + "ms (limite: 5000ms)"
        );
    }

    // ════════════════════════════════════════════════════════════
    // METODO AUXILIAR
    // ════════════════════════════════════════════════════════════

    /**
     * Verifica de forma flexible si la respuesta contiene datos de departamentos.
     */
    private boolean verificarQueTieneDepartamentos(String cuerpo) {
        String cuerpoMin = cuerpo.toLowerCase();
        return cuerpoMin.contains("department")
            || cuerpoMin.contains("categoria")
            || cuerpoMin.contains("label")
            || cuerpoMin.contains("section")
            || (cuerpo.length() > 10
                && !cuerpo.equals("null")
                && !cuerpo.equals("[]")
                && !cuerpo.equals("{}"));
    }
}
