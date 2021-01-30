# POI数据有效性校验

```java
/**
* 添加数据有效性检查.
* @param sheet 要添加此检查的Sheet
* @param firstRow 开始行
* @param lastRow 结束行
* @param firstCol 开始列
* @param lastCol 结束列
* @param explicitListValues 有效性检查的下拉列表
* @throws IllegalArgumentException 如果传入的行或者列小于0(< 0)或者结束行/列比开始行/列小
*/
public static void setValidationData(Sheet sheet, int firstRow,  int lastRow,
        int firstCol,  int lastCol,String[] explicitListValues) throws IllegalArgumentException{
    if (firstRow < 0 || lastRow < 0 || firstCol < 0 || lastCol < 0 || lastRow < firstRow || lastCol < firstCol) {
        throw new IllegalArgumentException("Wrong Row or Column index : " + firstRow+":"+lastRow+":"+firstCol+":" +lastCol);
    }
    if (sheet instanceof XSSFSheet) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet)sheet);
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper
                .createExplicitListConstraint(explicitListValues);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    } else if(sheet instanceof HSSFSheet){
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(explicitListValues);
        DataValidation validation = new HSSFDataValidation(addressList, dvConstraint);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }
}  
```
