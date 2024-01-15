package cn.tockey.controller;

import cn.tockey.domain.User;
import cn.tockey.domain.Vocabulary;
import cn.tockey.service.VocabularyService;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.VocabularyAddVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
@RestController
@RequestMapping("/vocabulary")
public class VocabularyController {
    @Resource
    private VocabularyService vocabularyService;

    // 添加词集
    @PostMapping
    BaseResult<HashMap<String, Integer>> addVocabulary(@RequestBody VocabularyAddVo vocabularyAddVo) {
        int insertResult = vocabularyService.addVocabulary(vocabularyAddVo);
        HashMap<String, Integer> obj = new HashMap<>();
        obj.put("vid", insertResult);
        if (insertResult != 0) {
            return BaseResult.ok("添加成功", obj);
        } else {
            return BaseResult.error("添加失败");
        }
    }

    ;

    // 修改词集
    @PutMapping
    BaseResult<String> updVocabulary(@RequestBody Vocabulary vocabulary) {
        boolean updateResult = vocabularyService.updVocabulary(vocabulary);
        if (updateResult) {
            return BaseResult.ok("修改成功");
        } else {
            return BaseResult.error("修改失败");
        }
    }

    ;

    // 删除词集
    @DeleteMapping("/{id}")
    BaseResult<String> delVocabulary(@PathVariable Integer id) {
        boolean deleteResult = vocabularyService.delVocabulary(id);
        if (deleteResult) {
            return BaseResult.ok("删除成功");
        } else {
            return BaseResult.error("删除失败");
        }
    }

    ;

    // 获取词集
    @GetMapping("/{id}")
    BaseResult<Vocabulary> getVocabulary(@PathVariable Integer id) {
        Vocabulary vocabulary = vocabularyService.getVocabulary(id);
        if (vocabulary != null) {
            return BaseResult.ok("获取成功", vocabulary);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    ;

    // 获取用户的词集列表
    @GetMapping("/list/{uid}")
    BaseResult<User> getUserVocabularyListByUid(@PathVariable String uid) {
        List<Vocabulary> result = vocabularyService.getUserVocabularyListByUid(uid);
        if (result != null) {
            return BaseResult.ok("获取成功", result);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    ;
}
