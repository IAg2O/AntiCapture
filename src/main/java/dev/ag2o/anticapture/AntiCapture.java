package dev.ag2o.anticapture;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import dev.ag2o.anticapture.jna.CocoaLib;
import dev.ag2o.anticapture.jna.User32Ext;
import dev.ag2o.anticapture.os.OSVersion;

@SuppressWarnings("unused")
public class AntiCapture {
    private AntiCapture() {
    }

    public static void apply(long overlay, long owner) {
        switch (OSVersion.getOs()) {
            case Windows -> new Windows(overlay, owner);
            case MacOS -> new MacOS(overlay, owner);
            case IDK -> System.err.println("[AntiCapture] Unsupported operating system.");
        };
    }

    public static void applyWindows(long overlay, long owner) {
        new Windows(overlay, owner);
    }

    public static void appleMac(long overlay, long owner) {
        new MacOS(overlay, owner);
    }

    private static final class Windows extends AntiCapture {
        public Windows(long overlay, long owner) {
            try {
                WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(overlay));
                if (owner != 0) {
                    WinDef.HWND ownerHwnd = new WinDef.HWND(new Pointer(owner));
                    User32Ext.INSTANCE.SetWindowLongPtrA(overlayHwnd, User32Ext.GWLP_HWNDPARENT, ownerHwnd.getPointer());
                    User32Ext.INSTANCE.SetWindowPos(overlayHwnd, ownerHwnd, 0, 0, 0, 0, User32Ext.SWP_NOMOVE | User32Ext.SWP_NOSIZE | User32Ext.SWP_NOACTIVATE | User32Ext.SWP_FRAMECHANGED);
                }

                int affinity = 0;
                if (OSVersion.getVersion() > 10 || (OSVersion.getVersion() == 10 && OSVersion.getBuildVersion() >= 18362)) {
                    affinity = User32Ext.WDA_EXCLUDEFROMCAPTURE;
                } else if (OSVersion.getVersion() >= 6) {
                    affinity = User32Ext.WDA_MONITOR;
                }
                if (affinity != 0) {
                    User32Ext.INSTANCE.SetWindowDisplayAffinity(overlayHwnd, affinity);
                }

                int oldS = User32Ext.INSTANCE.GetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE);
                int newS = oldS | User32Ext.WS_EX_LAYERED | User32Ext.WS_EX_TRANSPARENT | User32Ext.WS_EX_NOACTIVATE;

                User32Ext.INSTANCE.SetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE, newS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static final class MacOS extends AntiCapture {
        public MacOS(long overlay, long owner) {
            try {
                Pointer nsWindow = new Pointer(overlay);

                Pointer setSharingType = CocoaLib.INSTANCE.sel_registerName("setSharingType:");
                CocoaLib.INSTANCE.objc_msgSend(nsWindow, setSharingType, 0L);

                if (OSVersion.getVersion() >= 12) {
                    Pointer setSharingPolicy = CocoaLib.INSTANCE.sel_registerName("setContentSharingPolicy:");
                    CocoaLib.INSTANCE.objc_msgSend(nsWindow, setSharingPolicy, 1L);
                }

                Pointer setLevel = CocoaLib.INSTANCE.sel_registerName("setLevel:");
                CocoaLib.INSTANCE.objc_msgSend(nsWindow, setLevel, 3L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
