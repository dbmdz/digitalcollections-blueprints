package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.UserImplJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * To execute Querydsl predicates we simply let our repository extend QueryDslPredicateExecutor.
 */
public interface UserRepositoryJpa extends JpaRepository<UserImplJpa, Long>, QuerydslPredicateExecutor {

  public UserImplJpa findByEmail(String email);

}
