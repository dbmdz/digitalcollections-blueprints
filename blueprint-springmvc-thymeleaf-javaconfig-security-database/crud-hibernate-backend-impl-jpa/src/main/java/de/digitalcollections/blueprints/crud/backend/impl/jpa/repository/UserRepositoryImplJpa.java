package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.UserRepository;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.RoleImplJpa;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.UserImplJpa;
import de.digitalcollections.blueprints.crud.model.api.enums.UserRole;
import de.digitalcollections.blueprints.crud.model.api.security.Role;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImplJpa extends AbstractPagingAndSortingRepositoryImplJpa<UserImplJpa, Long, UserRepositoryJpa>
        implements UserRepository<UserImplJpa, Long> {

  @Override
  public UserImplJpa create() {
    return new UserImplJpa();
  }

  @Override
  public List<UserImplJpa> findActiveAdminUsers() {
    UserImplJpa adminUser = create();
    adminUser.setEnabled(true);
    adminUser.setRoles(Arrays.asList(new RoleImplJpa(Role.PREFIX + UserRole.ADMIN.name())));
    Example<UserImplJpa> example = Example.of(adminUser);

    Iterable result = jpaRepository.findAll(example);
    return (List<UserImplJpa>) result;
  }

  @Override
  public List<UserImplJpa> findAll(Sort sort) {
    return jpaRepository.findAll(sort);
  }

  @Override
  public UserImplJpa findByEmail(String email) {
    return jpaRepository.findByEmail(email);
  }

  @Autowired
  @Override
  void setJpaRepository(UserRepositoryJpa jpaRepository) {
    this.jpaRepository = jpaRepository;
  }
}
