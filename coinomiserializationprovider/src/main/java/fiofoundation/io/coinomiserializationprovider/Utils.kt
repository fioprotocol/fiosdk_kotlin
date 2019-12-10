package fiofoundation.io.coinomiserializationprovider

class Utils {

    private enum class Runtime {
        ANDROID, ROBOVM, OPENJDK, ORACLE_JAVA
    }

    private enum class OS {
        LINUX, WINDOWS, MAC_OS, IOS
    }

    companion object {
        private var runtime: Runtime? = null
        private var os: OS? = null

        init {
            val runtimeProp:String = System.getProperty("java.runtime.name", "").toLowerCase()

            if (runtimeProp.equals(""))
                runtime = null
            else if (runtimeProp.contains("android"))
                runtime = Runtime.ANDROID
            else if (runtimeProp.contains("openjdk"))
                runtime = Runtime.OPENJDK
            else if (runtimeProp.contains("java(tm) se"))
                runtime = Runtime.ORACLE_JAVA
            else if (runtimeProp.contains("robovm"))
                runtime = Runtime.ROBOVM

            val osProp:String = System . getProperty ("os.name", "").toLowerCase()
            if (osProp.equals(""))
                os = null
            else if (osProp.contains("linux"))
                os = OS.LINUX
            else if (osProp.contains("win"))
                os = OS.WINDOWS
            else if (osProp.contains("mac"))
                os = OS.MAC_OS
            else if (osProp.contains("ios"))
                os = OS.IOS
        }

        fun isAndroidRuntime(): Boolean {
            return runtime === Runtime.ANDROID
        }

        fun isOpenJDKRuntime(): Boolean {
            return runtime === Runtime.OPENJDK
        }

        fun isOracleJavaRuntime(): Boolean {
            return runtime === Runtime.ORACLE_JAVA
        }

        fun isRoboVMRuntime(): Boolean {
            return runtime === Runtime.ROBOVM
        }

        fun isLinux(): Boolean {
            return os === OS.LINUX
        }

        fun isWindows(): Boolean {
            return os === OS.WINDOWS
        }

        fun isMac(): Boolean {
            return os === OS.MAC_OS
        }

        fun isIos(): Boolean {
            return os === OS.IOS
        }
    }


}