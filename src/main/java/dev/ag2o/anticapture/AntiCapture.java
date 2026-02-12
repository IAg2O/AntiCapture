package dev.ag2o.anticapture;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import dev.ag2o.anticapture.jna.CocoaLib;
import dev.ag2o.anticapture.jna.User32Ext;
import dev.ag2o.anticapture.os.OSVersion;

@SuppressWarnings("unused")
public final class AntiCapture {
    private AntiCapture() {
    }

    public static void applyWindows(long overlay, long owner) {
        try {
            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(overlay));
            if (owner != 0) {
                WinDef.HWND ownerHwnd = new WinDef.HWND(new Pointer(owner));
                User32Ext.INSTANCE.SetWindowLongPtrA(overlayHwnd, User32Ext.GWLP_HWNDPARENT, ownerHwnd.getPointer());
                User32Ext.INSTANCE.SetWindowPos(overlayHwnd, ownerHwnd, 0, 0, 0, 0, User32Ext.SWP_NOMOVE | User32Ext.SWP_NOSIZE | User32Ext.SWP_NOACTIVATE | User32Ext.SWP_FRAMECHANGED);

                int oldS = User32Ext.INSTANCE.GetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE);
                int newS = oldS | User32Ext.WS_EX_LAYERED | User32Ext.WS_EX_TRANSPARENT | User32Ext.WS_EX_NOACTIVATE;

                User32Ext.INSTANCE.SetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE, newS);
            }

            int affinity = 0;
            if (OSVersion.getVersion() > 10 || (OSVersion.getVersion() == 10 && OSVersion.getBuildVersion() >= 18362)) {
                affinity = User32Ext.WDA_EXCLUDEFROMCAPTURE;
            } else if (OSVersion.getVersion() >= 6) {
                affinity = User32Ext.WDA_MONITOR;
            }
            if (affinity != 0) {
                User32Ext.INSTANCE.SetWindowDisplayAffinity(overlayHwnd, affinity);
            } else {
                System.err.println("[AntiCapture] Error to set AntiCapture for this window: " + overlay + "; OS: " + OSVersion.getOs().toString() + " " + OSVersion.getVersion() + " " + OSVersion.getBuildVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // i not have a mac >_<
    public static void appleMac(long overlay, long owner) {
        try {
            Pointer nsWindow = new Pointer(overlay);

            CocoaLib.INSTANCE.objc_msgSend(nsWindow, CocoaLib.INSTANCE.sel_registerName("setSharingType:"), 0L);
            if (OSVersion.getVersion() >= 12) {
                CocoaLib.INSTANCE.objc_msgSend(nsWindow, CocoaLib.INSTANCE.sel_registerName("setContentSharingPolicy:"), 1L);
            }

            if (owner != 0) {
                Pointer parentWindow = new Pointer(owner);
                CocoaLib.INSTANCE.objc_msgSend(parentWindow, CocoaLib.INSTANCE.sel_registerName("addChildWindow:ordered:"), nsWindow, 1L);
            }

            CocoaLib.INSTANCE.objc_msgSend(nsWindow, CocoaLib.INSTANCE.sel_registerName("setLevel:"), 3L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
