package kr.co.igns.framework.config.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class byteArrayResult<T> extends CommonResult {
    private byte[] data;
}
