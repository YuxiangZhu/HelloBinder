package com.ly.hellobinder.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.ly.hellobinder.Student;
import com.ly.hellobinder.proxy.Proxy;

import java.util.List;

/**
 */
public abstract class Stub extends Binder implements StudentManager {

    private static final String DESCRIPTOR = "com.ly.hellobinder.server.StudentManager";

    public Stub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static StudentManager asInterface(IBinder binder) {
        if (binder == null)
            return null;
        IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof StudentManager)
            return (StudentManager) iin;
        return new Proxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {

            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            case TRANSAVTION_getStudents:
                data.enforceInterface(DESCRIPTOR);
                List<Student> result = this.getStudents();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;

            case TRANSAVTION_addStudent:
                data.enforceInterface(DESCRIPTOR);
                Student arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = Student.CREATOR.createFromParcel(data);
                }
                this.addStudent(arg0);
                reply.writeNoException();
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }

    public static final int TRANSAVTION_getStudents = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSAVTION_addStudent = IBinder.FIRST_CALL_TRANSACTION + 1;
}
