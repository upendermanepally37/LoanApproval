package com.bank.loanapproval.customerservice.dto;

import com.bank.loanapproval.domain.customer.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequest {

    @NotNull(message = "Document type is required")
    private Document.DocumentType documentType;

    @NotBlank(message = "Document name is required")
    @Size(max = 255, message = "Document name must not exceed 255 characters")
    private String documentName;

    @Size(max = 500, message = "Document path must not exceed 500 characters")
    private String documentPath;

    @Size(max = 50, message = "Document number must not exceed 50 characters")
    private String documentNumber;

    @Size(max = 20, message = "Document format must not exceed 20 characters")
    private String documentFormat;

    @Positive(message = "File size must be positive")
    private Long fileSize;
}
