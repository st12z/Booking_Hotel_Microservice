package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.FilterDtoManage;
import com.thuc.rooms.dto.PageResponseDto;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.repository.PropertyRepository;
import com.thuc.rooms.repository.RoomTypeRepository;
import com.thuc.rooms.service.IExportFileService;
import com.thuc.rooms.service.IRedisPropertyService;
import com.thuc.rooms.service.client.BillsFeignClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportFileServiceImpl implements IExportFileService {
    private final PropertyRepository propertyRepository;
    private final CityRepository cityRepository;
    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
    private final IRedisPropertyService redisPropertyService;
    private final RoomTypeRepository roomTypeRepository;
    private final BillsFeignClient billsFeignClient;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void generateExcelOfPropertyRevenue(HttpServletResponse response) {

        try{
            StringBuilder builder = new StringBuilder("SELECT * FROM properties p  ");
            Query query = entityManager.createNativeQuery(builder.toString(), Property.class);
            List<Property> properties = query.getResultList();
            List<Integer> propertyIds = properties.stream().map(Property::getId).toList();
            Map<Integer,Integer> billMap = billsFeignClient.getBillByPropertyIds(propertyIds).getBody().getData();
            Map<Integer,Integer> revenueMap = billsFeignClient.getRevenueByPropertyIds(propertyIds).getBody().getData();
            List<PropertyDto> propertiesDtos = properties.stream().map(item->{
                int propertyId = item.getId();
                PropertyDto propertyDto = PropertyConverter.toPropertyDto(item);
                propertyDto.setTotalBills(billMap.get(propertyId));
                propertyDto.setTotalPayments(revenueMap.get(propertyId));
                return propertyDto;
            }).collect(Collectors.toList());
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("Properties Revenue Info");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Property type");
            row.createCell(3).setCellValue("Rating");
            row.createCell(4).setCellValue("Total Bills");
            row.createCell(5).setCellValue("Total Revenue");
            int dataRowIndex = 1;
            for(PropertyDto propertyDto:propertiesDtos){
                HSSFRow dataRow = sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(propertyDto.getId());
                dataRow.createCell(1).setCellValue(propertyDto.getName());
                dataRow.createCell(2).setCellValue(propertyDto.getPropertyType());
                dataRow.createCell(3).setCellValue(propertyDto.getRatingStar());
                dataRow.createCell(4).setCellValue(propertyDto.getTotalBills());
                dataRow.createCell(5).setCellValue(propertyDto.getTotalPayments());
                dataRowIndex++;
            }
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
