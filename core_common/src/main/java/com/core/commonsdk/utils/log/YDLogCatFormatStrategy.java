package com.core.commonsdk.utils.log;


import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.core.commonsdk.utils.log.YDLogUtils.checkNotNull;


public class YDLogCatFormatStrategy implements FormatStrategy {
    private static final String YD_NEW_LINE = "\n";
    private static final String YD_SEPARATOR = " ";
    private static final String YD_BRACKET_LEFT= "[";
    private static final String YD_BRACKET_RIGHT= "]";
    private static final String YD_ROW_ROD= " - ";
    private static final String YD_FILE_NAME= "文件名：";
    private static final String YD_METHOD_NAME= "方法名：";
    private static final String YD_LINE_NUMBER= "行数：";
    private static final String YD_LINE_NUMBER_U= "行";
    private static final String YD_THREAD= "线程：";

    @NonNull
    private final LogStrategy logStrategy;
    @Nullable
    private final String tag;

    private YDLogCatFormatStrategy(@NonNull Builder builder) {
        checkNotNull(builder);

        logStrategy = builder.logStrategy;
        tag = builder.tag;
    }

    @NonNull public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        checkNotNull(message);

        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackIndex= YDLogUtils.getStackOffset(trace);
        String threadName = Thread.currentThread().getName();

        StringBuilder builder = new StringBuilder();
        builder.append(YD_BRACKET_LEFT);

        // thread
        if (threadName != null) {
            builder.append(YD_THREAD);
            builder.append(threadName);
            builder.append(YD_SEPARATOR);
        }

        // class and method
        if (stackIndex <= trace.length - 1) {
            builder.append(YD_FILE_NAME);
            builder.append(trace[stackIndex].getFileName());
            builder.append(YD_SEPARATOR);
            builder.append(YD_LINE_NUMBER);
            builder.append(trace[stackIndex].getLineNumber());
            builder.append(YD_LINE_NUMBER_U);
            builder.append(YD_SEPARATOR);
            builder.append(YD_METHOD_NAME);
            builder.append(trace[stackIndex].getMethodName());
        } else  {
            // TODO
        }

        builder.append(YD_BRACKET_RIGHT);

        // message
        builder.append(YD_ROW_ROD);
        builder.append(message);

        builder.append(YD_NEW_LINE);

        logStrategy.log(priority, tag, builder.toString());
    }

    public static final class Builder {
        LogStrategy logStrategy;
        String tag = "YD_PRETTY_LOGGER";

        private Builder() {
        }

        @NonNull public YDLogCatFormatStrategy.Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull public YDLogCatFormatStrategy.Builder tag(@Nullable String tag) {
            this.tag = tag;
            return this;
        }

        @NonNull public YDLogCatFormatStrategy build() {
            return new YDLogCatFormatStrategy(this);
        }
    }
}
