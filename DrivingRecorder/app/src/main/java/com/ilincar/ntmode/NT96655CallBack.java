package com.ilincar.ntmode;

/**
 * Created by zoufeng on 16/3/5.
 */
public interface NT96655CallBack {

    /**
     * 当发送0x20 获取状态指令后，回调接口
     */
    public void onGetN96655Data(String txbuf);

}
