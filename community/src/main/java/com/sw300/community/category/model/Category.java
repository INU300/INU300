package com.sw300.community.category.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;
    @Column(unique = true)
    private String name;

    @Column(name = "daily_visitors", columnDefinition = "int default 0") // 일별 방문자 수를 저장할 컬럼
    private int dailyVisitors; // 일별 방문자 수를 저장할 필드

    @Column(columnDefinition = "boolean default false")
    private boolean isSubclass; // 학과 소분류 카테고리인지 여부

    public void changeDailyVisiters(int dailyVisitors){
        this.dailyVisitors = dailyVisitors;
    }
}