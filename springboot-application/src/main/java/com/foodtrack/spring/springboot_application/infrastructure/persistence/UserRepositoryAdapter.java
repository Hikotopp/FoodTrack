package com.foodtrack.spring.springboot_application.infrastructure.persistence;

import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper.AppUserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final AppUserMapper appUserMapper;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, AppUserMapper appUserMapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    public List<AppUser> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(appUserMapper::toDomain)
                .toList();
    }

    @Override
    @SuppressWarnings("null")
    public Optional<AppUser> findById(Long id) {
        return jpaUserRepository.findById(id).map(appUserMapper::toDomain);
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(appUserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    @SuppressWarnings("null")
    public AppUser save(AppUser user) {
        return appUserMapper.toDomain(jpaUserRepository.save(appUserMapper.toEntity(user)));
    }

    @Override
    public long count() {
        return jpaUserRepository.count();
    }
}
