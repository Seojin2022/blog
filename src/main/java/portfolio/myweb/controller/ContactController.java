package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import portfolio.myweb.domain.Otp;
import portfolio.myweb.repository.OtpRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final OtpRepository otpRepository;

    // 이메일 OTP 발송
    @PostMapping("/verify-email")
    public String sendOtp(@RequestBody Map<String, String> body) {

        String email = body.get("email");

        String code = String.valueOf(100000 + new Random().nextInt(900000));

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        otpRepository.save(otp);

        System.out.println("OTP CODE: " + code);

        return "OTP 전송 완료 (콘솔 확인)";
    }

    // OTP 검증
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        String code = body.get("otp");

        Otp otp = otpRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("OTP 없음"));

        if (!otp.getCode().equals(code)) {
            throw new RuntimeException("OTP 틀림");
        }

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP 만료");
        }

        return "인증 성공";
    }
}