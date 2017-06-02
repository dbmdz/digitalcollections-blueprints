package de.digitalcollections.blueprints.crud.backend.impl.database.migration;

import de.digitalcollections.blueprints.crud.model.api.enums.UserRole;
import de.digitalcollections.blueprints.crud.model.api.security.Role;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V1_0_4__DML_Create_WMS_Roles implements SpringJdbcMigration {

  @Override
  public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
    UserRole[] values = UserRole.values();
    int i = 0;
    for (UserRole value : values) {
      jdbcTemplate.update("INSERT into roles (id, name) values (?,?)", ++i, Role.PREFIX + value);
    }
  }
}
