/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.digitalcollections.blueprints.crud.business.api.service;

import java.io.Serializable;
import java.util.List;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.Errors;

/**
 * Service for User.
 *
 * @author ralf
 * @param <T> domain object
 * @param <ID> unique id
 */
public interface UserService<T extends User, ID extends Serializable> extends UserDetailsService {

    T activate(ID id);

    T create();

    T create(T user, String password1, String password2, Errors results);

    T createAdminUser();

    T deactivate(ID id);

    T get(ID id);

    List<T> getAll();

    T update(T user, String password1, String password2, Errors results);
    
    boolean doesActiveAdminUserExist();
}
