package dev.ag2o.anticapture.os;

import dev.ag2o.anticapture.jna.NtDll;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SuppressWarnings("unused")
public final class OSVersion {
    private static final OS os;
    private static final int majorVersion;
    private static final int buildVersion;

    static {
        String os_name = System.getProperty("os.name").toLowerCase();
        int os_version = -1;
        int os_build = -1;
        if (os_name.contains("win")) {
            os = OS.Windows;
            try {
                NtDll.RTL_OSVERSIONINFOW info = new NtDll.RTL_OSVERSIONINFOW();
                if (NtDll.INSTANCE.RtlGetVersion(info) == 0) {
                    os_version = info.dwMajorVersion;
                    os_build = info.dwBuildNumber;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (os_name.contains("mac")) {
            os = OS.MacOS;
            try {
                Process process = Runtime.getRuntime().exec("sw_vers -productVersion");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String readVersion = reader.readLine();
                    if (readVersion != null) {
                        os_version = Integer.parseInt(readVersion.split("\\.")[0]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            os = OS.IDK;
        }

        majorVersion = os_version;
        buildVersion = os_build;

        System.out.println("OS: " + os + ", Major: " + majorVersion + ", Build: " + buildVersion);
    }

    public static OS getOs() {
        return os;
    }

    public static int getVersion() {
        return majorVersion;
    }

    public static int getBuildVersion() {
        return buildVersion;
    }
}