package cn.com.sinofaith.util;

import cn.com.sinofaith.bean.bankBean.BankPersonEntity;

public class Compare {
    private int compare(String str, String target) {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) {                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) {                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private int min(int one, int two, int three)
    {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */

    public float getSimilarityRatio(String str, String target) {
        int a = compare(str,target);
        if(str.contains(target)||target.contains("str")){
            a-=5;
        }
        return 1 - (float) a / Math.max(str.length(), target.length());
    }

    public static void main(String[] args) {
        Compare lt = new Compare();
        String[] strs= {"交易日期","交易时间","本方户名","本方卡号","摘要","借",
                "贷","账户余额","交易机构名称",
        "对方账号","对方户名","交易对手账号","对方行名","交易渠道",
        "交易备注","币种"};
//        String[] target = {"交易姓名","交易户名","交易名称"};
        String[] target = {"借方发生额"};
        float result=0;
        String name = "";
        for(int i=0;i<strs.length;i++) {
            String temp = strs[i];
//            if(temp.contains("日期")||temp.contains("号")||temp.contains("卡")||temp.contains("时间")||temp.contains("方式")||temp.contains("余额")||temp.contains("IP")||temp.contains("名")){
//                continue;
//            }
            for (int j=0;j<target.length;j++) {
                float tempresult = lt.getSimilarityRatio(strs[i], target[j]);
                if(tempresult>result){
                    result = tempresult;
                    name = strs[i];
                }
            }
        }
        System.out.println(name+"      "+result);

        System.out.println(Long.parseLong("146628"));
    }
}

