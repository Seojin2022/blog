package portfolio.myweb.service;

import portfolio.myweb.domain.Setting;
import portfolio.myweb.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;

    public Map<String, String> findAllAsMap() {
        List<Setting> list = settingRepository.findAllByOrderByKeyAsc();
        return list.stream()
                .collect(Collectors.toMap(Setting::getKey, Setting::getValue));
    }

    @Transactional
    public void saveAll(Map<String, String> settings) {
        if (settings == null || settings.isEmpty()) return;
        for (Map.Entry<String, String> e : settings.entrySet()) {
            Setting s = settingRepository.findById(e.getKey())
                    .orElse(new Setting(e.getKey(), ""));
            s.setValue(e.getValue() != null ? e.getValue() : "");
            settingRepository.save(s);
        }
    }
}