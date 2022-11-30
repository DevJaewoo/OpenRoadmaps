package com.devjaewoo.openroadmaps.domain.client.controller;

import com.devjaewoo.openroadmaps.domain.client.dto.ClientDto;
import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.domain.client.service.ClientService;
import com.devjaewoo.openroadmaps.global.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody ClientDto.Register request) {
        ClientDto client = clientService.register(request);
        return ResponseEntity.ok(ClientDto.Response.from(client));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody ClientDto.Register request) {
        ClientDto client = clientService.login(request);
        return ResponseEntity.ok(ClientDto.Response.from(client));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        clientService.logout();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> getCurrentClientInfo() {
        SessionClient currentClient = SessionUtil.getCurrentClient();

        ClientDto clientDto = clientService.findClientById(currentClient.getId());
        return ResponseEntity.ok(ClientDto.Response.from(clientDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientInfo(@PathVariable Long id) {
        ClientDto clientDto = clientService.findClientById(id);
        return ResponseEntity.ok(ClientDto.Response.from(clientDto));
    }
}
