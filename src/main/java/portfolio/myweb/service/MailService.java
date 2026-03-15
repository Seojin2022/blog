package portfolio.myweb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import portfolio.myweb.dto.ContactRequest;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${app.contact.to}")
    private String contactTo;

    @Value("${app.contact.from}")
    private String fromEmail;

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://api.resend.com")
            .build();

    public void sendContactMail(ContactRequest request) {
        String textBody =
                "이름: " + request.getName() + "\n" +
                        "이메일: " + request.getEmail() + "\n\n" +
                        "내용:\n" + request.getMessage();

        Map<String, Object> payload = Map.of(
                "from", fromEmail,
                "to", new String[]{contactTo},
                "subject", "[Portfolio Contact] " + request.getSubject(),
                "text", textBody,
                "replyTo", request.getEmail()
        );

        restClient.post()
                .uri("/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + resendApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }
}