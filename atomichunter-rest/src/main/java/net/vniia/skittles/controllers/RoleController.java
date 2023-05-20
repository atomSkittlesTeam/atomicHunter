package net.vniia.skittles.controllers;

import net.vniia.skittles.dto.RoleDto;
import net.vniia.skittles.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/get-data/id/{id}")
    public RoleDto getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/get-data/name/{name}")
    public RoleDto getRoleByName(@PathVariable String name) {
        return roleService.getRoleByName(name);
    }

    @GetMapping("/get-data")
    public List<RoleDto> getListRole() {
        return roleService.getListRole();
    }

    @GetMapping("/get-data-as-map/id")
    public Map<Long, RoleDto> getMapRoleToId() {
        return roleService.getMapRoleToId();
    }

    @GetMapping("/get-data-as-map/name")
    public Map<String, RoleDto> getMapRoleToName() {
        return roleService.getMapRoleToName();
    }
}
