package br.cwust.billscontrol.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.cwust.billscontrol.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	@Transactional(readOnly = true)
	User findByEmail(String email);

}
