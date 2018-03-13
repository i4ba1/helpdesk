package id.co.knt.helpdesk.api.repositories;

import id.co.knt.helpdesk.api.model.ActivationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepo extends JpaRepository<ActivationHistory, Long> {
	
}
