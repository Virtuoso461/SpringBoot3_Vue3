package com.hc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: 何超
 * @date: 2023-04-29 16:34
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {

    private int id;

    private String username;

    private String password;

    private String email;
}
