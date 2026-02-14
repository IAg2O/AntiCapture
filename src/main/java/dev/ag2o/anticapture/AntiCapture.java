package dev.ag2o.anticapture;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import dev.ag2o.anticapture.jna.User32Ext;
import dev.ag2o.anticapture.os.OSVersion;

@SuppressWarnings("unused")
public final class AntiCapture {
    private AntiCapture() {
    }

    public static class Windows {
        private Windows() {
        }

        public static void applyOwner(long windowHandle, long ownerHandle) {
            if (windowHandle == 0 || ownerHandle == 0) {
                return;
            }

            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(windowHandle));
            WinDef.HWND ownerHwnd = new WinDef.HWND(new Pointer(ownerHandle));

            User32Ext.INSTANCE.SetWindowLongPtrA(overlayHwnd, User32Ext.GWLP_HWNDPARENT, ownerHwnd.getPointer());
            User32Ext.INSTANCE.SetWindowPos(overlayHwnd, ownerHwnd, 0, 0, 0, 0, User32Ext.SWP_NOMOVE | User32Ext.SWP_NOSIZE | User32Ext.SWP_NOACTIVATE | User32Ext.SWP_FRAMECHANGED);
        }

        public static void applyStyle(long windowHandle) {
            if (windowHandle == 0) {
                return;
            }

            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(windowHandle));
            int oldS = User32Ext.INSTANCE.GetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE);
            int newS = oldS | User32Ext.WS_EX_LAYERED | User32Ext.WS_EX_TRANSPARENT | User32Ext.WS_EX_NOACTIVATE;

            User32Ext.INSTANCE.SetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE, newS);
        }

        public static void applyAffinity(long windowHandle) {
            if (windowHandle == 0) {
                return;
            }

            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(windowHandle));
            int affinity = 0;
            if (OSVersion.getVersion() > 10 || (OSVersion.getVersion() == 10 && OSVersion.getBuildVersion() >= 18362)) {
                affinity = User32Ext.WDA_EXCLUDEFROMCAPTURE;
            } else if (OSVersion.getVersion() >= 6) {
                affinity = User32Ext.WDA_MONITOR;
            }

            User32Ext.INSTANCE.SetWindowDisplayAffinity(overlayHwnd, affinity);
        }

        public static void resetOwner(long windowHandle) {
            if (windowHandle == 0) {
                return;
            }

            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(windowHandle));

            User32Ext.INSTANCE.SetWindowLongPtrA(overlayHwnd, User32Ext.GWLP_HWNDPARENT, Pointer.NULL);
            User32Ext.INSTANCE.SetWindowPos(overlayHwnd, null, 0, 0, 0, 0, User32Ext.SWP_NOMOVE | User32Ext.SWP_NOSIZE | User32Ext.SWP_NOACTIVATE | User32Ext.SWP_FRAMECHANGED | User32Ext.SWP_NOZORDER);
        }

        public static void resetStyle(long windowHandle) {
            if (windowHandle == 0) {
                return;
            }

            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(windowHandle));
            int oldS = User32Ext.INSTANCE.GetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE);
            int newS = oldS & ~User32Ext.WS_EX_LAYERED | User32Ext.WS_EX_TRANSPARENT | User32Ext.WS_EX_NOACTIVATE;

            User32Ext.INSTANCE.SetWindowLongA(overlayHwnd, User32Ext.GWL_EXSTYLE, newS);
        }

        public static void resetAffinity(long windowHandle) {
            if (windowHandle == 0) {
                return;
            }

            WinDef.HWND overlayHwnd = new WinDef.HWND(new Pointer(windowHandle));

            User32Ext.INSTANCE.SetWindowDisplayAffinity(overlayHwnd, User32Ext.WDA_NONE);
        }
    }
}
