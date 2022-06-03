package com.by.gomel.gstu.excel;

import com.by.gomel.gstu.model.Detail;
import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.OrdersDetail;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.viewModel.SailedDetailsViewModel;
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

            formOrderReport(orders, user, workbook, fos, sheet);
        }
    }

    public static void saveDetailsSalesReport(List<SailedDetailsViewModel> sailedDetailsViewModels, User user) throws IOException {
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
            orderCost.setCellValue("Количество");

            for(int i = 0; i < sailedDetailsViewModels.size(); i++){
                Row dataRow = sheet.createRow(i + 3);

                var currentEntity = sailedDetailsViewModels.get(i);

                Cell detailName = dataRow.createCell(0);
                detailName.setCellValue(currentEntity.getDetail().getDetailName() + " " + currentEntity.getDetail().getDetailVendorCode());

                Cell detailDescription = dataRow.createCell(1);
                detailDescription.setCellValue(currentEntity.getDetail().getDetailDescription());

                Cell detailsCount = dataRow.createCell(2);
                detailsCount.setCellValue(currentEntity.getDetailsCount());


            }

            reportFooter(user, workbook, fos, sheet, sailedDetailsViewModels.size());
        }
    }

    public static void saveEmployeePeriodReport(List<Order> orders, User user) throws IOException {
        try (Workbook workbook = new HSSFWorkbook();
             var fos = new FileOutputStream(dir + "/reports/employeePeriod/" + Math.pow(LocalDateTime.now().hashCode(), 2) + ".xls")) {
            Sheet sheet = workbook.createSheet("Заказы");

            Row headerRow = sheet.createRow(0);

            Cell headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Заказы сотрудника, сформированные в период между указанными датами");

            formOrderReport(orders, user, workbook, fos, sheet);
        }
    }

    private static void reportFooter(User user, Workbook workbook, FileOutputStream fos, Sheet sheet, int size) throws IOException {
        Row footerRow = sheet.createRow(size + 5);

        Cell userCell = footerRow.createCell(0);
        userCell.setCellValue("ФИО составителя отчёта: " + user.getFio());

        Cell assigneeCell = footerRow.createCell(1);
        assigneeCell.setCellValue("Подпись : ");

        workbook.write(fos);
    }

    private static void formOrderReport(List<Order> orders, User user, Workbook workbook, FileOutputStream fos, Sheet sheet) throws IOException {
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

            var currentEntity = orders.get(i);

            Cell customerFioValue = dataRow.createCell(0);
            customerFioValue.setCellValue(currentEntity.getUser().getFio());

            Cell employeeFioValue = dataRow.createCell(1);
            employeeFioValue.setCellValue(currentEntity.getEmployee().getFio());

            Cell orderCostValue = dataRow.createCell(2);
            orderCostValue.setCellValue(currentEntity.getOrderCost());

            Cell orderCostWithDiscountValue = dataRow.createCell(3);
            orderCostWithDiscountValue.setCellValue(currentEntity.getOrderCost() - currentEntity.getDiscount());

            DataFormat format = workbook.createDataFormat();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));

            Cell orderDateValue = dataRow.createCell(4);
            orderDateValue.setCellStyle(dateStyle);
            orderDateValue.setCellValue(String.valueOf(currentEntity.getCreated().toLocalDate()));

            Cell issueDateValue = dataRow.createCell(5);
            issueDateValue.setCellStyle(dateStyle);
            issueDateValue.setCellValue(String.valueOf(currentEntity.getPlannedIssueDate()));

        }

        reportFooter(user, workbook, fos, sheet, orders.size());
    }
}
