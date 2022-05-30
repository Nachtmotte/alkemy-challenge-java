package alkemy.challenge.backend.controller;

import alkemy.challenge.backend.controller.util.ResponseEntityUtil;
import alkemy.challenge.backend.dto.user.UserGetDto;
import alkemy.challenge.backend.dto.user.UserPostDto;
import alkemy.challenge.backend.entity.Role;
import alkemy.challenge.backend.entity.User;
import alkemy.challenge.backend.security.util.JwtService;
import alkemy.challenge.backend.service.UserService;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static alkemy.challenge.backend.entity.Roles.Constants.ROLE_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;

    private final ModelMapper mapper;

    private final JwtService jwtService;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserPostDto requestUser) {

        if (!userService.isUniqueData(requestUser.getUsername(), requestUser.getEmail())) {
            return ResponseEntityUtil.generateResponse(HttpStatus.CONFLICT,
                    "message", "There is already users with this data in the system");
        }

        User user = mapper.map(requestUser, User.class);
        user = userService.save(user);
        UserGetDto userGetDto = mapper.map(user, UserGetDto.class);

        return ResponseEntityUtil.generateResponse(HttpStatus.CREATED, "user", userGetDto);
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers() {

        List<User> users = userService.getAll();
        List<UserGetDto> usersGetDto = mapper.map(users, new TypeToken<List<UserGetDto>>() {
        }.getType());

        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "users", usersGetDto);
    }

    @GetMapping("/auth/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {

        Map<String, Object> bodyResponse = new HashMap<>();

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = jwtService.decodeJwt(refreshToken);
                String username = decodedJWT.getSubject();
                User user = userService.getByUsername(username);

                String newAccessToken = jwtService.createAccessToken(username, user.getRoles()
                        .stream().map(Role::getName).collect(Collectors.toList()));
                String newRefreshToken = jwtService.createRefreshToken(username);

                bodyResponse.put("accessToken", newAccessToken);
                bodyResponse.put("refreshToken", newRefreshToken);
                return new ResponseEntity<>(bodyResponse, HttpStatus.OK);

            } catch (Exception e) {
                bodyResponse.put("errorMessage", e.getMessage());
                return new ResponseEntity<>(bodyResponse, HttpStatus.FORBIDDEN);
            }
        } else {
            bodyResponse.put("errorMessage", "Refresh token is missing");
            return new ResponseEntity<>(bodyResponse, HttpStatus.FORBIDDEN);
        }
    }
}
