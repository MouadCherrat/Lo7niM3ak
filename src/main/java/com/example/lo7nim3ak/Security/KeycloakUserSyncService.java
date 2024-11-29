package com.example.lo7nim3ak.Security;

import com.example.lo7nim3ak.entities.User;
import com.example.lo7nim3ak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class KeycloakUserSyncService {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    private final RestTemplate restTemplate;
    @Autowired
    public KeycloakUserSyncService(@Lazy RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }


    @Scheduled(fixedRate = 60000)
    public void syncUsersFromKeycloak() {
        String keycloakUrl = "http://localhost:9000/realms/Lo7niM3ak/protocol/openid-connect/token";
        String clientId = "Lo7niM3ak-backend";
        String clientSecret = "awu6zIBiBrj3GhC184igwr3WoUvp0SdH";
        String grantType = "client_credentials";

        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("grant_type", grantType);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest, headers);

        Map<String, Object> response = restTemplate.postForObject(keycloakUrl, request, Map.class);
        String accessToken = (String) response.get("access_token");

        String usersUrl = "http://localhost:9000/admin/realms/Lo7niM3ak/users";
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(accessToken);

        HttpEntity<Void> authRequest = new HttpEntity<>(authHeaders);

        try {
            ResponseEntity<List> keycloakUsersResponse = restTemplate.exchange(usersUrl, HttpMethod.GET, authRequest, List.class);
            List<Map<String, Object>> keycloakUsers = keycloakUsersResponse.getBody();

            for (Map<String, Object> keycloakUser : keycloakUsers) {
                String username = (String) keycloakUser.get("username");
                String email = (String) keycloakUser.get("email");
                String firstName = (String) keycloakUser.get("firstName");
                String lastName = (String) keycloakUser.get("lastName");

                String rolesUrl = "http://localhost:9000/admin/realms/Lo7niM3ak/users/" + keycloakUser.get("id") + "/role-mappings/realm";
                ResponseEntity<List> rolesResponse = restTemplate.exchange(rolesUrl, HttpMethod.GET, authRequest, List.class);
                List<Map<String, Object>> roles = rolesResponse.getBody();
                StringBuilder rolesBuilder = new StringBuilder();

                if (roles != null) {
                    for (Map<String, Object> role : roles) {
                        String roleName = (String) role.get("name");
                        if ("driver".equals(roleName) || "passenger".equals(roleName)) {
                            rolesBuilder.append(roleName).append(",");
                        }
                    }
                }

                String rolesString = rolesBuilder.length() > 0 ? rolesBuilder.substring(0, rolesBuilder.length() - 1) : "";

                User user = userRepository.findByEmail(email).orElse(new User());
                user.setName(username);
                user.setFirstName(firstName);
                user.setEmail(email);
                user.setRole(rolesString);

                userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
