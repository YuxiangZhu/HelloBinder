package com.ly.hellobinder.proxy;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.ly.hellobinder.Student;
import com.ly.hellobinder.server.Stub;
import com.ly.hellobinder.server.StudentManager;

import java.util.List;

/**
 */
public class Proxy implements StudentManager {

    //包名加文件名
    private static final String DESCRIPTOR = "com.ly.hellobinder.server.StudentManager";

    private IBinder remote;

    public Proxy(IBinder remote) {

        this.remote = remote;
    }

    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public IBinder asBinder() {
        return remote;
    }

    @Override
    public List<Student> getStudents() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        List<Student> result;

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            remote.transact(Stub.TRANSAVTION_getStudents, data, replay, 0);
            replay.readException();
            result = replay.createTypedArrayList(Student.CREATOR);
        } finally {
            replay.recycle();
            data.recycle();
        }
        return result;
    }

    @Override
    public void addStudent(Student stu) throws RemoteException {

        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (stu != null) {
                data.writeInt(1);
                stu.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(Stub.TRANSAVTION_addStudent, data, replay, 0);
            replay.readException();
        } finally {
            replay.recycle();
            data.recycle();
        }
    }
}
