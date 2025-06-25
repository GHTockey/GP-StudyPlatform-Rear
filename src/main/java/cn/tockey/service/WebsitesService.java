package cn.tockey.service;

import cn.tockey.domain.Website;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2024-04-07
 */
public interface WebsitesService extends IService<Website> {
    // 修改网站信息
    boolean updWebsiteInfo(Website website);
}
