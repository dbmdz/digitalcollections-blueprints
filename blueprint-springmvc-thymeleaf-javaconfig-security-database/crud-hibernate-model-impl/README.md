Security Model
==============

see http://stackoverflow.com/questions/19525380/difference-between-role-and-grantedauthority-in-spring-security:

"It also took a while for me to understand what GrantedAuthority and roles are and how they are connected.

Think of a GrantedAuthority as being a "permission" or a "right". Those "permissions" are (normally) expressed as strings (with the getAuthority() method). Those strings let you identify the permissions and let your voters decide if they grant access to something.

You can grant different GrantedAuthoritys (permissions) to users by putting them into the security context. You normally do that by implementing your own UserDetailsService that returns a UserDetails implementation that returns the needed GrantedAuthorities.

Roles (as they are used in many examples) are just "permissions" with a naming convention that says that a role is a GrantedAuthority that starts with the prefix ROLE_. There's nothing more. A role is just a GrantedAuthority - a "permission" - a "right". You see a lot of places in spring security where the role with its ROLE_ prefix is handled specially as e.g. in the RoleVoter, where the ROLE_ prefix is used as a default.

To see that a role and an authority are the same, have a look at the implementation of the hasAuthority() method in SecurityExpressionRoot - which simply calls hasRole(). These expressions are used in annotations like @PreAuthorize("hasRole('XYZ')"). Using hasAuthority instead of hasRole makes the purpose of those annotations much more understandable if dealing with authorities which are not roles like in @PreAuthorize("hasAuthority('OP_DELETE_ACCOUNT')") public void deleteAccount(...) {...}

Regarding your use case:

    Users have roles and roles can perform certain operations.

You could end up in GrantedAuthorities for the roles a user belongs to and the operations a role can perform. The GrantedAuthorities for the roles have the prefix ROLE_ and the operations have the prefix OP_. An example for operation authorities could be OP_DELETE_ACCOUNT, OP_CREATE_USER, OP_RUN_BATCH_JOBetc. Roles can be ROLE_ADMIN, ROLE_USER etc.

You could end up having your entities implement GrantedAuthority like in this (pseudo-code) example:

+---+
@Entity
class Role implements GrantedAuthority {
    @Id
    private String id;

    @OneToMany
    private final List<Operation> allowedOperations = new ArrayList<>();

    @Override
    public String getAuthority() {
        return id;
    }

    public Collection<GrantedAuthority> getAllowedOperations() {
        return allowedOperations;
    }
}

@Entity
class User {
    @Id
    private String id;

    @OneToMany
    private final List<Role> roles = new ArrayList<>();

    public Collection<Role> getRoles() {
        return roles;
    }
}

@Entity
class Operation implements GrantedAuthority {
    @Id
    private String id;

    @Override
    public String getAuthority() {
        return id;
    }
}
+---+

The ids of the roles and operations you create in your database would be the GrantedAuthority representation, e.g. "ROLE_ADMIN", "OP_DELETE_ACCOUNT" etc. When a user is authenticated, make sure that all GrantedAuthorities of all its roles and the corresponding operations are returned from the UserDetails.getAuthorities() method.

Example: The admin role with id ROLE_ADMIN has the operations OP_DELETE_ACCOUNT, OP_READ_ACCOUNT, OP_RUN_BATCH_JOB assigned to it. The user role with id ROLE_USER has the operation OP_READ_ACCOUNT.

If an admin logs in the resulting security context will have the GrantedAuthorities: ROLE_ADMIN, OP_DELETE_ACCOUNT, OP_READ_ACCOUNT, OP_RUN_BATCH_JOB

If a user logs it, it will have: ROLE_USER, OP_READ_ACCOUNT

The UserDetailsService would take care to collect all roles and all operations of those roles and make them available by the method getAuthorities() in the returned UserDetails instance."
