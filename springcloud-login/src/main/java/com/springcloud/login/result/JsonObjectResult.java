package com.springcloud.login.result;

public class JsonObjectResult {
    private String code;
    private String message;
    private Object data;

    public JsonObjectResult() {
        this.setCode(ResultCode.SUCCESS);
        this.setMessage("成功！");

    }

    public JsonObjectResult(ResultCode code) {
        this.setCode(code);
        this.setMessage(code.msg());
    }

    public JsonObjectResult(ResultCode code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public JsonObjectResult(ResultCode code, String message, Object data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public JsonObjectResult(Object data) {
        this.setData(data);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    public void setCode(ResultCode code) {
        this.code = code.val();
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
