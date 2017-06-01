package de.digitalcollections.blueprints.crud.frontend.webapp.propertyeditor;

import de.digitalcollections.blueprints.crud.business.api.service.RoleService;
import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleEditor extends PropertyEditorSupport {

  @Autowired
  RoleService roleService;

  @Override
  public void setAsText(String text) {
    long id = Long.parseLong(text);
    setValue(roleService.get(id));
  }
}
