package cn.tockey.feign;

import cn.tockey.domain.Classes;
import cn.tockey.vo.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "classes-service",path = "/classes")
public interface ClassesFeign {
    // 根据ID获取班级信息
    @GetMapping("/{id}")
    BaseResult<Classes> getClassesById(@PathVariable String id);
    // 根据用户ID获取班级
    @GetMapping("/user/{uid}")
    BaseResult<Classes> getCLassesByUid(@PathVariable String uid);
}
