package com.example.backoffice.domain.finance.entity;

import com.example.backoffice.domain.expense.entity.Expense;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Finance extends CommonEntity {

    // field
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 은행 계좌 ID (금융 결제원에서 제공하는 ID)
    private Long bankId;

    // 회사 전체 자산 잔액
    private BigDecimal totalBalance;

    // 화폐 단위 (KRW, USD 등)
    private String currency;

    // 재정 상태에 대한 설명
    private String description;

    // 금융 결제원이 전달해주는 코드
    private String rsp_code;

    private Integer expires_in;

    private String rsp_message;

    // relation
    @OneToMany(mappedBy = "finance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenseList;

    // entity method
}
