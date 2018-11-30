package de.digitalcollections.blueprints.crud.business.impl.service;

import de.digitalcollections.blueprints.crud.backend.api.repository.UserRepository;
import de.digitalcollections.blueprints.crud.business.api.service.UserService;
import de.digitalcollections.blueprints.crud.config.SpringConfigBackendForTest;
import de.digitalcollections.blueprints.crud.config.SpringConfigBusiness;
import de.digitalcollections.blueprints.crud.model.api.security.User;
import de.digitalcollections.blueprints.crud.model.impl.security.UserImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfigBusiness.class, SpringConfigBackendForTest.class})
public class UserServiceTest {

    private User user;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService service;

    @BeforeEach
    public void setup() {
        user = new UserImpl();
        user.setEmail("foo@spar.org");
        user.setPasswordHash("foobar");
        Mockito.when(userRepository.findByEmail("foo@spar.org")).thenReturn(user);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(userRepository);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        UserDetails retrieved = service.loadUserByUsername("foo@spar.org");
        assertThat(user.getEmail()).isEqualTo(retrieved.getUsername());
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findByEmail("foo@spar.org");
    }

    @Test
    public void testGetPasswordHash() throws Exception {
        PasswordEncoder pwEncoder = new BCryptPasswordEncoder();
        assertThat(pwEncoder.matches("foobar", user.getPasswordHash())).isTrue();
    }
}
