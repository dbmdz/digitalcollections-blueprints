package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.UserRepository;
import de.digitalcollections.blueprints.crud.backend.config.SpringConfigBackendForTest;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.TestUserFactory;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.UserImplJpa;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigBackendForTest.class})
@Transactional
public class UserRepositoryImplJpaTest {

  @Autowired
  private UserRepository repository;

  @Test
  @Rollback(true)
  public void testSaveAndFind() {
    UserImplJpa entity = TestUserFactory.build("test@test.org");
    repository.save(entity);
    Optional result = repository.findById(entity.getId());
    User user = (User) result.get();

    assertThat(user).isNotNull();
    assertThat(entity.getId()).isEqualTo(user.getId());
  }

  @Test
  @Rollback(true)
  public void testSaveAndDelete() throws Exception {
    UserImplJpa entity = TestUserFactory.build("test@test.org");
    repository.save(entity);
    Optional result = repository.findById(entity.getId());
    User foundEntity = (User) result.get();
    assertThat(entity.getId()).isEqualTo(foundEntity.getId());

    repository.delete(foundEntity);
    result = repository.findById(entity.getId());
    assertThat(!result.isPresent()).isTrue();
  }

  @Test
  @Rollback(true)
  public void testFindByEmail() throws Exception {
    UserImplJpa entity = TestUserFactory.build("test@test.org");
    repository.save(entity);
    User user = repository.findByEmail("test@test.org");
    assertThat(entity.getId()).isEqualTo(user.getId());
  }
}
