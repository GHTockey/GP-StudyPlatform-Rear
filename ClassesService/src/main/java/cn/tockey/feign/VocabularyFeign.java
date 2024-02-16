package cn.tockey.feign;

import cn.tockey.domain.Vocabulary;
import cn.tockey.vo.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "vocabulary-service",path = "/vocabulary")
public interface VocabularyFeign {
    // 获取用户的词集列表
    @GetMapping("/list/{uid}")
    BaseResult<List<Vocabulary>> getUserVocabularyListByUid(@PathVariable String uid);
}
