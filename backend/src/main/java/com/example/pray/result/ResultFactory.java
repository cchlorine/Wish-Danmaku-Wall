package cc.sends.pray.result;

public class ResultFactory {

    public static Result buildSuccessResult(Object data){
        return buildResult(ResultCodeEnum.SUCCESS, "成功", data);
    }

    public static Result buildFailResult(String message){
        return buildResult(ResultCodeEnum.FAIL, message, null);
    }

    public static Result buildResult(ResultCodeEnum resultCode, String message, Object data){
        return buildResult(resultCode.code, message, data);
    }

    public static Result buildResult(int resultCode, String message, Object data){
        return new Result(resultCode, message, data);
    }
}
