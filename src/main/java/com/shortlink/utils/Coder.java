package com.shortlink.utils;

public class Coder {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(Long id) {
        var code = new StringBuilder();
        Long copyId = id;
        while (copyId > 0) {
            int indexInBase62 = (int)(copyId % 62);
            code.append(BASE62.charAt(indexInBase62));
            copyId = copyId / 62;
        }

// для контроля чтобы было сложнее просто перейти на чужую ссылку - добавляется буква
        code.append(BASE62.charAt((int)(id % 26 + 10)));
// и добавляется цифра
        code.append(BASE62.charAt((int)(id % 10)));
        code.append("LS"); // признак ссылки

        return code.reverse().toString();
    }

    public static Long decode(String str) {

// минимально 4 буквы SL + A + 5
        if (str == null || str.length() < 5) {
            return null;
        }
        if((!str.startsWith("SL"))) {
            return null;
        }
        String chackLetter = String.valueOf(str.charAt(3));
        String chackNumber = String.valueOf(str.charAt(2));

        long res = 0;
        for (int i = 4; i < str.length(); i++) {
            int nnn = BASE62.indexOf(str.charAt(i));
            res = res * 62 + nnn;
        }

// если не совпали проверочные бувка и цифра - то ссылка поддельная
        if (!chackLetter.equals(String.valueOf(BASE62.charAt((int)(res % 26 + 10))))
                || (!chackNumber.equals(String.valueOf(res % 10)))) {
            return null;
        }

        return res;
    }

}
