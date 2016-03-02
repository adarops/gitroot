/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/zyx/workspace/eclipse/MDAS2_apk_20160116/src/com/roadrover/carinfo/ICarListener.aidl
 */
package com.roadrover.carinfo;
/**
 * {@hide}
 */
public interface ICarListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.roadrover.carinfo.ICarListener
{
private static final java.lang.String DESCRIPTOR = "com.roadrover.carinfo.ICarListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.roadrover.carinfo.ICarListener interface,
 * generating a proxy if needed.
 */
public static com.roadrover.carinfo.ICarListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.roadrover.carinfo.ICarListener))) {
return ((com.roadrover.carinfo.ICarListener)iin);
}
return new com.roadrover.carinfo.ICarListener.Stub.Proxy(obj);
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
case TRANSACTION_onDeviceParamChanged:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
int _result = this.onDeviceParamChanged(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onDeviceInfo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
byte[] _arg2;
_arg2 = data.createByteArray();
int _result = this.onDeviceInfo(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_onCommonParamChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
byte[] _arg3;
_arg3 = data.createByteArray();
int _result = this.onCommonParamChange(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.roadrover.carinfo.ICarListener
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
@Override public int onDeviceParamChanged(int device, int paramid, byte[] value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(paramid);
_data.writeByteArray(value);
mRemote.transact(Stub.TRANSACTION_onDeviceParamChanged, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onDeviceInfo(int device, int msgtype, byte[] msgdata) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(device);
_data.writeInt(msgtype);
_data.writeByteArray(msgdata);
mRemote.transact(Stub.TRANSACTION_onDeviceInfo, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int onCommonParamChange(int paramid, int lparam, int wparam, byte[] extra) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(paramid);
_data.writeInt(lparam);
_data.writeInt(wparam);
_data.writeByteArray(extra);
mRemote.transact(Stub.TRANSACTION_onCommonParamChange, _data, _reply, 0);
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
static final int TRANSACTION_onDeviceParamChanged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onDeviceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onCommonParamChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public int onDeviceParamChanged(int device, int paramid, byte[] value) throws android.os.RemoteException;
public int onDeviceInfo(int device, int msgtype, byte[] msgdata) throws android.os.RemoteException;
public int onCommonParamChange(int paramid, int lparam, int wparam, byte[] extra) throws android.os.RemoteException;
}
