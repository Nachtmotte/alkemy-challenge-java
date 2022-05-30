package alkemy.challenge.backend.dto.user;

import alkemy.challenge.backend.dto.role.RoleGetDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserGetDto extends UserDto{

    private Set<RoleGetDto> roles;
}
