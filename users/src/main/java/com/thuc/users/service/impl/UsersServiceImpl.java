package com.thuc.users.service.impl;

import com.thuc.users.converter.UsersConverter;
import com.thuc.users.dto.requestDto.FilterUserDto;
import com.thuc.users.dto.requestDto.ResetPasswordDto;
import com.thuc.users.dto.requestDto.RoomChatsDto;
import com.thuc.users.dto.requestDto.UserRequestDto;
import com.thuc.users.dto.responseDto.PageResponseDto;
import com.thuc.users.dto.responseDto.StatisticVisitByMonth;
import com.thuc.users.dto.responseDto.SuccessResponseDto;
import com.thuc.users.dto.responseDto.UserDto;
import com.thuc.users.entity.RoleEntity;
import com.thuc.users.entity.UserEntity;
import com.thuc.users.entity.UserVisits;
import com.thuc.users.exception.ResourceAlreadyException;
import com.thuc.users.exception.ResourceNotFoundException;
import com.thuc.users.repository.RoleRepository;
import com.thuc.users.repository.UserRepository;
import com.thuc.users.repository.UserVisitRepository;
import com.thuc.users.service.IKeycloakAccountService;
import com.thuc.users.service.IKeycloakService;
import com.thuc.users.service.IUsersService;
import com.thuc.users.service.client.RoomChatsFeignClient;
import com.thuc.users.utils.RandomString;
import com.thuc.users.utils.RoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements IUsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IKeycloakAccountService keycloakAccountService;
    private final IKeycloakService keycloakService;
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    private final StreamBridge streamBridge;
    private final RoomChatsFeignClient  roomChatsFeignClient;
    private final UserVisitRepository userVisitRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public UserDto createUser(UserRequestDto user) {
        logger.debug("Creating a new user");
        UserEntity existUser = userRepository.findByEmail(user.getEmail());
        if(existUser != null) {
            throw new ResourceAlreadyException("User","email",user.getEmail());
        }
        boolean checked= keycloakAccountService.createUser(user);
        if(checked){
            RoleEntity roleUser = roleRepository.findByName(RoleEnum.USER.getValue());
            UserEntity userEntity = UsersConverter.toUserEntity(user);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setRoles(List.of(roleUser));
            userEntity.setAvatar(user.getAvatar());
            UserEntity saveUser= userRepository.save(userEntity);
            sendCommunication(user);
            return UsersConverter.toUserDto(saveUser);
        }
        throw new RuntimeException("create user fail");
    }
    @Override
    public UserDto createStaff(UserRequestDto user) {
        logger.debug("Creating a new staff");
        UserEntity existUser = userRepository.findByEmail(user.getEmail());
        if(existUser != null) {
            throw new ResourceAlreadyException("User","email",user.getEmail());
        }
        List<RoleEntity> roles = user.getRoleIds().stream().map(id->{
            return roleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Role","id",String.valueOf(id)));
        }).toList();
        boolean checked= keycloakAccountService.createStaff(user,roles);

        if(checked){
            UserEntity userEntity = UsersConverter.toUserEntity(user);
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
            userEntity.setRoles(roles);
            userEntity.setAvatar(user.getAvatar());
            UserEntity saveUser= userRepository.save(userEntity);
            sendCommunication(user);
            return UsersConverter.toUserDto(saveUser);
        }
        throw new RuntimeException("create staff fail");
    }
    private void sendCommunication(UserRequestDto user) {
        logger.debug("Sending communication with {}",user);
        var result = streamBridge.send("sendCommunication-out-0",user);
        logger.debug("Received communication with {}",result);
    }

    @Override
    public Map<String,String> getToken(String code) {
        return keycloakService.getToken(code);
    }

    @Override
    public Map<String, String> getAccessTokenByRefresh() {
        return keycloakService.getAccessTokenByRefresh();
    }

    @Override
    public void logout() {
        keycloakService.logout();
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null) {
            throw new ResourceNotFoundException("User","email", email);
        }
        return UsersConverter.toUserDto(userEntity);
    }

    @Override
    public UserDto getInfoUserById(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User","id", String.valueOf(id)));
        return UsersConverter.toUserDto(user);
    }

    @Override
    public RoomChatsDto createRoomChats(RoomChatsDto roomChats) {
        SuccessResponseDto<RoomChatsDto> response = roomChatsFeignClient.createRoomChat(roomChats).getBody();
        return response.getData();
    }

    @Override
    public int updateUserVisits(Integer userId) {
        UserVisits userVisits = UserVisits.builder()
                .accessedAt(LocalDateTime.now())
                .build();
        if(userId!=null) {
            userVisits.setUserId(userId);
        }
        userVisitRepository.save(userVisits);
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return (int) userVisitRepository.countByAccessedAt(startOfDay,endOfDay);
    }

    @Override
    public Integer getAmountVisits() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return (int) userVisitRepository.countByAccessedAt(startOfDay,endOfDay);
    }

    @Override
    public Integer getAmountUsers() {
        return (int)userRepository.count();
    }

    @Override
    public List<StatisticVisitByMonth> getAmountVisitsByMonth(Integer month) {
        List<StatisticVisitByMonth> result = new ArrayList<>();
        Year currentYear = Year.now();
        YearMonth currentMonth = currentYear.atMonth(month);
        int daysInMonth = currentMonth.lengthOfMonth();
        for(int i=1;i<=daysInMonth;i++){
            LocalDateTime startOfDay = LocalDateTime.of(currentYear.getValue(),month,i,0,0,0);
            LocalDateTime endOfDay = LocalDateTime.of(currentYear.getValue(),month,i,23,59,59,999_999_999);
            int total=userVisitRepository.countByAccessedAt(startOfDay,endOfDay);
            result.add(new StatisticVisitByMonth(i,total));
        }
        return result;
    }

    @Override
    public List<UserDto> getAllUsersAdmin() {
        List<RoleEntity> roles = roleRepository.findAll();
        Integer roleId = roles.stream().filter(role->role.getName().equals("USER")).findFirst().get().getId();
        List<UserEntity> users = userRepository.findByRolesNotContainUser(roleId);
        return users.stream().map(UsersConverter::toUserDto).collect(Collectors.toList());
    }

    @Override
    public PageResponseDto<List<UserDto>> getAllUsersByPage(FilterUserDto filterDto) throws ParseException {
        StringBuilder builder = new StringBuilder(" SELECT * FROM users u WHERE 1=1 ");
        Map<String,Object> params = new HashMap<>();
        int pageNo = filterDto.getPageNo();
        int pageSize = filterDto.getPageSize();
        if(filterDto.getRoleId()!=null && filterDto.getRoleId()!=0) {
            builder.append(" AND EXISTS (SELECT 1 FROM user_roles ur WHERE ur.role_id=:roleId AND ur.user_id=u.id )");
            params.put("roleId", filterDto.getRoleId());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!filterDto.getTimeOption().equals("0")){
            builder.append(" AND created_at >=:beginDate AND created_at <=:endDate");
            LocalDate now = LocalDate.now();
            switch (filterDto.getTimeOption()) {
                case "custom":{
                    Date beginDate = sdf.parse(filterDto.getStartDate());
                    Date endDate = sdf.parse(filterDto.getEndDate());
                    params.put("beginDate",beginDate);
                    params.put("endDate",endDate);
                    break;
                }
                case "today": {
                    LocalDate today = LocalDate.now();
                    params.put("beginDate", today.atStartOfDay());
                    params.put("endDate", today.plusDays(1).atStartOfDay());
                    break;
                }
                case "yesterday": {
                    LocalDate yesterday = LocalDate.now().minusDays(1);
                    params.put("beginDate", yesterday.atStartOfDay());
                    params.put("endDate", yesterday.plusDays(1).atStartOfDay());
                    break;
                }
                case "last_7_days": {
                    params.put("beginDate", now.minusDays(7).atTime(LocalTime.MIN));
                    params.put("endDate", now.atTime(LocalTime.MAX));
                    break;
                }
                case "last_30_days": {
                    params.put("beginDate", now.minusDays(30).atTime(LocalTime.MIN));
                    params.put("endDate", now.atTime(LocalTime.MAX));
                    break;
                }
                case "this_week": {
                    LocalDate today = LocalDate.now();
                    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                    LocalDate endOfWeek = startOfWeek.plusDays(7);
                    params.put("beginDate", startOfWeek.atStartOfDay());
                    params.put("endDate", endOfWeek.atStartOfDay());
                    break;
                }
                case "this_month": {
                    LocalDate today = LocalDate.now();
                    LocalDate firstDay = today.withDayOfMonth(1);
                    LocalDate firstOfNextMonth = firstDay.plusMonths(1);
                    params.put("beginDate", firstDay.atStartOfDay());
                    params.put("endDate", firstOfNextMonth.atStartOfDay());
                    break;
                }
                case "this_year": {
                    LocalDate today = LocalDate.now();
                    LocalDate firstDay = today.withDayOfYear(1);
                    LocalDate firstOfNextYear = firstDay.plusYears(1);
                    params.put("beginDate", firstDay.atStartOfDay());
                    params.put("endDate", firstOfNextYear.atStartOfDay());
                    break;
                }
            }
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString()
                .replace("SELECT *","SELECT COUNT(*)"),Long.class);
        if(!filterDto.getSortOption().equals("0")){
            switch (filterDto.getSortOption()) {
                case "last_name_desc":
                    builder.append(" ORDER BY last_name DESC, id ASC ");
                    break;
                case "last_name_asc":
                    builder.append(" ORDER BY last_name ASC, id ASC ");
                    break;
                case "date_desc":
                    builder.append(" ORDER BY created_at DESC, id ASC ");
                    break;
                case "date_asc":
                    builder.append(" ORDER BY created_at ASC, id ASC ");
                    break;
                default:
                    break;
            }
        }
        else{
            builder.append(" ORDER BY id ASC");
        }
        int limit = pageSize;
        int offset= (pageNo-1)*pageSize;
        Query query = entityManager.createNativeQuery(builder.toString(),UserEntity.class);
        query.setFirstResult(offset).setMaxResults(limit);
        params.forEach(query::setParameter);
        params.forEach(queryTotal::setParameter);
        List<UserEntity> users = query.getResultList();
        List<UserDto> userDtos=users.stream().map(UsersConverter::toUserDto).toList();
        Long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<UserDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(userDtos)
                .build();
    }

    @Override
    public PageResponseDto<List<UserDto>> getSearchUsers(String keyword, Integer pageNo, Integer pageSize) {
        StringBuilder builder = new StringBuilder("SELECT * FROM users WHERE 1=1 ");
        if(keyword!=null && !keyword.isEmpty()){
            builder.append(" AND (unaccent(first_name) ILIKE unaccent(:keyword)");
            builder.append(" OR unaccent(last_name) ILIKE unaccent(:keyword)");
            builder.append(" OR unaccent(email) ILIKE unaccent(:keyword)");
            builder.append(" OR unaccent(CAST(id AS TEXT)) ILIKE unaccent(:keyword))");
        }
        Query queryTotal = entityManager.createNativeQuery(builder.toString()
                .replace("SELECT *","SELECT COUNT(*)"),Long.class);
        builder.append(" ORDER BY id ASC");
        Query query = entityManager.createNativeQuery(builder.toString(),UserEntity.class);
        if(keyword!=null && !keyword.isEmpty()){
            query.setParameter("keyword",'%'+ keyword+'%');
            queryTotal.setParameter("keyword", '%'+keyword+'%');
        }
        query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize);
        List<UserEntity> users = query.getResultList();
        List<UserDto> userDtos=users.stream().map(UsersConverter::toUserDto).toList();
        long total = ((Number)queryTotal.getSingleResult()).longValue();
        return PageResponseDto.<List<UserDto>>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .total(total)
                .dataPage(userDtos)
                .build();
    }
    private UserEntity getUserById(Integer id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User","id",String.valueOf(id)));
    }
    @Modifying
    @Transactional
    @Override
    public UserDto updateRolesByUser(Integer id, List<Integer> roleIds) {
        UserEntity existUser = getUserById(id);
        boolean update = keycloakAccountService.updateRoleByUser(existUser,roleIds);
        if(update){
            List<RoleEntity> roleEntities = roleRepository.findAllById(roleIds);
            existUser.getRoles().clear();
            existUser.getRoles().addAll(roleEntities);
            UserEntity savedUser = userRepository.save(existUser);
            return UsersConverter.toUserDto(savedUser);
        }
        return  UsersConverter.toUserDto(existUser);
    }
    @Override
    public UserDto resetPassword(Integer id) {
        UserEntity user = getUserById(id);
        String passwordRaw = RandomString.generateRandomString(6);
        boolean checked=keycloakAccountService.resetPassword(user,passwordRaw);
        if(checked){
            ResetPasswordDto resetPasswordDto = new ResetPasswordDto(user.getEmail(),passwordRaw);
            var result = streamBridge.send("sendResetPassword-out-0",resetPasswordDto);
            logger.debug("Received sendResetPassword-out-0 with {}",result);
            String passwordEncode = passwordEncoder.encode(passwordRaw);
            user.setPassword(passwordEncode);
            System.out.println("Encoded password: " + passwordEncode + " (length = " + passwordEncode.length() + ")");
            userRepository.updatePasswordById(id,passwordEncode);
            return UsersConverter.toUserDto(user);
        }
        throw new RuntimeException("Reset Password Failed");
    }


}
