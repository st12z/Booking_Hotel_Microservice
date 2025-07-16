package com.thuc.rooms.service.impl;

import com.thuc.rooms.converter.PropertyConverter;
import com.thuc.rooms.dto.PropertyDto;
import com.thuc.rooms.entity.City;
import com.thuc.rooms.entity.Property;
import com.thuc.rooms.entity.Trip;
import com.thuc.rooms.entity.TripType;
import com.thuc.rooms.repository.CityRepository;
import com.thuc.rooms.repository.TripRepository;
import com.thuc.rooms.repository.TripTypeRepository;
import com.thuc.rooms.service.IExportFileService;
import com.thuc.rooms.service.client.BillsFeignClient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.locationtech.jts.triangulate.tri.Tri;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExportFileServiceImpl implements IExportFileService {
    private final BillsFeignClient billsFeignClient;
    private final CityRepository cityRepository;
    private final TripRepository tripRepository;
    private final TripTypeRepository tripTypeRepository;
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

    @Override
    public void generateExcelOfCities(HttpServletResponse response) {
        try{
            List<City> cities = cityRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("List of Cities Info");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Image");
            row.createCell(3).setCellValue("Slug");
            row.createCell(4).setCellValue("Latitude Center");
            row.createCell(5).setCellValue("Longitude Center");
            row.createCell(6).setCellValue("Created Date");
            int dataRowIndex = 1;
            for(City item:cities){
                HSSFRow dataRow = sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(item.getId());
                dataRow.createCell(1).setCellValue(item.getName());
                dataRow.createCell(2).setCellValue(item.getImage());
                dataRow.createCell(3).setCellValue(item.getSlug());
                dataRow.createCell(4).setCellValue(String.valueOf(item.getLatitudeCenter()));
                dataRow.createCell(5).setCellValue(String.valueOf(item.getLongitudeCenter()));
                dataRow.createCell(6).setCellValue(item.getCreatedAt());
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

    @Override
    public void generateExcelOfTrips(HttpServletResponse response) {
        try{
            List<Trip> trips = tripRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("List of Trips");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Trip Type");
            row.createCell(3).setCellValue("City ID");
            row.createCell(4).setCellValue("Latitude");
            row.createCell(5).setCellValue("Longitude");
            row.createCell(6).setCellValue("Image");
            row.createCell(7).setCellValue("CreatedAt");
            int dataRowIndex = 1;
            for(Trip item:trips){
                HSSFRow dataRow = sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(item.getId());
                dataRow.createCell(1).setCellValue(item.getName());
                dataRow.createCell(2).setCellValue(item.getTripType());
                dataRow.createCell(3).setCellValue(item.getCity().getId());
                dataRow.createCell(4).setCellValue(item.getLatitude().toString());
                dataRow.createCell(5).setCellValue(item.getLongitude().toString());
                dataRow.createCell(6).setCellValue(item.getImage());
                dataRow.createCell(7).setCellValue(item.getCreatedAt());
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

    @Override
    public void generateExcelOfTripTypes(HttpServletResponse response) {
        try{
            List<TripType> tripTypes = tripTypeRepository.findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet= workbook.createSheet("List of TripTypes");
            HSSFRow row=sheet.createRow(0);
            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Image Icon");
            row.createCell(3).setCellValue("CreatedAt");
            int dataRowIndex = 1;
            for(TripType item:tripTypes){
                HSSFRow dataRow = sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(item.getId());
                dataRow.createCell(1).setCellValue(item.getName());
                dataRow.createCell(1).setCellValue(item.getIcon());
                dataRow.createCell(7).setCellValue(item.getCreatedAt());
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
