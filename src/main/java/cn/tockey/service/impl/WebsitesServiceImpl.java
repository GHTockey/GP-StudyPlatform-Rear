package cn.tockey.service.impl;

import cn.tockey.domain.Website;
import cn.tockey.mapper.WebsitesMapper;
import cn.tockey.service.WebsitesService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tockey
 * @since 2024-04-07
 */
@Service
public class WebsitesServiceImpl extends ServiceImpl<WebsitesMapper, Website> implements WebsitesService {
    @Resource
    private WebsitesMapper websitesMapper;

    @Override
    public boolean updWebsiteInfo(Website website) {
        return websitesMapper.update(website, new QueryWrapper<Website>().eq("id","0")) > 0;
    }
}
