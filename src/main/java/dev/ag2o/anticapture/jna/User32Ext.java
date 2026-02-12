package dev.ag2o.anticapture.jna;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;

@SuppressWarnings({"UnusedReturnValue", "SpellCheckingInspection"})
public interface User32Ext extends User32 {
    User32Ext INSTANCE = Native.load("user32", User32Ext.class);

    int WDA_NONE = 0x00;
    int WDA_MONITOR = 0x00000001; // Fuck Win7
    int WDA_EXCLUDEFROMCAPTURE = 0x00000011; // Win10 1903...

    int GWLP_HWNDPARENT = -8;
    int GWL_STYLE = -16;
    int GWL_EXSTYLE = -20;

    int WS_EX_TRANSPARENT = 0x00000020;
    int WS_EX_LAYERED = 0x00080000;
    int WS_EX_NOACTIVATE = 0x08000000;

    boolean SetWindowDisplayAffinity(HWND hWnd, int dwAffinity);
    int GetWindowLongA(HWND hWnd, int nIndex);
    int SetWindowLongA(HWND hWnd, int nIndex, int dwNewLong);
    boolean SetWindowPos(HWND hWnd, HWND hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
    Pointer SetWindowLongPtrA(HWND hWnd, int nIndex, Pointer dwNewLong);
}