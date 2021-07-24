package com.boot.libsys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.libsys.entity.TblUser;
import com.boot.libsys.mapper.TblUserMapper;
import com.boot.libsys.service.ITblUserService;
import org.springframework.stereotype.Service;

/**
 * @author Lucky
 * @since 2020-06-03
 */

@Service
public class TblUserServiceImpl extends ServiceImpl<TblUserMapper, TblUser> implements ITblUserService {
}
