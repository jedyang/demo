package com.example.yunsheng.mysql;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/8/9 17:44
 */
@Getter
@Setter
@Entity
@Table(name = "tb_file")
public class FileEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="file", columnDefinition="longblob", nullable=true)
    private byte[] file;

}
