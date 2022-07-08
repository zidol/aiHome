package kr.co.aihome.common.dto;

import lombok.Data;

@Data
public class ResponseData {

    private StatusEnum status;
    private String message;
    private Object data;

    public ResponseData() {
        this.status = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }
}
