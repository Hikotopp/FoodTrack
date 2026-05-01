package com.foodtrack.spring.springboot_application.config;

import com.foodtrack.spring.springboot_application.application.port.out.MenuItemRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.RestaurantTableRepositoryPort;
import com.foodtrack.spring.springboot_application.application.port.out.UserRepositoryPort;
import com.foodtrack.spring.springboot_application.domain.model.AppUser;
import com.foodtrack.spring.springboot_application.domain.model.MenuCategory;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import com.foodtrack.spring.springboot_application.domain.model.RestaurantTable;
import com.foodtrack.spring.springboot_application.domain.model.TableStatus;
import com.foodtrack.spring.springboot_application.domain.model.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Configuration
public class DataSeeder {

    @Bean
    ApplicationRunner seedInitialData(
            UserRepositoryPort userRepositoryPort,
            MenuItemRepositoryPort menuItemRepositoryPort,
            RestaurantTableRepositoryPort restaurantTableRepositoryPort,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin-name}") String adminName,
            @Value("${app.bootstrap.admin-email}") String adminEmail,
            @Value("${app.bootstrap.admin-password}") String adminPassword
    ) {
        return args -> {
            seedAdminUser(userRepositoryPort, passwordEncoder, adminName, adminEmail, adminPassword);
            seedMenu(menuItemRepositoryPort);
            seedTables(restaurantTableRepositoryPort);
        };
    }

    private void seedAdminUser(
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder,
            String adminName,
            String adminEmail,
            String adminPassword
    ) {
        if (userRepositoryPort.count() > 0) {
            return;
        }

        userRepositoryPort.save(new AppUser(
                null,
                adminName,
                adminEmail.toLowerCase(Locale.ROOT),
                passwordEncoder.encode(adminPassword),
                UserRole.ADMIN
        ));
    }

    private void seedMenu(MenuItemRepositoryPort menuItemRepositoryPort) {
        if (menuItemRepositoryPort.count() > 0) {
            return;
        }

        List<MenuItem> menuItems = List.of(
                new MenuItem(null, "Tartar de Atun Fresh Track", "Atun fresco en cubos, aguacate, aceite de sesamo, soya y crocante de wonton.", MenuCategory.APPETIZER, new BigDecimal("42000"), true),
                new MenuItem(null, "Carpaccio de Res al Parmesano", "Laminas de res, rugula, queso parmesano, aceite de oliva extra virgen y reduccion balsamica.", MenuCategory.APPETIZER, new BigDecimal("39000"), true),
                new MenuItem(null, "Ceviche Caribe FoodTrack", "Pescado blanco y camarones marinados en limon, leche de tigre, mango y cilantro.", MenuCategory.APPETIZER, new BigDecimal("44000"), true),
                new MenuItem(null, "Crema de Tomate Asado", "Tomates rostizados, albahaca fresca y toque de crema.", MenuCategory.SOUP, new BigDecimal("28000"), true),
                new MenuItem(null, "Ajiaco FoodTrack Signature", "Pollo, papa criolla, mazorca, guascas, crema de leche y alcaparras.", MenuCategory.SOUP, new BigDecimal("36000"), true),
                new MenuItem(null, "Lomo de Res en Reduccion de Vino Tinto", "Corte premium, pure rustico y vegetales salteados.", MenuCategory.MAIN_COURSE, new BigDecimal("68000"), true),
                new MenuItem(null, "Salmon en Costra de Hierbas", "Acompanado de arroz de coco cremoso y esparragos grillados.", MenuCategory.MAIN_COURSE, new BigDecimal("64000"), true),
                new MenuItem(null, "Pechuga Rellena FoodTrack", "Pollo relleno de espinaca y queso, con salsa blanca y papas rusticas.", MenuCategory.MAIN_COURSE, new BigDecimal("52000"), true),
                new MenuItem(null, "Pasta Artesanal con Camarones", "Pasta fresca en salsa cremosa de ajo, parmesano y camarones salteados.", MenuCategory.MAIN_COURSE, new BigDecimal("58000"), true),
                new MenuItem(null, "Risotto de Champinones y Trufa", "Arroz arborio cremoso con champinones frescos y aceite de trufa.", MenuCategory.MAIN_COURSE, new BigDecimal("54000"), true),
                new MenuItem(null, "Ensalada Cesar Premium", "Pollo grillado, lechuga romana, parmesano y crutones artesanales.", MenuCategory.SALAD, new BigDecimal("35000"), true),
                new MenuItem(null, "Ensalada Tropical Gourmet", "Mix de lechugas, mango, fresas, nueces y vinagreta de maracuya.", MenuCategory.SALAD, new BigDecimal("33000"), true),
                new MenuItem(null, "Volcan de Chocolate FoodTrack", "Bizcocho caliente con centro liquido y helado de vainilla.", MenuCategory.DESSERT, new BigDecimal("24000"), true),
                new MenuItem(null, "Cheesecake de Maracuya", "Cremoso con base crocante y topping acido dulce.", MenuCategory.DESSERT, new BigDecimal("21000"), true),
                new MenuItem(null, "Brownie con Helado Artesanal", "Brownie tibio con helado y salsa de chocolate.", MenuCategory.DESSERT, new BigDecimal("22000"), true),
                new MenuItem(null, "Limonada de Coco Premium", "Bebida fresca y cremosa de coco.", MenuCategory.DRINK, new BigDecimal("15000"), true),
                new MenuItem(null, "Jugos Naturales", "Sabores disponibles: mora, mango y maracuya.", MenuCategory.DRINK, new BigDecimal("12000"), true),
                new MenuItem(null, "Copa de Vino", "Vino tinto o blanco por copa.", MenuCategory.DRINK, new BigDecimal("20000"), true),
                new MenuItem(null, "Cocteles de Autor FoodTrack", "Cocteles de la casa desde 28000.", MenuCategory.DRINK, new BigDecimal("28000"), true),
                new MenuItem(null, "Agua o Gaseosa", "Agua o gaseosa individual.", MenuCategory.DRINK, new BigDecimal("7000"), true)
        );

        for (MenuItem menuItem : menuItems) {
            menuItemRepositoryPort.save(menuItem);
        }
    }

    private void seedTables(RestaurantTableRepositoryPort restaurantTableRepositoryPort) {
        if (restaurantTableRepositoryPort.count() > 0) {
            return;
        }

        for (int tableNumber = 1; tableNumber <= 8; tableNumber++) {
            restaurantTableRepositoryPort.save(new RestaurantTable(null, tableNumber, TableStatus.AVAILABLE));
        }
    }
}
