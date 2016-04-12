package com.roadrover.carinfo;

/**
 * {@hide}
 */
interface ICarListener
{
        int onDeviceParamChanged(in int device,in int paramid,in byte[] value);
        int onDeviceInfo(in int device,in int msgtype,in byte[] msgdata);
        int onCommonParamChange(in int paramid, in int lparam, in int wparam, in byte[] extra);
}