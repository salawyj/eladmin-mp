package me.zhengjie.modules.security.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSSendDto{

    @ApiModelProperty(value = "编码：success，fail")
    private String resultCode;
    @ApiModelProperty(value = "说明：发送成功，发送失败")
    private String msg;
}
