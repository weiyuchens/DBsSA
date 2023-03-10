package com.code4faster.dbssa.controller.apartment;

import com.code4faster.dbssa.common.api.ApiResponse;
import com.code4faster.dbssa.common.api.ErrorCode;
import com.code4faster.dbssa.common.api.FileType;
import com.code4faster.dbssa.common.exception.UnsupportedFileTypeException;
import com.code4faster.dbssa.common.utils.FileProcessingUtils;
import com.code4faster.dbssa.mbg.model.Apartment;
import com.code4faster.dbssa.mbg.model.ApartmentRoom;
import com.code4faster.dbssa.mbg.model.ApartmentRoomPrice;
import com.code4faster.dbssa.mbg.model.ApartmentRoomType;
import com.code4faster.dbssa.pojo.dto.QueryResultSet;
import com.code4faster.dbssa.service.ApartmentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/api/manage/apartment")
public class ApartmentController {

    @Autowired
    ApartmentService apartmentService;

    // 公寓楼功能

    /**
     * 创建公寓
     *
     * @param apartment 公寓信息
     * @return 创建结果
     */
    @PostMapping
    public ApiResponse createApartment(@RequestBody Apartment apartment) {
        String apartmentName = apartment.getApartmentName();
        if (apartmentService.isApartmentExists(apartmentName)) {
            return ApiResponse.failure(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        boolean success = apartmentService.saveApartment(apartment);
        return (success) ? ApiResponse.success() : ApiResponse.failure(ErrorCode.RESOURCE_CREATE_FAILED);
    }

    /**
     * 获取公寓列表
     *
     * @param offset 子查询每条时间序列返回数据点的偏移量，默认值是0，代表不限制返回点数量
     * @param limit  子查询每条时间序列返回数据点的最大数目，默认值也是10，代表不偏移返回的数据点
     * @return 公寓列表数据
     */
    @GetMapping
    public ApiResponse getApartmentList(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PageInfo<Apartment> apartmentPageInfo = apartmentService.listApartments(offset, limit);
        QueryResultSet queryResultSet = new QueryResultSet(apartmentPageInfo.getSize(), apartmentPageInfo.getList());
        return ApiResponse.success(queryResultSet);
    }

    /**
     * 查询某一栋公寓的信息
     *
     * @param id 公寓id
     * @return 公寓信息
     */
    @GetMapping("/{id}")
    public ApiResponse getApartmentById(@PathVariable("id") Integer id) {
        Apartment apartment = apartmentService.getApartmentById(id);
        System.out.println(apartment);
        if (apartment == null) {
            return ApiResponse.failure(ErrorCode.RESOURCE_NOT_FOUND);
        }
        return ApiResponse.success(apartment);
    }

    /**
     * 更新公寓楼信息
     *
     * @param apartment 公寓待更新信息
     * @return 无
     */
    @PutMapping
    public ApiResponse updateApartmentInfo(@RequestBody Apartment apartment) {
        Integer id = apartment.getId();
        String apartmentName = apartment.getApartmentName();
        if (!apartmentService.isApartmentNameChanged(id, apartmentName)) {
            if (apartmentService.isApartmentExists(apartmentName)) {
                return ApiResponse.failure(ErrorCode.RESOURCE_ALREADY_EXISTS);
            }
        }
        boolean success = apartmentService.updateApartment(apartment);
        return (success) ? ApiResponse.success() : ApiResponse.failure(ErrorCode.RESOURCE_UPDATE_FAILED);
    }

    // 公寓房间接口
    @PostMapping(path = "/room")
    public ApiResponse createSingleApartmentRoom(@RequestBody ApartmentRoom apartmentRoom) {
        if (!apartmentService.isApartmentRoomExists(apartmentRoom)) {
            if (apartmentService.saveApartmentRoom(apartmentRoom)) {
                return ApiResponse.success();
            } else {
                return ApiResponse.failure(ErrorCode.USER_CREATE_FAILURE);
            }
        } else {
            return ApiResponse.failure(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
    }

    @PostMapping(path = "/room/batch")
    public ApiResponse createBatchApartmentRooms(@RequestBody List<ApartmentRoom> apartmentRooms) {
        if (!apartmentService.isApartmentRoomExists(apartmentRooms.get(0))) {
            if (apartmentService.saveApartmentRoom(apartmentRooms.get(0))) {
                return ApiResponse.success();
            } else {
                return ApiResponse.failure(ErrorCode.USER_CREATE_FAILURE);
            }
        } else {
            return ApiResponse.failure(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
    }

    /**
     * 创建房间接口，支持单条数据或批量数据创建，接口接受内容包括数据传输和文件上传
     * <p>
     * // * @param apartmentRoom  单个房间数据
     * // * @param apartmentRooms 多个房间数据
     *
     * @param file 房间数据文件
     * @return 创建结果
     */
    @PostMapping(path = "/room/upload")
    public ApiResponse createBatchApartmentRoomsByUploadFile(@RequestParam(value = "file") MultipartFile file) {
        // 获取文件类型和输入流
        String filename = file.getOriginalFilename();
        if (filename != null && !filename.equals("")) {
            String fileExtension = FileProcessingUtils.getFileExtension(filename);
            System.out.println(fileExtension);
            try {
                InputStream inputStream = file.getInputStream();
                // 根据文件类型，选择解析器解析文件
                if (FileProcessingUtils.isFileTypeTrue(fileExtension, FileType.JSON_FILE)) {
                    // 处理JSON文件，批量创建房间
                    System.out.println("The file is a json file.");
                    System.out.println(filename);
                } else if (FileProcessingUtils.isFileTypeTrue(fileExtension, FileType.EXCEL_FILE)) {
                    // TODO: 处理excel文件，批量创建房间
                    System.out.println("The file is a excel file.");
                } else if (FileProcessingUtils.isFileTypeTrue(fileExtension, FileType.CSV_FILE)) {
                    // TODO: 处理CSV文件，批量创建房间
                    System.out.println("The file is a csv file.");
                } else {
                    // 不支持的文件类型
                    throw new UnsupportedFileTypeException("Unsupported File Type");
                    // throw new Exception();
                }
                // apartmentService.saveApartmentRoomWithBatchData(apartmentRooms);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return ApiResponse.success("Upload file successfully");
        } else {
            return ApiResponse.failure(ErrorCode.FILE_IS_REQUIRED);
        }
    }


    // 公寓房型接口

    /**
     * 创建新房型
     *
     * @param apartmentRoomType 房型数据
     * @return 创建结果
     */
    @PostMapping(path = "/room_type", produces = "application/json;charset=UTF-8")
    public ApiResponse createApartmentRoomType(@RequestBody ApartmentRoomType apartmentRoomType) {
        boolean isApartmentRoomTypeExists = apartmentService.isApartmentRoomTypeExists(apartmentRoomType);
        if (isApartmentRoomTypeExists) {
            return ApiResponse.failure(ErrorCode.RESOURCE_ALREADY_EXISTS);
        }
        boolean success = apartmentService.saveApartmentRoomType(apartmentRoomType);
        return (success) ? ApiResponse.success() : ApiResponse.failure(ErrorCode.RESOURCE_UPDATE_FAILED);
    }


    /**
     * 公寓房型绑定，并制定价格
     *
     * @param apartmentRoomPrice 数据内容包含房型id、公寓id、价格
     * @return 定价结果
     */
    @PostMapping(path = "/room_type/pricing", produces = "application/json;charset=UTF-8")
    public ApiResponse bindRoomTypeToApartmentAndDefinePrice(@RequestBody ApartmentRoomPrice apartmentRoomPrice) {
        // TODO: 接口需要实现绑定方法，并在完成绑定后返回完整的 `公寓->房型->价格` 信息
        return null;
    }


    /**
     * 获取某公寓的房型列表（包含价格信息）
     *
     * @param apartmentId 公寓id
     * @param offset      子查询每条时间序列返回数据点的偏移量，默认值是0，代表不限制返回点数量
     * @param limit       子查询每条时间序列返回数据点的最大数目，默认值也是10，代表不偏移返回的数据点
     * @return 某公寓的房型列表
     */
    @RequestMapping(path = "/room_type/{apartment_id}", method = RequestMethod.GET)
    public ApiResponse getApartmentRoomTypeList(@PathVariable("apartment_id") Integer apartmentId,
                                                @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        // TODO: 需要从关系表中组合查询出公寓的房型列表
        return null;
    }

}
