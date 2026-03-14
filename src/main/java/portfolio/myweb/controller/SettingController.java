package portfolio.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portfolio.myweb.service.SettingService;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @GetMapping("/settings")
    public ResponseEntity<Map<String, String>> getSettings() {
        return ResponseEntity.ok(settingService.findAllAsMap());
    }
}