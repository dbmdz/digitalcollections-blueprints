package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.TextContentImplJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * To execute Querydsl predicates we simply let our repository extend QueryDslPredicateExecutor.
 */
public interface TextContentRepositoryJpa extends JpaRepository<TextContentImplJpa, Long>, QueryDslPredicateExecutor {

}
