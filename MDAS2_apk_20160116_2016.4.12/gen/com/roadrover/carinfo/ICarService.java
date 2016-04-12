/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/zyx/workspace/eclipse/MDAS2_apk_20160116/src/com/roadrover/carinfo/ICarService.aidl
 */
package com.roadrover.carinfo;
/**
 * System private API for talking with the carservice service.
 *
 * {@hide}
 */
public interface ICarService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.roadrover.carinfo.ICarService
{
private static final java.lang.String DESCRIPTOR = "com.roadrover.carinfo.ICarService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.roadrover.carinfo.ICarService interface,
 * generating a proxy if needed.
 */
public static com.roadrover.carinfo.ICarService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.roadrover.carinfo.ICarService))) {
return ((com.roadrover.carinfo.ICarService)iin);
}
return new com.roadrover.carinfo.ICarService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_getVersion:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getVersion();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_requestRRUpdates:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.roadrover.carinfo.ICarListener _arg1;
_arg1 = com.roadrover.carinfo.ICarListener.Stub.asInterface(data.readStrongBinder());
this.requestRRUpdates(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_unrequestRRUpdates:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.roadrover.carinfo.ICarListener _arg1;
_arg1 = com.roadrover.carinfo.ICarListener.Stub.asInterface(data.readStrongBinder());
this.unrequestRRUpdates(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_deviceControl:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _arg3;
_arg3 = data.readInt();
int _result = this.deviceControl(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_deviceControlExt:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
int _result = this.deviceControlExt(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDeviceIntParam:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _result = this.getDeviceIntParam(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDeviceParam:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _result = this.getDeviceParam(_arg0, _arg1);
reply.writeNoException();
reply.writeByteArray(_result);
return true;
}
case TRANSACTION_setDeviceIntParam:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _result = this.setDeviceIntParam(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setDeviceParam:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
int _result = this.setDeviceParam(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setCommonIntParam:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
int _result = this.setCommonIntParam(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getCommonIntParam:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.getCommonIntParam(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sendStringCommand:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _result = this.sendStringCommand(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.roadrover.carinfo.ICarService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public java.lang.String getVersion() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getVersion, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void requestRRUpdates(int notifytype, com.roadrover.carinfo.ICarListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(notifytype);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_requestRRUpdates, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unrequestRRUpdates(int notifytype, com.roadrover.carinfo.ICarListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(notifytype);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unrequestRRUpdates, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int deviceControl(int device, int controltype, int lparam, int wparam) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(controltype);
_data.writeInt(lparam);
_data.writeInt(wparam);
mRemote.transact(Stub.TRANSACTION_deviceControl, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int deviceControlExt(int device, int controltype, byte[] pdata) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(controltype);
_data.writeByteArray(pdata);
mRemote.transact(Stub.TRANSACTION_deviceControlExt, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getDeviceIntParam(int device, int paramid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(paramid);
mRemote.transact(Stub.TRANSACTION_getDeviceIntParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public byte[] getDeviceParam(int device, int paramid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
byte[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(paramid);
mRemote.transact(Stub.TRANSACTION_getDeviceParam, _data, _reply, 0);
_reply.readException();
_result = _reply.createByteArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setDeviceIntParam(int device, int paramid, int value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(paramid);
_data.writeInt(value);
mRemote.transact(Stub.TRANSACTION_setDeviceIntParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setDeviceParam(int device, int pararmid, byte[] value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(pararmid);
_data.writeByteArray(value);
mRemote.transact(Stub.TRANSACTION_setDeviceParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int setCommonIntParam(int device, int paramid, int value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(paramid);
_data.writeInt(value);
mRemote.transact(Stub.TRANSACTION_setCommonIntParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getCommonIntParam(int paramid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(paramid);
mRemote.transact(Stub.TRANSACTION_getCommonIntParam, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int sendStringCommand(int cmdtype, java.lang.String cmd, java.lang.String param) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmdtype);
_data.writeString(cmd);
_data.writeString(param);
mRemote.transact(Stub.TRANSACTION_sendStringCommand, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_getVersion = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_requestRRUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_unrequestRRUpdates = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_deviceControl = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_deviceControlExt = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getDeviceIntParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getDeviceParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_setDeviceIntParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_setDeviceParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setCommonIntParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getCommonIntParam = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_sendStringCommand = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
}
public java.lang.String getVersion() throws android.os.RemoteException;
public void requestRRUpdates(int notifytype, com.roadrover.carinfo.ICarListener listener) throws android.os.RemoteException;
public void unrequestRRUpdates(int notifytype, com.roadrover.carinfo.ICarListener listener) throws android.os.RemoteException;
public int deviceControl(int device, int controltype, int lparam, int wparam) throws android.os.RemoteException;
public int deviceControlExt(int device, int controltype, byte[] pdata) throws android.os.RemoteException;
public int getDeviceIntParam(int device, int paramid) throws android.os.RemoteException;
public byte[] getDeviceParam(int device, int paramid) throws android.os.RemoteException;
public int setDeviceIntParam(int device, int paramid, int value) throws android.os.RemoteException;
public int setDeviceParam(int device, int pararmid, byte[] value) throws android.os.RemoteException;
public int setCommonIntParam(int device, int paramid, int value) throws android.os.RemoteException;
public int getCommonIntParam(int paramid) throws android.os.RemoteException;
public int sendStringCommand(int cmdtype, java.lang.String cmd, java.lang.String param) throws android.os.RemoteException;
}
