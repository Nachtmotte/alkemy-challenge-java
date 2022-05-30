package alkemy.challenge.backend.service;

import alkemy.challenge.backend.entity.Role;
import alkemy.challenge.backend.entity.Roles;
import alkemy.challenge.backend.entity.User;
import alkemy.challenge.backend.repository.RoleRepository;
import alkemy.challenge.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepo.findByName(Roles.ROLE_USER.name());
        user.getRoles().add(roleUser);
        return userRepo.save(user);
    }

    public User getByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public void delete(Long userId){
        try {
            userRepo.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("User with id " + userId + " does no exist");
        }
    }

    public boolean isUniqueData(String username, String email) {
        User user = userRepo.findByUsernameOrEmail(username, email);
        return user == null;
    }
}
