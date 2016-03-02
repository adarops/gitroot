package com.roadrover.carinfo;

import com.roadrover.carinfo.ICarListener;


/**
 * System private API for talking with the carservice service.
 *
 * {@hide}
 */
interface ICarService
{
		String getVersion();
        void requestRRUpdates(in int notifytype, in ICarListener listener);
        void unrequestRRUpdates(in int notifytype,in ICarListener listener);
        int deviceControl(int device,int controltype,int lparam,int wparam);
        int deviceControlExt(int device,int controltype,in byte[] pdata);     
        
  		int getDeviceIntParam(int device,int paramid);
        byte[] getDeviceParam(int device,int paramid);
        
        int setDeviceIntParam(int device,int paramid,int value);
        int setDeviceParam(int device,int pararmid,in byte[] value);
        
         int setCommonIntParam(int device,int paramid,int value);
         int getCommonIntParam(int paramid);
         int sendStringCommand(int cmdtype,String cmd, String param);
} 