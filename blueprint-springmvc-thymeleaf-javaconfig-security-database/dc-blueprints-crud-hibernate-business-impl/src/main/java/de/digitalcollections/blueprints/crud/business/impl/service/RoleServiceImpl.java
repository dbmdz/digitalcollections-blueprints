package de.digitalcollections.blueprints.crud.business.impl.service;

import de.digitalcollections.blueprints.crud.backend.api.repository.RoleRepository;
import de.digitalcollections.blueprints.crud.business.api.service.RoleService;
import de.digitalcollections.blueprints.crud.model.api.enums.UserRole;
import de.digitalcollections.blueprints.crud.model.api.security.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService<Role, Long> {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role create() {
    return roleRepository.create();
  }

  @Override
  public Role getAdminRole() {
    return roleRepository.findByName(Role.PREFIX + UserRole.ADMIN);
  }

  @Override
  public Role get(Long id) {
    return (Role) roleRepository.findOne(id);
  }

  @Override
  public List<Role> getAll() {
    return roleRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
  }

}
