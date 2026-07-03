package com.bank.loanapproval.loanproductservice.dto;

import com.bank.loanapproval.domain.loanproduct.EligibilityRule;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityRuleRequest {

    @NotBlank(message = "Rule name is required")
    @Size(max = 100, message = "Rule name must not exceed 100 characters")
    private String ruleName;

    @Size(max = 500, message = "Rule description must not exceed 500 characters")
    private String ruleDescription;

    @NotNull(message = "Rule type is required")
    private EligibilityRule.RuleType ruleType;

    @Size(max = 500, message = "Rule expression must not exceed 500 characters")
    private String ruleExpression;

    private BigDecimal minValue;

    private BigDecimal maxValue;

    @Size(max = 100, message = "String value must not exceed 100 characters")
    private String stringValue;

    @Min(value = 0, message = "Priority must be non-negative")
    private Integer priority;

    private Boolean mandatory;
}
