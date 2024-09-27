package ru.itmentor.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoJPAImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    private final PasswordEncoder encoder;

    @Autowired
    public UserDaoJPAImpl(PasswordEncoder encoder) {
        this.encoder = encoder;
    }


    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT user FROM User user", User.class).getResultList();
    }


    private List<Role> roleManagement(User user){
        List<Role> roles = new ArrayList<>(5);

        for (Role role : user.getRoles()){
            role.setRole(role.getRole().toUpperCase());

            List<Role> dataBaseRole = entityManager.createQuery("SELECT role FROM Role role where role.role=:role", Role.class).
                    setParameter("role",role.getRole()).getResultList();

            if (dataBaseRole.size() == 1) {
                roles.add(dataBaseRole.get(0));
            } else {
                roles.add(role);
            }
        }
        return roles;
    }


    @Override
    public void createUser(User user) {
        try {
            User ifUserExistDoNothing = getUserByEmail(user.getEmail());
        } catch (NoResultException ignore){
            user.setRoles(roleManagement(user));
            user.setPassword(encoder.encode(user.getPassword()));
            entityManager.persist(user);
        }
    }

    @Override
    public User getUserById(long id) {
        return entityManager.createQuery("SELECT user FROM User user where user.id=:id", User.class).
                setParameter("id",id).getSingleResult();
    }

    @Override
    public User getUserByEmail(String email) {
        return entityManager.createQuery("SELECT user FROM User user where user.email=:email", User.class).
                setParameter("email",email).getSingleResult();
    }

    @Override
    public void updateUser(User user) {
        user.setRoles(roleManagement(user));
        user.setPassword(encoder.encode(user.getPassword()));
        entityManager.merge(user);
    }

    @Override
    public void deleteUserById(long id) {
        entityManager.createQuery("delete from User user where user.id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
