
package me.zhengjie.modules.security.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 修改用户手机的参数
 */
@Getter
@Setter
public class AuthUserPhoneDto {

    @NotBlank
    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String code;
}
