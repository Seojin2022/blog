package portfolio.myweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.myweb.domain.Otp;

public interface OtpRepository extends JpaRepository<Otp, String> {
}