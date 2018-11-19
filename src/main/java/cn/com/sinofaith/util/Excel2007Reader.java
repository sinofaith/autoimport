package cn.com.sinofaith.util;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.com.sinofaith.bean.bankBean.BankZzxxEntity;
import cn.com.sinofaith.bean.wlBean.WuliuEntity;
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

/**
 *  会忽略空单元格问题  解决类
 */
public abstract class Excel2007Reader extends DefaultHandler {
    //共享字符串表
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
        pkg.flush();
        pkg.close();
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
            char pre;
            char curr;
            if(preX.length() == 2) {
                pre = preX.charAt(1);
            } else {
                pre = preX.charAt(0);
            }
            if(currX.length() == 2) {
                curr = currX.charAt(1);
            } else {
                curr = currX.charAt(0);
            }
            flag = curr - pre;
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

    /**
     * 获取行数据回调
     * @param sheetIndex
     * @param curRow
     * @param rowList
     */
    public abstract void getRows(int sheetIndex, int curRow, List<String> rowList);

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

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        String file = "D:\\work\\数据模型\\物流\\第二起假表情况\\7afedb300ecdff14ed8fcae41b552f98.xlsx";
        final Map<String,Integer> title = new HashMap<>();
        final List<WuliuEntity> wls = new ArrayList<>();

        Excel2007Reader reader = new Excel2007Reader() {
            @Override
            public void getRows(int sheetIndex, int curRow, List<String> rowList) {
                // 第一行
                if (curRow == 0) {
                    for(int i =0;i<rowList.size();i++){
                        // 获取每一列的标题
                        String temp = rowList.get(i);
                        // 进行筛选
                        if (temp.contains("运单号") || temp.contains("单号")) {
                            title.put("waybill_id",i);
                        } else if (temp.contains("寄件时间") || temp.contains("寄时间")) {
                            title.put("ship_time",i);
                        } else if (temp.contains("寄件地址") || temp.contains("寄地址")) {
                            title.put("ship_address",i);
                        } else if (temp.contains("寄件人") || temp.contains("寄件联系人")) {
                            title.put("sender",i);
                        } else if (temp.contains("寄件电话") || temp.contains("寄电话")) {
                            title.put("ship_phone",i);
                        } else if (temp.contains("寄件手机") || temp.contains("寄客户编码") || temp.contains("寄方客户编码")) {
                            title.put("ship_mobilephone",i);
                        } else if (temp.contains("收件地址") || temp.contains("收地址")) {
                            title.put("sj_address",i);
                        } else if (temp.contains("收件人") || temp.contains("收件联系人")) {
                            title.put("addressee",i);
                        } else if (temp.contains("收件电话") || temp.contains("收电话")) {
                            title.put("sj_phone",i);
                        } else if (temp.contains("收件手机") || temp.contains("到客户编码") || temp.contains("派方客户编码")) {
                            title.put("sj_mobilephone",i);
                        } else if (temp.contains("收件员")) {
                            title.put("collector",i);
                        } else if (temp.contains("托寄物") || temp.contains("托寄内容") || temp.contains("托物")) {
                            title.put("tjw",i);
                        } else if (temp.contains("付款方式") || temp.contains("付款")) {
                            title.put("payment",i);
                        } else if (temp.contains("代收货款金额") || temp.contains("代收货款")) {
                            title.put("dshk",i);
                        } else if (temp.contains("计费重量") || temp.contains("重量")) {
                            title.put("weight",i);
                        } else if (temp.contains("件数")) {
                            title.put("number_cases",i);
                        } else if (temp.contains("运费") || temp.contains("费用")) {
                            title.put("freight",i);
                        }
                    }
                } else {
                    WuliuEntity wl = WuliuEntity.listToObj(rowList, title);
                    if(wl!=null){
                        wls.add(wl);
                    }
                }
            }
        };

        reader.process(file);
        System.out.println(wls.size());
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }

}

