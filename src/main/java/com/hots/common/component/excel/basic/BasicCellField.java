package com.hots.common.component.excel.basic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BasicCellField {

    /**
     * 导出到Excel中的名字.
     */
    public abstract String name();

    /**
     * 配置列的顺序:0、1、2...
     */
    public abstract int column();

    /**
     * 提示信息
     */
    public abstract String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容.
     */
    public abstract String[] combo() default {};

    /**
     * 是否导出数据
     */
    public abstract boolean isExport() default true;

}