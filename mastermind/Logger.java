public final class Logger {
    private static int callCount = 0;

    public static void call(Object o) {
        call(o, currentMethod()+"()");
    }

    public static void call(Object o, String message) {
        System.out.printf("CALL %3d: %s\n", callCount, o.getClass().getName()+"."+message);
        callCount++;
    }

    public static void log(String name, Object o) {
        System.out.printf("--LOG: %10s = %s\n", name, o.toString());
        callCount++;
    }

    private static String currentMethod() {
        return Thread.currentThread().getStackTrace()[3].getMethodName();
    }
}
