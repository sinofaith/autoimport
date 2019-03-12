package cn.com.sinofaith.util;

import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
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
    //是否跳过了单元格
    private boolean isSkipCeil = false;

    private boolean isTElement;
    //两个不为空的单元格之间隔了多少个空的单元格
    private int flag = 0;

    /**只遍历一个电子表格，其中sheetId为要遍历的sheet索引，从1开始，1-3
     * @param filename
     * @param sheetId
     * @throws Exception
     */
    public void processOneSheet(String filename,int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId"+sheetId);
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     * 遍历工作簿中所有的电子表格
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
        for (int i = charArray.length - 1 ; i >= 0; i--) {
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
            if ("1".equals(cellDateType)){
                dateFlag = true;
            } else {
                dateFlag = false;
            }
            String cellNumberType = attributes.getValue("s");
            if("2".equals(cellNumberType)){
                numberFlag = true;
            } else {
                numberFlag = false;
            }
            //与判断空单元格有关
            isSkipCeil = false;
            String cellXy =  attributes.getValue("r");
            if("".equals(preXy)) {
                preXy = cellXy;
            }
            currXy = cellXy;
            preX = preXy.replaceAll("\\d", "").trim();
            currX = currXy.replaceAll("\\d", "").trim();

            flag = twentyToDecimai(currX) - twentyToDecimai(preX);

            if(flag != 0 && flag != 1 && flag > 0) {
                isSkipCeil = true;
            }
            preXy = cellXy;
        }
        //当元素为t时
        if("t".equals(name)){
            isTElement = true;
        } else {
            isTElement = false;
        }

        // 置空
        lastContents = "";
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
        if(isTElement){
            String value = lastContents.trim();
            rowlist.add(curCol, value);
            curCol++;
            isTElement = false;
            // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
            // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        } else if ("v".equals(name)) {
            String value = lastContents.trim();
            value = value.equals("")?" ":value;
            //日期格式处理
           /*if(dateFlag){
                 Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
                 SimpleDateFormat dateFormat = new SimpleDateFormat(
                 "dd/MM/yyyy");
                 value = dateFormat.format(date);
            }*/
            //数字类型处理
            if(numberFlag){
                BigDecimal bd = new BigDecimal(value);
                value = bd.setScale(3,BigDecimal.ROUND_UP).toString();
            }
            //当某个单元格的数据为空时，其后边连续的单元格也可能为空
            if(isSkipCeil == true) {
                for(int i = 0; i < (flag-1); i++) {
                    rowlist.add(curCol + i, "");
                }
                curCol += (flag-1);
            }
            rowlist.add(curCol, value);
            curCol++;
        }else {
            //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                try {
                    getRows(sheetIndex,curRow,rowlist);
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
        String file = "D:\\work\\数据模型\\资金\\柏顺福资金\\账户交易明细表.xlsx";
        final Map<String,Integer> title=new HashMap();
        final List<BankZzxxEntity> listB = new ArrayList<>();

        Excel2007Reader reader = new Excel2007Reader() {
            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                if(curRow==0){
                    for(int i = 0;i<rowList.size(); i++){
                        String temp = rowList.get(i);
                        if(temp.contains("交易账卡号")){
                            title.put("yhkkh",i);
                        }else if(temp.contains("交易账号")){
                            title.put("yhkzh",i);
                        }else if(rowList.get(i).contains("交易户名")){
                            title.put("jyxm",i);
                        }else if(temp.contains("交易证件号")){
                            title.put("jyzjh",i);
                        }else if(temp.contains("交易日期")){
                            title.put("jysj",i);
                        }else if(temp.contains("交易金额")){
                            title.put("jyje",i);
                        }else if(temp.contains("交易余额")&&!temp.contains("对手")){
                            title.put("jyye",i);
                        }else if(temp.contains("收付标志")){
                            title.put("sfbz",i);
                        }else if(temp.contains("对手账号")){
                            title.put("dskh",i);
                        }else if(temp.contains("对手卡号")){
                            title.put("dszh",i);
                        }else if(temp.contains("对手户名")){
                            title.put("dsxm",i);
                        }else if(temp.contains("对手身份证号")){
                            title.put("dssfzh",i);
                        }else if(temp.contains("对手开户银行")){
                            title.put("dskhh",i);
                        }else if(temp.contains("摘要说明")){
                            title.put("zysm",i);
                        }else if(temp.contains("交易网点名称")){
                            title.put("jywdmc",i);
                        }else if(temp.contains("交易发生地")){
                            title.put("jyfsd",i);
                        }else if(temp.contains("交易是否成功")){
                            title.put("jysfcg",i);
                        }else if(temp.contains("对手交易余额")){
                            title.put("dsjyye",i);
                        }else if(temp.contains("对手余额")){
                            title.put("dsye",i);
                        }else if(temp.contains("备注")){
                            title.put("bz",i);
                        }
                    }
                }
                if(!rowList.get(0).equals("交易账卡号")) {
                    System.out.println(rowList);
                    listB.add(BankZzxxEntity.listToObj(rowList, title));
                }
            }
        };

        reader.process(file);
        Set<BankZzxxEntity> setB = new HashSet<>(listB);
        System.out.println(listB.size());
        System.out.println(setB.size());
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
