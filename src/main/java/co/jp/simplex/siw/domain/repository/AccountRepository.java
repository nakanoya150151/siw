package co.jp.simplex.siw.domain.repository;

import co.jp.simplex.siw.domain.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
