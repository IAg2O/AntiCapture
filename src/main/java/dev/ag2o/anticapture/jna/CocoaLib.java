package dev.ag2o.anticapture.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface CocoaLib extends Library {
    CocoaLib INSTANCE = Native.load("AppKit", CocoaLib.class);
    Pointer sel_registerName(String name);
    void objc_msgSend(Pointer receiver, Pointer selector, long arg1);
}