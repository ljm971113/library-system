package com.boot.libsys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.libsys.entity.TblBookType;
import com.boot.libsys.mapper.TblBookTypeMapper;
import com.boot.libsys.service.ITblBookTypeService;
import org.springframework.stereotype.Service;

/**
 * @author Lucky
 * @since 2020-06-03
 */

@Service
public class TblBookTypeServiceImpl extends ServiceImpl<TblBookTypeMapper, TblBookType> implements ITblBookTypeService {
}
