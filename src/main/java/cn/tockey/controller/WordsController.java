package cn.tockey.controller;

import cn.tockey.domain.Words;
import cn.tockey.service.WordsService;
import cn.tockey.vo.BaseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
@RestController
@RequestMapping("/words")
public class WordsController {
    @Resource
    private WordsService wordsService;

    // 新增新的词语
    @PostMapping
    BaseResult<String> generateNewWord(@RequestBody Words words){
        String id = wordsService.generateNewWord(words);
        Map<String, Object> data = new HashMap<>();
        data.put("id",id);
        return BaseResult.ok("操作成功",data);
    }

    // 删除词语
    @DeleteMapping("/{id}")
    BaseResult<String> delWordsById(@PathVariable String id) {
        Integer i = wordsService.delWordsById(id);
        if (i>0) return BaseResult.ok("删除成功");
        return BaseResult.error("删除失败,请检查ID");
    }
}
