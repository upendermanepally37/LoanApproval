package com.bank.loanapproval.eligibilityengine.controller;

import com.bank.loanapproval.common.dto.ApiResponse;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationRequest;
import com.bank.loanapproval.eligibilityengine.dto.EligibilityEvaluationResponse;
import com.bank.loanapproval.eligibilityengine.service.EligibilityEngineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluate")
@RequiredArgsConstructor
@Tag(name = "Eligibility Evaluation", description = "APIs for loan eligibility evaluation")
public class EligibilityController {

    private final EligibilityEngineService eligibilityEngineService;

    @PostMapping
    @Operation(summary = "Evaluate loan eligibility", description = "Evaluate customer eligibility for a loan product based on various rules")
    public ResponseEntity<ApiResponse<EligibilityEvaluationResponse>> evaluateEligibility(
            @Valid @RequestBody EligibilityEvaluationRequest request) {
        EligibilityEvaluationResponse response = eligibilityEngineService.evaluateEligibility(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/async")
    @Operation(summary = "Evaluate loan eligibility asynchronously", description = "Submit eligibility evaluation for async processing")
    public ResponseEntity<ApiResponse<Void>> evaluateEligibilityAsync(
            @Valid @RequestBody EligibilityEvaluationRequest request) {
        eligibilityEngineService.evaluateEligibility(request);
        return ResponseEntity.accepted().body(
                ApiResponse.success("Eligibility evaluation submitted for processing", null));
    }
}
