package com.sts.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sts.constants.Endpoints;
import com.sts.constants.EntityNames;
import com.sts.constants.APILogDebugMessages;
import com.sts.constants.APILogInfoMessages;
import com.sts.constants.SuccessMessages;
import com.sts.constants.SuccessResponse;
import com.sts.dto.department.CreateDepartmentReq;
import com.sts.dto.department.CreateDepartmentRes;
import com.sts.dto.department.DepartmentResponse;
import com.sts.dto.department.GetDepartmentByIdRes;
import com.sts.dto.department.UpdateDepartmentReq;
import com.sts.dto.department.UpdateDepartmentRes;
import com.sts.service.interfaces.DepartmentService;
import com.sts.utils.ResponseBuilder1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(Endpoints.V1_DEPARTMENTS)
@RequiredArgsConstructor
@Slf4j

public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping("/{departmentId}")
    public ResponseEntity<SuccessResponse<GetDepartmentByIdRes>> getDepartmentById(@PathVariable("departmentId") String departmentId) {
        log.info(APILogInfoMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT, departmentId));
        GetDepartmentByIdRes res = departmentService.getDepartmentById(departmentId);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.DEPARTMENT, res));
        log.info(APILogInfoMessages.ENTITY_FETCHED_WITH_ID.getMessage(EntityNames.DEPARTMENT, departmentId));
        return ResponseBuilder1.ok(SuccessMessages.FETCH_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT, departmentId), res);
    }

    @GetMapping("/bulk")
    public ResponseEntity<SuccessResponse<List<DepartmentResponse>>> getAllDepartments() {
        log.info(APILogInfoMessages.FETCHING_ALL_ENTITIES.getMessage(EntityNames.DEPARTMENTS));
        List<DepartmentResponse> res = departmentService.getAllDepartments();
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.DEPARTMENTS, res));
        log.info(APILogInfoMessages.ALL_ENTITIES_FETCHED.getMessage(res.size(), EntityNames.DEPARTMENTS));
        return ResponseBuilder1.ok(SuccessMessages.ALL_ENTITIES_FETCHED.getMessage(res.size(), EntityNames.DEPARTMENTS), res);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse<CreateDepartmentRes>> createDepartment(@RequestBody CreateDepartmentReq req) {
        log.info(APILogInfoMessages.CREATE_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT, req.getDepartmentId()));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.DEPARTMENT, req));
        CreateDepartmentRes res = departmentService.createDepartment(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.DEPARTMENT, res));
        log.info(APILogInfoMessages.ENTITY_CREATED_WITH_ID.getMessage(EntityNames.DEPARTMENT, res.getDepartmentId()));
        return ResponseBuilder1.created(SuccessMessages.ENTITY_CREATED.getMessage(EntityNames.DEPARTMENT, res.getDepartmentId()), res);
    }

    @PostMapping("/bulk")
    public ResponseEntity<SuccessResponse<Object>> createBulkDepartments(@RequestBody @Valid List<CreateDepartmentReq> requests) {
        log.info(APILogInfoMessages.CREATE_BULK_ENTITIES_WITH_SIZE.getMessage(EntityNames.DEPARTMENTS, requests.size()));
        log.debug(APILogDebugMessages.BULK_REQUEST_OBJECT.getMessage(EntityNames.DEPARTMENTS, requests.size(), requests));
        String res = departmentService.createDepartmentsInBulk(requests);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.DEPARTMENTS, res));
        log.info(APILogInfoMessages.BULK_ENTITIES_CREATED_WITH_SIZE.getMessage(EntityNames.DEPARTMENTS, requests.size()));
        return ResponseBuilder1.ok(SuccessMessages.BULK_ENTITIES_CREATED.getMessage(requests.size(), EntityNames.DEPARTMENTS), res);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<UpdateDepartmentRes>> updateDepartment(@RequestBody @Valid UpdateDepartmentReq req) {
        log.info(APILogInfoMessages.CREATE_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT, req.getDepartmentId()));
        log.debug(APILogDebugMessages.REQUEST_OBJECT.getMessage(EntityNames.DEPARTMENT, req));
        UpdateDepartmentRes res = departmentService.updateDepartment(req);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.DEPARTMENT, res));
        log.info(APILogInfoMessages.ENTITY_UPDATED_WITH_ID.getMessage(EntityNames.DEPARTMENT, res.getDepartmentId()));
        return ResponseBuilder1.ok(SuccessMessages.ENTITY_UPDATED.getMessage(EntityNames.DEPARTMENT, res.getDepartmentId()), res);
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<SuccessResponse<String>> deleteDepartmentById(@PathVariable("departmentId") String departmentId) {
        log.info(APILogInfoMessages.DELETE_ENTITY_WITH_ID.getMessage(EntityNames.DEPARTMENT, departmentId));
        String res = departmentService.deleteDepartmentById(departmentId);
        log.debug(APILogDebugMessages.RESPONSE_OBJECT.getMessage(EntityNames.DEPARTMENT, res));
        log.info(APILogInfoMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.DEPARTMENT, departmentId));
        return ResponseBuilder1.ok(SuccessMessages.ENTITY_DELETED_WITH_ID.getMessage(EntityNames.DEPARTMENT, departmentId), res);
    }
}
