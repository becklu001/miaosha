package com.becklu.response;

public class CommonReturnType {
    //对应请求的返回处理结果状态，有“success”和“fail”两种
    private String status;
    //若status为success，则data返回前端需要的json数据
    //若status为fail，则data内使用通用的错误码格式
    private Object data;

    //定义一个通用的创建方法
    //不是通过构造方法创建，而是通过静态方法创建，为何如此？
    //如果不带status参数，则默认添加 success状态
    public static CommonReturnType create(Object result){
        return CommonReturnType.create(result,"success");
    }

    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setData(result);
        type.setStatus(status);
        return type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
