package com.clinica.recall.service;

import com.clinica.recall.domain.enums.RolePerfil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Value("${resend.from-name}")
    private String fromName;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void enviarConvite(String email, String nome, String nomeClinica, String token, RolePerfil perfil) {
        String linkAtivacao = frontendUrl + "/ativar-conta?token=" + token;
        String perfilLabel = perfil == RolePerfil.MEDICO ? "Médico" : "Secretária";

        String html = """
                <div style="font-family: Arial, sans-serif; max-width: 480px; margin: 0 auto;">
                    <h2 style="color: #4F525A;">Bem-vindo(a) ao Recall</h2>
                    <p style="color: #4F525A;">Olá %s,</p>
                    <p style="color: #4F525A;">Você foi convidado(a) por <strong>%s</strong> para acessar o sistema Recall como <strong>%s</strong>.</p>
                    <p style="margin: 24px 0;">
                        <a href="%s" style="background-color: #14B7C1; color: white; padding: 12px 24px; border-radius: 8px; text-decoration: none; font-weight: bold;">
                            Ativar minha conta
                        </a>
                    </p>
                    <p style="color: #9CA3AF; font-size: 13px;">Este link expira em 7 dias.</p>
                </div>
                """.formatted(nome, nomeClinica, perfilLabel, linkAtivacao);

        Map<String, Object> body = new HashMap<>();
        body.put("from", fromName + " <" + fromEmail + ">");
        body.put("to", new String[]{email});
        body.put("subject", "Você foi convidado para o Recall");
        body.put("html", html);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            RequestEntity<String> request = RequestEntity
                    .method(HttpMethod.POST, URI.create("https://api.resend.com/emails"))
                    .headers(headers)
                    .body(objectMapper.writeValueAsString(body));

            restTemplate.exchange(request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail de convite: " + e.getMessage());
        }
    }
}