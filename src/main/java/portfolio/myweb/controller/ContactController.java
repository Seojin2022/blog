package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.myweb.dto.ContactRequest;
import portfolio.myweb.service.MailService;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final MailService MailService;

    @PostMapping
    public ResponseEntity<String> sendContact(@RequestBody ContactRequest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            return ResponseEntity.badRequest().body("이름을 입력해주세요.");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("이메일을 입력해주세요.");
        }

        if (!request.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body("올바른 이메일 형식이 아닙니다.");
        }

        if (request.getSubject() == null || request.getSubject().isBlank()) {
            return ResponseEntity.badRequest().body("제목을 입력해주세요.");
        }

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body("내용을 입력해주세요.");
        }

        if (request.getMessage().length() > 2000) {
            return ResponseEntity.badRequest().body("내용은 2000자 이하로 입력해주세요.");
        }

        try {
            MailService.sendContactMail(request);
            return ResponseEntity.ok("문의가 전송되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("메일 전송 실패: " + e.getMessage());
        }
    }
}