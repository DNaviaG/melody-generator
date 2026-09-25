package com.DanielNavia.melody_generator.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Arranca el contexto completo de Spring para la prueba.
@AutoConfigureMockMvc // Configura MockMvc para simular peticiones HTTP.
public class MelodyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Cliente HTTP simulado proporcionado por Spring.

    /**
     * Se prueba este comportamiento porque el endpoint representa la frontera
     * HTTP de la aplicación y debe aceptar una escala válida, generar una melodía
     * y devolver una respuesta correcta con la escala recibida y sus cuatro compases.
     */
    @Test
    void shouldGenerateMelodyFromScaleRequest() throws Exception {
        // JSON que enviaremos en el cuerpo de la petición.
        String requestBody = """
                {
                    "rootNote": "C",
                    "mode": "MAJOR"
                }
                """;
        mockMvc.perform(
                        post("/api/v1/melody/generate") // Simula un POST al endpoint.
                                .contentType(MediaType.APPLICATION_JSON) // Indica que enviamos JSON.
                                .content(requestBody) // Añade el JSON al cuerpo de la petición.
                )
                .andExpect(status().isOk()) // Comprueba que la respuesta es HTTP 200.
                .andExpect(jsonPath("$.scale.rootNote").value("C")) // Comprueba la tónica devuelta.
                .andExpect(jsonPath("$.scale.mode").value("MAJOR")) // Comprueba el modo devuelto.
                .andExpect(jsonPath("$.measures.length()").value(4)); // Comprueba que hay 4 compases.
    }
}