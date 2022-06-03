package com.by.gomel.gstu.excel;

import com.by.gomel.gstu.model.Detail;
import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ExcelSaver {

    private final static File dir = new File(System.getProperty("user.dir"));

    public static void savePeriodOrderReport(List<Order> orders, User user) throws IOException {
        try(Workbook workbook = new HSSFWorkbook();
            var fos = new FileOutputStream(dir + "/reports/orderByDateReports/" + Math.pow(LocalDateTime.now().hashCode(), 2) + ".xls")){
            Sheet sheet = workbook.createSheet("Заказы");

            Row headerRow = sheet.createRow(0);

            Cell headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Заказы, сформированные в период между указанными датами");

            Row row = sheet.createRow(2);

            Cell customerFio = row.createCell(0);
            customerFio.setCellValue("ФИО покупателя");

            Cell employeeFio = row.createCell(1);
            employeeFio.setCellValue("ФИО продавца");

            Cell orderCost = row.createCell(2);
            orderCost.setCellValue("Стоимость заказа(без скидки)");

            Cell orderCostWithDiscount = row.createCell(3);
            orderCostWithDiscount.setCellValue("Стоимость заказа(со скидкой)");

            Cell orderDate = row.createCell(4);
            orderDate.setCellValue("Дата формирования заказа");

            Cell issueDate = row.createCell(5);
            issueDate.setCellValue("Дата выдачи заказа");

            for(int i = 0; i < orders.size(); i++){
                Row dataRow = sheet.createRow(i + 3);

                Cell customerFioValue = dataRow.createCell(0);
                customerFioValue.setCellValue(orders.get(i).getUser().getFio());

                Cell employeeFioValue = dataRow.createCell(1);
                employeeFioValue.setCellValue(orders.get(i).getEmployee().getFio());

                Cell orderCostValue = dataRow.createCell(2);
                orderCostValue.setCellValue(orders.get(i).getOrderCost());

                Cell orderCostWithDiscountValue = dataRow.createCell(3);
                orderCostWithDiscountValue.setCellValue(orders.get(i).getOrderCost() - orders.get(i).getDiscount());

                DataFormat format = workbook.createDataFormat();
                CellStyle dateStyle = workbook.createCellStyle();
                dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

                Cell orderDateValue = dataRow.createCell(4);
                orderDateValue.setCellStyle(dateStyle);
                orderDateValue.setCellValue(String.valueOf(orders.get(i).getCreated().toLocalDate()));

                Cell issueDateValue = dataRow.createCell(5);
                issueDateValue.setCellStyle(dateStyle);
                issueDateValue.setCellValue(String.valueOf(orders.get(i).getPlannedIssueDate()));

            }

            Row footerRow = sheet.createRow(orders.size() + 5);

            Cell userCell = footerRow.createCell(0);
            userCell.setCellValue("ФИО составителя отчёта: " + user.getFio());

            Cell assigneeCell = footerRow.createCell(1);
            assigneeCell.setCellValue("Подпись : ");

            workbook.write(fos);
        }
    }

    public static void saveDetailsSalesReport(List<Detail> details, User user) throws IOException {
        try(Workbook workbook = new HSSFWorkbook();
            var fos = new FileOutputStream(dir + "/reports/detailsSalesByPeriod/" + Math.pow(LocalDateTime.now().hashCode(), 2) + ".xls")){
            Sheet sheet = workbook.createSheet("Детали");

            Row headerRow = sheet.createRow(0);

            Cell headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Детали, проданные в период между указанными датами");

            Row row = sheet.createRow(2);

            Cell customerFio = row.createCell(0);
            customerFio.setCellValue("Имя детали");

            Cell employeeFio = row.createCell(1);
            employeeFio.setCellValue("Описание");

            Cell orderCost = row.createCell(2);
            orderCost.setCellValue("Количство");

            for(int i = 0; i < orders.size(); i++){
                Row dataRow = sheet.createRow(i + 3);

                Cell customerFioValue = dataRow.createCell(0);
                customerFioValue.setCellValue(orders.get(i).getUser().getFio());

                Cell employeeFioValue = dataRow.createCell(1);
                employeeFioValue.setCellValue(orders.get(i).getEmployee().getFio());

                Cell orderCostValue = dataRow.createCell(2);
                orderCostValue.setCellValue(orders.get(i).getOrderCost());

                Cell orderCostWithDiscountValue = dataRow.createCell(3);
                orderCostWithDiscountValue.setCellValue(orders.get(i).getOrderCost() - orders.get(i).getDiscount());

                DataFormat format = workbook.createDataFormat();
                CellStyle dateStyle = workbook.createCellStyle();
                dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

                Cell orderDateValue = dataRow.createCell(4);
                orderDateValue.setCellStyle(dateStyle);
                orderDateValue.setCellValue(String.valueOf(orders.get(i).getCreated().toLocalDate()));

                Cell issueDateValue = dataRow.createCell(5);
                issueDateValue.setCellStyle(dateStyle);
                issueDateValue.setCellValue(String.valueOf(orders.get(i).getPlannedIssueDate()));

            }

            Row footerRow = sheet.createRow(orders.size() + 5);

            Cell userCell = footerRow.createCell(0);
            userCell.setCellValue("ФИО составителя отчёта: " + user.getFio());

            Cell assigneeCell = footerRow.createCell(1);
            assigneeCell.setCellValue("Подпись : ");

            workbook.write(fos);
        }
    }
}
