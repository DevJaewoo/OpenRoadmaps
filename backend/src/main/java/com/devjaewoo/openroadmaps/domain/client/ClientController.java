package com.devjaewoo.openroadmaps.domain.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(new ClientDto.Response(client));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody ClientDto.Register request) {
        ClientDto client = clientService.login(request);
        return ResponseEntity.ok(new ClientDto.Response(client));
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        clientService.logout();
        return ResponseEntity.noContent().build();
    }
}
