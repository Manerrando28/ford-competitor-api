package com.ford.competitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ford.competitor.config.JwtUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompetitorApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve realizar login, gerar JWT e informar a expiração")
    void loginComCredenciaisValidas() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@ford.com\",\"password\":\"user123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    @DisplayName("Deve padronizar a resposta para credenciais inválidas")
    void loginComCredenciaisInvalidas() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@ford.com\",\"password\":\"senhaErrada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("invalid_credentials"))
                .andExpect(jsonPath("$.path").value("/api/auth/login"));
    }

    @Test
    @DisplayName("Deve rejeitar acesso a recurso protegido sem JWT com 401")
    void consultaSemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/v1/competitors/{brand}/{model}/{version}/specifications",
                                "Ford", "Ranger", "Raptor")
                        .param("attributes", "Motor"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("unauthorized"));
    }

    @Test
    @DisplayName("Deve rejeitar um JWT malformado com 401 padronizado")
    void consultaComJwtInvalido() throws Exception {
        mockMvc.perform(get("/api/v1/competitors/{brand}/{model}/{version}/specifications",
                                "Ford", "Ranger", "Raptor")
                        .param("attributes", "Motor")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("unauthorized"));
    }

    @Test
    @DisplayName("Deve consultar especificações pelo recurso usando JWT válido")
    void consultaProtegidaComSucesso() throws Exception {
        String token = authenticate("user@ford.com", "user123");

        mockMvc.perform(get("/api/v1/competitors/{brand}/{model}/{version}/specifications",
                                "Ford", "Ranger", "Raptor")
                        .param("attributes", "Motor", "Preco")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("Ford"))
                .andExpect(jsonPath("$.specifications[0].attribute").value("Motor"))
                .andExpect(jsonPath("$.specifications[0].value").value("3.0 V6 Bi-Turbo Gasoline"))
                .andExpect(jsonPath("$.specifications[0].available").value(true))
                .andExpect(jsonPath("$.specifications[1].value").value("Não disponível"))
                .andExpect(jsonPath("$.specifications[1].available").value(false));
    }

    @Test
    @DisplayName("Deve validar o corpo da consulta e devolver erro padronizado")
    void consultaComCorpoInvalido() throws Exception {
        String token = authenticate("user@ford.com", "user123");
        String request = "{\"brand\":\"\",\"model\":\"Ranger\",\"version\":\"Raptor\",\"attributes\":[]}";

        mockMvc.perform(post("/api/v1/competitors/query")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("validation_error"))
                .andExpect(jsonPath("$.fieldErrors.brand").value("A marca é obrigatória"));
    }

    @Test
    @DisplayName("Deve impedir USER de cadastrar uma especificação exclusiva de ADMIN")
    void usuarioComumNaoPodeCadastrarEspecificacao() throws Exception {
        String token = authenticate("user@ford.com", "user123");

        mockMvc.perform(post("/api/v1/vehicle-specifications")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validSpecificationJson()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("forbidden"));
    }

    @Test
    @DisplayName("Deve permitir ADMIN cadastrar recurso e responder 201 com Location")
    void administradorPodeCadastrarEspecificacao() throws Exception {
        String token = authenticate("admin@ford.com", "admin123");

        mockMvc.perform(post("/api/v1/vehicle-specifications")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validSpecificationJson()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/v1/vehicle-specifications/7")))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.attribute").value("Motor"));
    }

    @Test
    @DisplayName("Deve invalidar JWT após a expiração configurada")
    void jwtExpiradoNaoEhValido() throws InterruptedException {
        JwtUtils jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret",
                "9a6e8b12c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1L);
        String token = jwtUtils.generateToken("user@ford.com", "ROLE_USER");

        Thread.sleep(20);

        assertThat(jwtUtils.validateToken(token)).isFalse();
    }

    private String authenticate(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get("token").asText();
    }

    private String validSpecificationJson() {
        return "{\"brand\":\"Toyota\",\"model\":\"Hilux\",\"version\":\"SRX\","
                + "\"attribute\":\"Motor\",\"value\":\"2.8 Turbo Diesel\"}";
    }
}
