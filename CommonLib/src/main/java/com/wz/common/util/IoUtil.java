package com.wz.common.util;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by wz on 2017/4/11.
 */
public class IoUtil {
    public static void safeClose(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
