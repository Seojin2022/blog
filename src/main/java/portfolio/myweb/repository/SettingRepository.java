package portfolio.myweb.repository;

import portfolio.myweb.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {

    List<Setting> findAllByOrderByKeyAsc();
}