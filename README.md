# Anti Capture

## 中文
`AntiCapture` 是一个基于 Java 的轻量级实用库，旨在防止特定窗口被截图软件、录屏工具（如 [OBS](https://obsproject.com/)、[Discord](https://discord.com/)、[Zoom](https://zoom.us/) 等）捕获。

通过 **JNA** 调用 Windows 和 MacOS 的原生系统 API 来实现窗口在截图软件面前“隐身”。_**理论上**_ 可以支持~~所有~~渲染库。

### 特性
* **跨平台支持：** 自动识别并支持 Windows 和 MacOS。
* Windows:
  1. 在 Windows 10 (1903+) 上支持完全防捕获`WDA_EXCLUDEFROMCAPTURE`。
  2. 在旧版本 Windows 只支持黑屏遮盖`WDA_MONITOR`。
* MacOS:
  1. 通过 AppKit 修改 `NSWindow` 的共享策略。
  2. 支持 **MacOS 12+** 的最新内容共享安全特性。

### 用法
```java
import dev.ag2o.anticapture.AntiCapture;

public class Main {
    public static void main(String[] args) {
        // 你的窗口初始化
        // ...

        // overlay: 要防捕获的窗口句柄
        // owner: 父窗口句柄
        AntiCapture.apply(overlay, owner);

        // 你的窗口渲染循环
        // ...
    }
}
```

### 截图
Used:  
![Used](/screenshot/img.png)

Unused:  
![Unused](/screenshot/img_1.png)

# 本项目采用 **GPL v3** 开源协议。
* **如果你是开源项目**：可以根据 GPL v3 条款自由使用。
* **如果你是闭源/商业项目**：根据 GPL v3 的条款，你**不能**直接在闭源软件中使用本项目。如需在商业闭源项目中使用，请联系作者获取**商业授权**。

📬 **联系**: [in1ect.ag2o@gmail.com](https://mail.google.com/)

## English
`AntiCapture` is a lightweight Java library designed to prevent specific windows from being captured by screen recording and screenshot software (such as [OBS](https://obsproject.com/), [Discord](https://discord.com/), [Zoom](https://zoom.us/), etc.).

By leveraging **JNA** to interface with native Windows and macOS APIs, it makes your application window "invisible" or "blacked out" to capture tools.

### Features
* **Cross-Platform:** Automatically detects and supports Windows and macOS.
* Windows:
  1. **Windows 10 (1903+):** Supports `WDA_EXCLUDEFROMCAPTURE`, making the window completely invisible in captures.
  2. **Legacy Windows:** Falls back to `WDA_MONITOR`, resulting in a blacked-out window in captures.
* MacOS:
  1. Modifies `NSWindow` sharing policy via AppKit.
  2. Supports the latest content sharing security features for **MacOS 12+**.

### Usage
```java
import dev.ag2o.anticapture.AntiCapture;

public class Main {
    public static void main(String[] args) {
        // your window init
        //...

        // overlay: The handle of the window to hide
        // owner: The handle of the parent/owner window
        AntiCapture.apply(overlay, owner);

        // your window render loop
        // ...
    }
}
```

# This project is licensed under **GPL v3**.
* **For Open Source Projects**: Feel free to use it under the terms of GPL v3.
* **For Closed Source/Commercial Projects**: Due to the copyleft nature of GPL v3, you **CANNOT** use this library in closed-source software. If you wish to use this in a commercial project, please contact the author for a **Commercial License**.

📬 **Contact**: [in1ect.ag2o@gmail.com](https://mail.google.com/)