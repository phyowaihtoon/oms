package creatip.oms.service.message;

import java.io.Serializable;

public class ReplyMessage<T> extends BaseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
