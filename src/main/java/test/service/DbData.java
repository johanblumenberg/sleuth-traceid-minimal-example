package test.service;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("test")
public class DbData {
    @Id
    @Column("id")
    public String id;

    @Column("value")
    public Integer value;
}
