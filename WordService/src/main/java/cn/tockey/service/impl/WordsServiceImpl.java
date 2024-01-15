package cn.tockey.service.impl;

import cn.tockey.domain.Words;
import cn.tockey.mapper.WordsMapper;
import cn.tockey.service.WordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
@Service
public class WordsServiceImpl extends ServiceImpl<WordsMapper, Words> implements WordsService {

}
