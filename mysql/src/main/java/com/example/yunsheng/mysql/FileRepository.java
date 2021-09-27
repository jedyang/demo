package com.example.yunsheng.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: yunsheng
 * @time: 2021/8/9 17:50
 */
@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
