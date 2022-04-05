package cc.sends.pray.result;

public enum ResultCodeEnum {
    SUCCESS(200),
    FAIL(400),
    TOKEN_ERROR(440), // token出错
    USER_NOT_FOUND(441), // 找不到该用户

    // /submit 许愿接口
    WISH_EMPTY(441), // 许愿内容不能为空
    WISH_LENGTH_ERROR(442), // 许愿内容过长，超过20字
    WISH_LIMIT(444), // 只可以许三个愿望
    ILLEGAL(445), // 存在非法单词

    // /favor 点赞接口
    FAVORED(443), // 你已经为该条愿望点过赞啦
    MESSAGE_NOT_FOUND(463), // 找不到该条愿望
    ;


    public int code;

    ResultCodeEnum(int code){
        this.code = code;
    }
}
