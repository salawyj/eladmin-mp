/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.security.service.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import me.zhengjie.modules.app.domain.AppUser;
import me.zhengjie.modules.system.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 李军辉
 */
@Data
@AllArgsConstructor
public class AppJwtUserDto {

    @ApiModelProperty(value = "用户")
    private final AppUser appUser;

    @ApiModelProperty(value = "角色")
    private final List<AuthorityDto> authorities;
  /*  @ApiModelProperty(value = "数据权限")
    private final List<Long> dataScopes;

    @ApiModelProperty(value = "角色")
    private final List<AuthorityDto> authorities;*/

}
