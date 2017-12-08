package io.github.enterprise.utils.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Sheldon on 2017/12/08
 */
@ApiModel(value = "返回结果", description = "接口返回的处理结果")
public class JsonResult<T> {

    /**
     * 处理成功
     */
    public static final String OK = "200";

    /**
     * 未知错误
     */
    public static final String ERR = "-1";

    @ApiModelProperty(name = "状态码", value = "除了 \" 200 \" 表示返回正确结果，其余都是异常状态")
    private String result = "-1";

    @ApiModelProperty(name = "描述", value = "返回结果的描述")
    private String desc = "";

    @ApiModelProperty(name = "数据", value = "接口返回的数据")
    private T data;

    public JsonResult() {

    }

    public JsonResult(String code, String message) {
        this.result = code;
        this.desc = message;
    }

    public JsonResult(String code, String message, T result) {
        this.result = code;
        this.desc = message;
        this.data = result;
    }

    public JsonResult(T result) {
        this(OK, "操作成功", result);
    }

    /**
     * 成功
     *
     * @param message 成功信息
     * @return 封装后的结果
     */
    public static JsonResult getSuccessResult(String message) {
        return new JsonResult(OK, message);
    }

    /**
     * 成功
     *
     * @param data 接口数据
     * @param message 成功信息
     * @return 封装后的结果
     */
    public static JsonResult getSuccessResult(Object data, String message) {
        return new JsonResult(OK, message, data);
    }

    /**
     * 成功
     *
     * @return 成功的结果
     */
    public static JsonResult getSuccessResult() {
        return JsonResult.getSuccessResult("操作成功");
    }

    /**
     * 失败结果
     *
     * @param message 失败信息
     * @return 失败结果
     */
    public static JsonResult getFailResult(String message) {
        return new JsonResult(ERR, message);
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        final JsonResult ret = (JsonResult) obj;
        return result == ret.getResult();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
