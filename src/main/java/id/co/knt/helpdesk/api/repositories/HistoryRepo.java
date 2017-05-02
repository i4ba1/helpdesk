package id.co.knt.helpdesk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import id.co.knt.helpdesk.api.model.ActivationHistory;

public interface HistoryRepo extends JpaRepository<ActivationHistory, Long> {
	
}
