package de.digitalcollections.blueprints.crud.backend.config;

import de.digitalcollections.blueprints.crud.config.SpringConfigBackendDatabase;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class SpringConfigBackendDatabaseForTest extends SpringConfigBackendDatabase {

  @Bean
  @Override
  public DataSource dataSource() {
    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    builder.setType(EmbeddedDatabaseType.H2);
    return builder.build();
  }
}
