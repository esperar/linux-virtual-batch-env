package com.example.springbatchstudy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Dept {

    @Id
    private Long id;
    private String dName;
    private String loc;
}
