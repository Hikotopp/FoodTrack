package com.foodtrack.spring.springboot_application.infrastructure.persistence.mapper;

import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import com.foodtrack.spring.springboot_application.infrastructure.persistence.entity.AppUserEntity;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {

    public AppUser toDomain(AppUserEntity entity) {
        return new AppUser(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPasswordHash(),
                UserRole.valueOf(entity.getRole())
        );
    }

    public AppUserEntity toEntity(AppUser user) {
        AppUserEntity entity = new AppUserEntity();
        entity.setId(user.id());
        entity.setFullName(user.fullName());
        entity.setEmail(user.email());
        entity.setPasswordHash(user.passwordHash());
        entity.setRole(user.role().name());
        return entity;
    }
}
