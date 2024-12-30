package com.example.backoffice.domain.finance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFinance is a Querydsl query type for Finance
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFinance extends EntityPathBase<Finance> {

    private static final long serialVersionUID = 154534963L;

    public static final QFinance finance = new QFinance("finance");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    public final NumberPath<Long> bankId = createNumber("bankId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath currency = createString("currency");

    public final StringPath description = createString("description");

    public final ListPath<com.example.backoffice.domain.expense.entity.Expense, com.example.backoffice.domain.expense.entity.QExpense> expenseList = this.<com.example.backoffice.domain.expense.entity.Expense, com.example.backoffice.domain.expense.entity.QExpense>createList("expenseList", com.example.backoffice.domain.expense.entity.Expense.class, com.example.backoffice.domain.expense.entity.QExpense.class, PathInits.DIRECT2);

    public final NumberPath<Integer> expires_in = createNumber("expires_in", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath rsp_code = createString("rsp_code");

    public final StringPath rsp_message = createString("rsp_message");

    public final NumberPath<java.math.BigDecimal> totalBalance = createNumber("totalBalance", java.math.BigDecimal.class);

    public QFinance(String variable) {
        super(Finance.class, forVariable(variable));
    }

    public QFinance(Path<? extends Finance> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFinance(PathMetadata metadata) {
        super(Finance.class, metadata);
    }

}

