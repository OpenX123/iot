package com.openx.mqtt_subsribe.controller;

import com.openx.mqtt_subsribe.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileUploadController {

    private static final String UPLOAD_DIR = "D:/upload/";

    // 处理/upload请求，接收并保存上传的文件
    @PostMapping("/upload")
    @ResponseBody
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传失败，请选择文件");
        }
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
        // 获取文件类型
        String contentType = file.getContentType();
        // 获取文件大小
        long size = file.getSize();
        // 生成一个新的文件名，避免重复
        String newFileName = UUID.randomUUID() + "." + getExtension(fileName);
        // 创建文件保存路径
        File dest = new File(UPLOAD_DIR + newFileName);
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        try {
            // 将上传的文件内容写入到目标文件中
            file.transferTo(dest);
            // 读取并解析文件内容
            String fileContent = parseFile(dest);
            log.info(fileContent);
            return Result.success("上传成功，文件名：" + fileName + "，文件类型：" + contentType + "，文件大小：" + size + "，新文件名：" + newFileName + "，文件内容：" + fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String parseFile(File file) {
        String extension = getExtension(file.getName());
        StringBuilder content = new StringBuilder();
        try {
            switch (extension) {
                case "csv":
                    Reader csvReader = Files.newBufferedReader(Paths.get(file.getPath()));
                    Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
                    for (CSVRecord csvRecord : csvRecords) {
                        content.append("CSV Record: ").append(csvRecord).append("\n");
                    }
                    break;
                case "xlsx":
                    FileInputStream xlsxFile = new FileInputStream(file);
                    Workbook workbook = new XSSFWorkbook(xlsxFile);
                    Sheet sheet = workbook.getSheetAt(0);
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        Iterator<Cell> cellIterator = row.cellIterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            switch (cell.getCellType()) {
                                case STRING:
                                    content.append(cell.getStringCellValue()).append("\t");
                                    break;
                                case NUMERIC:
                                    content.append(cell.getNumericCellValue()).append("\t");
                                    break;
                                default:
                                    break;
                            }
                        }
                        content.append("\n");
                    }
                    workbook.close();
                    break;
                case "txt":
                    BufferedReader txtReader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = txtReader.readLine()) != null) {
                        content.append("[]").append(line).append("\n");
                    }
                    txtReader.close();
                    break;
                default:
                    content.append("Unsupported file type: ").append(extension);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
