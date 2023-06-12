package com.library.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	@NotEmpty (message = "Vui lòng nhập!")
    private String name;

	@NotEmpty
	@Email(message = "Email không hợp lệ!")
    private String username;

    @NotEmpty
    @Length(min = 8, message = "Mật khẩu phải có nhiều hơn 8 ký tự!")
    private String password;

    @NotEmpty
    private String repeatPassword;
    
    @Pattern(regexp = "[0-9]+", message = "Số điện thoại không hợp lệ!")
    private String phoneNumber;
   

}
