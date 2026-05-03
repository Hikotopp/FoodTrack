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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Configuration
public class DataSeeder {

    @Bean
    @ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
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
        List<MenuItem> activeItems = menuItemRepositoryPort.findAllActive();
        boolean saborExpressMenuExists = activeItems.stream()
                .anyMatch(item -> "Hamburguesa Sabor Express".equals(item.name()));
        if (saborExpressMenuExists) {
            return;
        }

        List<MenuItem> menuItems = List.of(
                menuItem("Papas a la francesa pequena", "Porcion pequena de papas a la francesa.", MenuCategory.APPETIZER, "5000"),
                menuItem("Papas a la francesa mediana", "Porcion mediana de papas a la francesa.", MenuCategory.APPETIZER, "7000"),
                menuItem("Papas a la francesa grande", "Porcion grande de papas a la francesa.", MenuCategory.APPETIZER, "9000"),
                menuItem("Papas con queso y tocineta", "Papas a la francesa con queso fundido y tocineta.", MenuCategory.APPETIZER, "11000"),
                menuItem("Aros de cebolla", "Aros de cebolla crocantes.", MenuCategory.APPETIZER, "8000"),
                menuItem("Nuggets de pollo", "8 unidades de nuggets de pollo.", MenuCategory.APPETIZER, "10000"),
                menuItem("Dedos de queso", "6 unidades de dedos de queso.", MenuCategory.APPETIZER, "11000"),
                menuItem("Alitas BBQ", "6 unidades de alitas con salsa BBQ.", MenuCategory.APPETIZER, "12000"),
                menuItem("Alitas picantes", "6 unidades de alitas con salsa picante.", MenuCategory.APPETIZER, "12000"),
                menuItem("Hamburguesa clasica", "Carne, queso, vegetales y salsas de la casa.", MenuCategory.BURGER, "12000"),
                menuItem("Hamburguesa doble carne", "Doble carne, queso, vegetales y salsas de la casa.", MenuCategory.BURGER, "16000"),
                menuItem("Hamburguesa BBQ", "Carne, queso, tocineta y salsa BBQ.", MenuCategory.BURGER, "15000"),
                menuItem("Hamburguesa pollo crispy", "Pollo crispy, queso, vegetales y salsas de la casa.", MenuCategory.BURGER, "13000"),
                menuItem("Hamburguesa vegetariana", "Opcion vegetariana con vegetales y salsas de la casa.", MenuCategory.BURGER, "13000"),
                menuItem("Hamburguesa Sabor Express", "Especial de la casa: doble carne, doble queso, tocineta, cebolla caramelizada y salsa especial.", MenuCategory.BURGER, "18000"),
                menuItem("Perro sencillo", "Perro caliente con salchicha, ripio y salsas.", MenuCategory.HOT_DOG, "8000"),
                menuItem("Perro especial", "Perro caliente con queso, ripio y salsas.", MenuCategory.HOT_DOG, "11000"),
                menuItem("Super perro", "Perro caliente grande con queso, tocineta, ripio y salsas.", MenuCategory.HOT_DOG, "14000"),
                menuItem("Perro Explosivo", "Especial de la casa: doble salchicha, queso, tocineta, maiz, ripio y full salsas.", MenuCategory.HOT_DOG, "16000"),
                menuItem("Burrito de pollo", "Burrito relleno de pollo, vegetales y salsas.", MenuCategory.OTHER, "13000"),
                menuItem("Burrito de carne", "Burrito relleno de carne, vegetales y salsas.", MenuCategory.OTHER, "13000"),
                menuItem("Wrap de pollo", "Wrap de pollo con vegetales y salsas.", MenuCategory.OTHER, "12000"),
                menuItem("Salchipapas", "Papas a la francesa con salchicha y salsas.", MenuCategory.OTHER, "12000"),
                menuItem("Salchipapa Monster", "Papas, doble salchicha, pollo, carne, queso, tocineta y salsas.", MenuCategory.OTHER, "18000"),
                menuItem("Sandwich mixto", "Sandwich de jamon y queso.", MenuCategory.OTHER, "8000"),
                menuItem("Gaseosa personal", "Gaseosa personal.", MenuCategory.DRINK, "4000"),
                menuItem("Gaseosa 400ml", "Gaseosa de 400ml.", MenuCategory.DRINK, "5000"),
                menuItem("Gaseosa 1.5L", "Gaseosa de 1.5 litros.", MenuCategory.DRINK, "8000"),
                menuItem("Jugo natural", "Jugo natural de la casa.", MenuCategory.DRINK, "6000"),
                menuItem("Limonada natural", "Limonada natural.", MenuCategory.DRINK, "7000"),
                menuItem("Limonada de coco", "Limonada de coco.", MenuCategory.DRINK, "7000"),
                menuItem("Te frio", "Te frio.", MenuCategory.DRINK, "5000"),
                menuItem("Agua", "Agua.", MenuCategory.DRINK, "3000"),
                menuItem("Malteada", "Malteada de la casa.", MenuCategory.DRINK, "9000"),
                menuItem("Helado", "Porcion de helado.", MenuCategory.DESSERT, "4000"),
                menuItem("Brownie con helado", "Brownie con helado.", MenuCategory.DESSERT, "8000"),
                menuItem("Sundae", "Sundae.", MenuCategory.DESSERT, "6000"),
                menuItem("Combo clasico", "Incluye hamburguesa clasica, papas y gaseosa 400ml.", MenuCategory.COMBO, "18000"),
                menuItem("Combo doble carne", "Incluye hamburguesa doble carne, papas y gaseosa 400ml.", MenuCategory.COMBO, "22000"),
                menuItem("Combo BBQ", "Incluye hamburguesa BBQ, papas y gaseosa 400ml.", MenuCategory.COMBO, "21000"),
                menuItem("Combo pollo crispy", "Incluye hamburguesa pollo crispy, papas y gaseosa 400ml.", MenuCategory.COMBO, "19000"),
                menuItem("Combo perro especial", "Incluye perro especial, papas y gaseosa 400ml.", MenuCategory.COMBO, "17000"),
                menuItem("Combo nuggets", "Incluye nuggets, papas y gaseosa 400ml.", MenuCategory.COMBO, "16000"),
                menuItem("Combo Sabor Express", "Top ventas: hamburguesa especial, papas grandes y gaseosa.", MenuCategory.COMBO, "26000"),
                menuItem("Queso extra", "Adicion de queso extra.", MenuCategory.ADDITIONAL, "2000"),
                menuItem("Tocineta", "Adicion de tocineta.", MenuCategory.ADDITIONAL, "3000"),
                menuItem("Carne adicional", "Adicion de carne.", MenuCategory.ADDITIONAL, "4000"),
                menuItem("Salsas extra", "Salsas disponibles: ajo, pina, BBQ, rosada y picante.", MenuCategory.ADDITIONAL, "1000"),
                menuItem("Promo 2x1 perros sencillos", "Martes y jueves: dos perros sencillos por el precio promocional.", MenuCategory.PROMOTION, "8000")
        );

        for (MenuItem activeItem : activeItems) {
            menuItemRepositoryPort.save(new MenuItem(
                    activeItem.id(),
                    activeItem.name(),
                    activeItem.description(),
                    activeItem.category(),
                    activeItem.price(),
                    false
            ));
        }

        for (MenuItem menuItem : menuItems) {
            menuItemRepositoryPort.save(menuItem);
        }
    }

    private MenuItem menuItem(String name, String description, MenuCategory category, String price) {
        return new MenuItem(null, name, description, category, new BigDecimal(price), true);
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
