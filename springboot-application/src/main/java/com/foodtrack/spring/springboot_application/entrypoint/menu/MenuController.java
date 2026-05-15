package com.foodtrack.spring.springboot_application.entrypoint.menu;

import com.foodtrack.spring.springboot_application.application.port.in.MenuUseCase;
import com.foodtrack.spring.springboot_application.domain.model.MenuItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@Tag(name = "Menu")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

    private final MenuUseCase menuUseCase;
  
private final MenuItemRestMapper menuItemMapper;

public MenuController(MenuUseCase menuUseCase, MenuItemRestMapper menuItemMapper) {
    this.menuUseCase = menuUseCase;
    this.menuItemMapper = menuItemMapper;
}
    @GetMapping
    @Operation(summary = "List all active menu items")
    public ResponseEntity<List<MenuItemResponse>> listMenuItems() {
        List<MenuItem> items = menuUseCase.listMenuItems();
        List<MenuItemResponse> response = items.stream()
                .map(menuItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all menu items for administration")
    public ResponseEntity<List<MenuItemResponse>> listAllMenuItems() {
        return ResponseEntity.ok(menuUseCase.listAllMenuItems().stream()
                .map(menuItemMapper::toResponse)
                .toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a menu item")
    public ResponseEntity<MenuItemResponse> createMenuItem(@Valid @RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemMapper.toResponse(
                menuUseCase.createMenuItem(
                        request.name(),
                        request.description(),
                        request.category(),
                        request.price(),
                        request.stockQuantity()
                )
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a menu item")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemRequest request
    ) {
        return ResponseEntity.ok(menuItemMapper.toResponse(
                menuUseCase.updateMenuItem(
                        id,
                        request.name(),
                        request.description(),
                        request.category(),
                        request.price(),
                        request.stockQuantity(),
                        request.active()
                )
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate a menu item")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuUseCase.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
