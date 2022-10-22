package com.devjaewoo.openroadmaps.domain.client;

import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
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

    @GetMapping("")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> getCurrentClientInfo() {
        SessionClient currentClient = SessionUtil.getCurrentClient()
                .orElseThrow(() -> new RestApiException(CommonErrorCode.UNAUTHORIZED));

        ClientDto clientDto = clientService.findClientById(currentClient.getId());
        return ResponseEntity.ok(new ClientDto.Response(clientDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientInfo(@PathVariable Long id) {
        ClientDto clientDto = clientService.findClientById(id);
        return ResponseEntity.ok(new ClientDto.Response(clientDto));
    }
}
