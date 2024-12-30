package com.example.backoffice.domain.expense.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExpense is a Querydsl query type for Expense
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExpense extends EntityPathBase<Expense> {

    private static final long serialVersionUID = 889337971L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExpense expense = new QExpense("expense");

    public final com.example.backoffice.global.common.QCommonEntity _super = new com.example.backoffice.global.common.QCommonEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<com.example.backoffice.domain.member.entity.MemberDepartment> department = createEnum("department", com.example.backoffice.domain.member.entity.MemberDepartment.class);

    public final StringPath details = createString("details");

    public final EnumPath<ExpenseProcess> expenseProcess = createEnum("expenseProcess", ExpenseProcess.class);

    public final ListPath<com.example.backoffice.domain.file.entity.Files, com.example.backoffice.domain.file.entity.QFiles> fileList = this.<com.example.backoffice.domain.file.entity.Files, com.example.backoffice.domain.file.entity.QFiles>createList("fileList", com.example.backoffice.domain.file.entity.Files.class, com.example.backoffice.domain.file.entity.QFiles.class, PathInits.DIRECT2);

    public final com.example.backoffice.domain.finance.entity.QFinance finance;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memberName = createString("memberName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<java.math.BigDecimal> money = createNumber("money", java.math.BigDecimal.class);

    public final StringPath title = createString("title");

    public QExpense(String variable) {
        this(Expense.class, forVariable(variable), INITS);
    }

    public QExpense(Path<? extends Expense> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExpense(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExpense(PathMetadata metadata, PathInits inits) {
        this(Expense.class, metadata, inits);
    }

    public QExpense(Class<? extends Expense> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.finance = inits.isInitialized("finance") ? new com.example.backoffice.domain.finance.entity.QFinance(forProperty("finance")) : null;
    }

}

