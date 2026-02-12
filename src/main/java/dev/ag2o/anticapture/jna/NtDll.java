package dev.ag2o.anticapture.jna;

import com.sun.jna.Native;
import com.sun.jna.Structure;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public interface NtDll extends com.sun.jna.Library {
    NtDll INSTANCE = Native.load("ntdll", NtDll.class);

    @Structure.FieldOrder({"dwOSVersionInfoSize", "dwMajorVersion", "dwMinorVersion", "dwBuildNumber", "dwPlatformId", "szCSDVersion"})
    class RTL_OSVERSIONINFOW extends Structure {
        public int dwOSVersionInfoSize;
        public int dwMajorVersion;
        public int dwMinorVersion;
        public int dwBuildNumber;
        public int dwPlatformId;
        public char[] szCSDVersion = new char[128];

        public RTL_OSVERSIONINFOW() {
            dwOSVersionInfoSize = size();
        }
    }

    int RtlGetVersion(RTL_OSVERSIONINFOW lpVersionInformation);
}