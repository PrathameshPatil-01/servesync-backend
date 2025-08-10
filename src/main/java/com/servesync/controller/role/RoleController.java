package com.servesync.controller.role;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servesync.dto.role.CreateRoleRequestDTO;
import com.servesync.dto.role.RoleDTO;
import com.servesync.dto.service.ServiceWithSubServiceDTO;
import com.servesync.entity.service.Service;
import com.servesync.service.role.RoleService;
import com.servesync.service.service.ServiceService;

@RestController
@RequestMapping("/api/role")
public class RoleController {
	
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllServices() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
    
    @PostMapping("/add-role")
    public ResponseEntity<RoleDTO> addServiceWithSubService(@RequestBody CreateRoleRequestDTO dto) {
    	System.out.print("dto: ");
    	System.out.println(dto);
       
        return ResponseEntity.ok(roleService.createRole(dto));
    }


}
