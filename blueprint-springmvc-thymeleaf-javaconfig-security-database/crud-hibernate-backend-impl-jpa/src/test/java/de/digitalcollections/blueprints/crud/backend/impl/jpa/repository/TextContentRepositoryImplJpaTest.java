package de.digitalcollections.blueprints.crud.backend.impl.jpa.repository;

import de.digitalcollections.blueprints.crud.backend.api.repository.TextContentRepository;
import de.digitalcollections.blueprints.crud.backend.config.SpringConfigBackendForTest;
import de.digitalcollections.blueprints.crud.backend.impl.jpa.entity.TextContentImplJpa;
import de.digitalcollections.blueprints.crud.model.api.TextContent;
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
public class TextContentRepositoryImplJpaTest {

  @Autowired
  private TextContentRepository repository;

  @Test
  @Rollback(true)
  public void testSaveAndFind() {
    TextContentImplJpa entity = new TextContentImplJpa();
    repository.save(entity);
    Optional result = repository.findById(entity.getId());
    TextContent content = (TextContent) result.get();

    assertThat(content).isNotNull();
    assertThat(content.getId()).isEqualTo(entity.getId());
  }
}
