package com.example.springbatchstudy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

interface DeptRepository extends CrudRepository<Dept, Long> {
}
