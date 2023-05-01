package com.hc.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 何超
 * @date: 2023-05-01 22:35
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountUser {

    private int id;

    private String username;

    private String email;
}
