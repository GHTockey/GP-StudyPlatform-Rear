package cn.tockey.controller;

import cn.tockey.domain.Website;
import cn.tockey.service.WebsitesService;
import cn.tockey.vo.MyResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tockey
 * @since 2024-04-07
 */
@RestController
@RequestMapping("/website")
public class WebsitesController {
    @Resource
    private WebsitesService websitesService;

    // 获取网站数据
    @GetMapping
    MyResult<Website> getWebsites(){
        Website website = websitesService.getOne(null);
        return MyResult.ok("获取成功", website);
    }

    // 修改网站信息
    @PutMapping
    MyResult<String> updWebsiteInfo(@RequestBody Website website){
        //System.out.println(website);
        if (websitesService.updWebsiteInfo(website)) {
            return MyResult.ok("修改成功");
        } else {
            return MyResult.error("修改失败");
        }
    }
}
