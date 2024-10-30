package com.example.backoffice.domain.expense.entity;

import com.example.backoffice.domain.file.entity.Files;
import com.example.backoffice.domain.finance.entity.Finance;
import com.example.backoffice.domain.member.entity.MemberDepartment;
import com.example.backoffice.global.common.CommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private BigDecimal money;

    private String details;

    // 작성자
    private String memberName;

    @Enumerated(EnumType.STRING)
    private ExpenseProcess expenseProcess;

    @Enumerated(EnumType.STRING)
    private MemberDepartment department;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    private Finance finance;

    @Builder.Default
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Files> fileList = new ArrayList<>();

    public void updateProcess(ExpenseProcess expenseProcess){
        this.expenseProcess = expenseProcess;
    }

    public void update(String details, String title, BigDecimal money){
        this.title = title;
        this.details = details;
        this.money = money;
    }
}