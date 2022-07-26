package io.jomatt.multitenant.sample.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> getByName(String name) {
        return repository.findById(name);
    }

    public Iterable<User> getAll() {
        return repository.findAll();
    }

}
