package alkemy.challenge.backend.dto.user;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public abstract class UserDto {

    @NotBlank(message = "The Username is required.")
    @Size(min = 8, max = 20, message = "The length of the username must be greater than or equal to 8 characters and less than or equal to 20.")
    @Pattern(regexp = "^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$", message = "The Username is invalid")
    private String username;

    @NotBlank(message = "The Email is required.")
    @Size(max = 45, message = "The length of the Email cannot exceed 45 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "The Email is invalid")
    private String email;
}
