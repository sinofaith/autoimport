package cn.com.sinofaith.util;

import cn.com.sinofaith.bean.bankBean.BankCustomerEntity;
import cn.com.sinofaith.bean.bankBean.BankZcxxEntity;
import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public abstract class ExcelReader extends DefaultHandler {

    private SharedStringsTable sst;
    //上一次的内容
    private String lastContents;
    private boolean nextIsString;

    private int sheetIndex = -1;
    private List<String> rowlist = new ArrayList<String>();
    //当前行
    private int curRow = 0;
    //当前列
    private int curCol = 0;
    //日期标志
    private boolean dateFlag;
    //数字标志
    private boolean numberFlag;
    //前一个单元格的xy
    private String preXy = "";
    //当前单元格的xy
    private String currXy = "";
    //前一个单元格的x
    private String preX = "";
    //当前单元格的x
    private String currX = "";
    //定义该文档一行最大的单元格数,用来补全一行最后可能缺失的单元格
    private String maxRef = null;
    //定义该文档一行最小的单元格数,用来补全开头缺失的单元格
    private String minRef = null;
    //是否跳过了单元格
    private boolean isSkipCeil = false;

    private boolean isTElement;
    //两个不为空的单元格之间隔了多少个空的单元格
    private int flag = 0;

    private String lastName= "";

    /**
     * 只遍历一个电子表格，其中sheetId为要遍历的sheet索引，从1开始，1-3
     *
     * @param filename
     * @param sheetId
     * @throws Exception
     */
    public void processOneSheet(String filename, int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId" + sheetId);
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     * 遍历工作簿中所有的电子表格
     *
     * @param filename
     * @throws Exception
     */
    public void process(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
        pkg.flush();
        pkg.close();
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst)
            throws SAXException {
        XMLReader parser = XMLReaderFactory
                .createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public int twentyToDecimai(String s) {

        int result = 0;
        if (s == null || s.length() == 0) {
            return result;
        }

        char[] charArray = s.toCharArray();
        //遍历字符数组，从数组的尾部开始计算
        for (int i = charArray.length - 1; i >= 0; i--) {
            //拿到对应字符对应的数字
            int val = charArray[i] - 64;
            //拿到指数
            int exp = charArray.length - i - 1;
            result += val * Math.pow(26, exp);
        }
        return result;
    }

    //读取单元格的格式
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {

        // c => 单元格
        if ("c".equals(name)) {
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            if ("s".equals(cellType)) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
            //日期格式
            String cellDateType = attributes.getValue("s");
            if ("1".equals(cellDateType)) {
                dateFlag = true;
            } else {
                dateFlag = false;
            }
            String cellNumberType = attributes.getValue("s");
            if ("2".equals(cellNumberType)) {
                numberFlag = true;
            } else {
                numberFlag = false;
            }
            //与判断空单元格有关
            isSkipCeil = false;
            String cellXy = attributes.getValue("r");
            if ("".equals(preXy)) {
                preXy = cellXy;
            }
            if (minRef == null) {
                minRef = cellXy;
            }
            currXy = cellXy;
            preX = preXy.replaceAll("\\d", "").trim();
            currX = currXy.replaceAll("\\d", "").trim();

            int cur = Integer.parseInt(currXy.replaceAll("\\D", ""));
            int pre = Integer.parseInt(preXy.replaceAll("\\D", ""));
            String min = minRef.replaceAll("\\d", "");
            if (cur - pre > 0) {
                int len = twentyToDecimai(currX) - twentyToDecimai(min);
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        rowlist.add(curCol, "");
                        curCol++;
                    }
                }
            }

            flag = twentyToDecimai(currX) - twentyToDecimai(preX);

            if (flag != 0 && flag != 1 && flag > 0) {
                isSkipCeil = true;
            }
            preXy = cellXy;
        }
        //当元素为t时
        if ("t".equals(name)) {
            isTElement = true;
        } else {
            isTElement = false;
        }

        // 置空
        lastContents = "";
        if(lastName.equals("c")&&name.equals("c")){
            rowlist.add(curCol, "");
            curCol++;
        }
        lastName = name;
    }

    //读取单元格的内容
    public void endElement(String uri, String localName, String name)
            throws SAXException {

        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
                        .toString();
            } catch (Exception e) {

            }
        }
        //t元素也包含字符串
        if (isTElement) {
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            isTElement = false;
            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        } else if ("v".equals(name)) {
            String value = lastContents.trim();
            value = value.equals("") ? "" : value;
            //日期格式处理
           /*if(dateFlag){
                 Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
                 SimpleDateFormat dateFormat = new SimpleDateFormat(
                 "dd/MM/yyyy");
                 value = dateFormat.format(date);
            }*/
            //数字类型处理
//            if(numberFlag){
//                BigDecimal bd = new BigDecimal(value);
//                value = bd.setScale(3,BigDecimal.ROUND_UP).toString();
//            }
            //当某个单元格的数据为空时，其后边连续的单元格也可能为空
            if (isSkipCeil == true) {
                for (int i = 0; i < (flag - 1); i++) {
                    rowlist.add(curCol + i, "");
                }
                curCol += (flag - 1);
            }
            rowlist.add(curCol, value);
            curCol++;
        } else {
            //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                if (curRow == 0) {
                    maxRef = currXy;
                }
                if (maxRef != null) {
                    int len = twentyToDecimai(maxRef.replaceAll("\\d", "")) - twentyToDecimai(currXy.replaceAll("\\d", ""));
                    for (int i = 0; i < len; i++) {
                        rowlist.add(curCol, "");
                        curCol++;
                    }
                }
                try {
                    getRows(sheetIndex, curRow, rowlist);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                rowlist.clear();
                curRow++;
                curCol = 0;
            }
        }

    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //得到单元格内容的值
        lastContents += new String(ch, start, length);
    }


    /**
     * 获取行数据回调
     *
     * @param sheetIndex
     * @param curRow
     * @param rowList
     */
    public abstract void getRows(int sheetIndex, int curRow, List<String> rowList);

    /**
     * 测试方法
     */
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        String file = "D:\\work\\数据模型\\资金\\假HP第一批\\陶国龙、梁明霞、张仁蛟、钟高艺\\交易明细信息 (26).xlsx";
        final Map<String,Integer> title=new HashMap();
        final List<BankZzxxEntity> listB = new ArrayList<>();
//
//        Set<String> temp = new HashSet<>();
        final StringBuffer temp = new StringBuffer();
        ExcelReader reader = new ExcelReader() {
            public void getRows(int sheetIndex, int curRow, List<String> rowList) {

                if(rowList.get(0).contains(":")){
                    temp.delete(0,temp.length());
                    temp.append(rowList.get(0).split(":")[1]);
                }
                if(rowList.size()==20&&!rowList.get(0).contains("序号")){
                    BankZzxxEntity zz = new BankZzxxEntity();
                    zz.setJysj(TimeFormatUtil.getDateSwitchTimestamp(rowList.get(1)));
                    if(temp.equals(rowList.get(3))){
                        zz.setYhkkh(rowList.get(3));
                        zz.setJyxm(rowList.get(4));
                        zz.setJyzjh(rowList.get(5));
                        zz.setDskh(rowList.get(7));
                        zz.setDsxm(rowList.get(8));
                        zz.setDssfzh(rowList.get(9));
                        zz.setSfbz("出");
                    }else{
                        zz.setYhkkh(rowList.get(7));
                        zz.setJyxm(rowList.get(8));
                        zz.setJyzjh(rowList.get(9));
                        zz.setDskh(rowList.get(3));
                        zz.setDsxm(rowList.get(4));
                        zz.setDssfzh(rowList.get(5));
                        zz.setSfbz("进");
                    }
                    zz.setJyje(new BigDecimal(rowList.get(12)));
                    zz.setJyfsd(rowList.get(15));
                    zz.setBz(rowList.get(16));
                    System.out.println(zz);
                    listB.add(zz);
                }
            }
        };
//
//        reader.processOneSheet(file,1);
        reader.processOneSheet(file,2);
        System.out.println(listB.size());
        long end = System.currentTimeMillis();
        System.out.println(end - start);

    }
}