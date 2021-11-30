package org.example.result;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public Result(CodeMsg cm) {
        if(cm == null){
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    //返回成功
    public static<T> Result<T> success(T data){
        return  new Result<T>(data);
    }

    //返回失败
    public static<T> Result<T> error(CodeMsg cm){
        return  new Result<T>(cm);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
