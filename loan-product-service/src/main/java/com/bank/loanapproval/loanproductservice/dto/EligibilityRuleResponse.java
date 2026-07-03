package com.bank.loanapproval.loanproductservice.dto;

import com.bank.loanapproval.domain.loanproduct.EligibilityRule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EligibilityRuleResponse {
    private Long id;
    private String ruleName;
    private String ruleDescription;
    private EligibilityRule.RuleType ruleType;
    private String ruleExpression;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private String stringValue;
    private Integer priority;
    private Boolean mandatory;
    private Boolean active;
}
