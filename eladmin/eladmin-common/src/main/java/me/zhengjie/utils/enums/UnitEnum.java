package me.zhengjie.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 货币类型
 */
@Getter
@AllArgsConstructor
public enum UnitEnum {
    RMB("￥", "元"),

    /* 自己部门的数据权限 */
    MY("$", "美元");
    private final String key;
    private final String description;

}
