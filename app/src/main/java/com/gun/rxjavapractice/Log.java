package com.gun.rxjavapractice;
public class Log {
    public static void d(String tag, Object obj) {
        System.out.println(getThreadName() + " | " + tag + " | debug = " + obj);
    }

    public static void e(String tag, Object obj) {
        System.out.println(getThreadName() + " | " + tag + " | error = " + obj);
    }

    public static void i(String tag, Object obj) {
        System.out.println(getThreadName() + " | " + tag + " | value = " + obj);
    }

    public static void v(Object obj) {
        System.out.println(getThreadName() + " | " + obj);
    }

    public static void d(Object obj) {
        System.out.println(getThreadName() + " | debug = " + obj);
    }

    public static void e(Object obj) {
        System.out.println(getThreadName() + " | error = " + obj);
    }

    public static void i(Object obj) {
        System.out.println(getThreadName() + " | value = " + obj);
    }

    public static void it(Object obj) {
        long time = System.currentTimeMillis() - CommonUtils.startTime;
        System.out.println(getThreadName() + " | " + time + " | " + "value = " + obj);
    }

//	public static void it(List<String> list) {
//		long time = System.currentTimeMillis() - CommonUtils.startTime;
//		StringBuffer sb = new StringBuffer();
//		sb.append(getThreadName() + " | " + time + " | " + "value = {");
//		for (String str : list) {
//			sb.append(str);
//			sb.append(",");
//		}
//		sb.append("}");
//		System.out.println(sb.toString().replace(",}", "}"));
//	}

    public static void dt(Object obj) {
        long time = System.currentTimeMillis() - CommonUtils.startTime;
        System.out.println(getThreadName() + " | " + time + " | " + "debug = " + obj);
    }

    public static void et(Object obj) {
        long time = System.currentTimeMillis() - CommonUtils.startTime;
        System.out.println(getThreadName() + " | " + time + " | " + "error = " + obj);
    }

    public static void onNextT(Object obj) {
        long time = System.currentTimeMillis() - CommonUtils.startTime;
        System.out.println(getThreadName() + " | " + time + " | " + "onNext >> " + obj);
    }

    public static void onCompleteT() {
        long time = System.currentTimeMillis() - CommonUtils.startTime;
        System.out.println(getThreadName() + " | " + time + " | " + "onComplete");
    }

    public static String getThreadName() {
        String threadName = Thread.currentThread().getName();
        if (threadName.length() > 30) {
            threadName = threadName.substring(0, 30) + "...";
        }
        return threadName;
    }

//      계산 스케줄러는 cpu에 대응하는 계산용 스케줄러. = 입출력 작업을 하지 않는 스케줄러
//      내부적으로 스레드 풀을 생성하며 스레드 개수는 기본적으로 프로세서의 개수와 동일
//      만약 계산 스케줄러의 기본적인 생성 개수를 수정하려면 시스템 프로퍼티인 rx2.computation-threads을 수정할 수 있다.
//      (권장사항 아님)


}