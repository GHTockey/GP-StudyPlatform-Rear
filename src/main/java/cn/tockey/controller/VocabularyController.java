package cn.tockey.controller;

import cn.tockey.domain.UserVocabulary;
import cn.tockey.domain.Vocabulary;
import cn.tockey.service.VocabularyService;
import cn.tockey.vo.BaseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

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
    //@ApiOperation("添加词集")
    @PostMapping
    BaseResult<HashMap<String, Object>> addVocabulary(@RequestBody Vocabulary vocabulary) {
        String id = vocabularyService.addVocabulary(vocabulary);
        HashMap<String, Object> obj = new HashMap<>();
        obj.put("vid", id);
        if (id != null && !id.isEmpty()) {
            return BaseResult.ok("添加成功", obj);
        } else {
            return BaseResult.error("添加失败");
        }
    }


    // 修改词集
    //@ApiOperation("修改词集")
    @PutMapping
    BaseResult<String> updVocabulary(@RequestBody Vocabulary vocabulary) {
        boolean updateResult = vocabularyService.updVocabulary(vocabulary);
        if (updateResult) {
            return BaseResult.ok("修改成功");
        } else {
            return BaseResult.error("修改失败");
        }
    }


    // 删除词集
    //@ApiOperation("删除词集")
    @DeleteMapping("/{id}")
    BaseResult<String> delVocabulary(@PathVariable Integer id) {
        boolean deleteResult = vocabularyService.delVocabulary(id);
        if (deleteResult) {
            return BaseResult.ok("删除成功");
        } else {
            return BaseResult.error("删除失败");
        }
    }


    // 获取词集
    //@ApiOperation("获取词集")
    @GetMapping("/{id}")
    BaseResult<Vocabulary> getVocabulary(@PathVariable Integer id) {
        Vocabulary vocabulary = vocabularyService.getVocabularyById(id);
        if (vocabulary != null) {
            return BaseResult.ok("获取成功", vocabulary);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    //@ApiOperation("获取词集列表")
    @GetMapping("/list")
    BaseResult<List<Vocabulary>> getVocabularyList() {
        List<Vocabulary> result = vocabularyService.getVocabularyList();
        if (result != null) {
            return BaseResult.ok("获取成功", result);
        } else {
            return BaseResult.error("获取失败");
        }
    }


    // 获取用户的词集列表
    //@ApiOperation("获取用户的词集列表")
    @GetMapping("/list/{uid}")
    BaseResult<List<Vocabulary>> getUserVocabularyListByUid(@PathVariable String uid) {
        List<Vocabulary> result = vocabularyService.getUserVocabularyListByUid(uid);
        if (result != null) {
            return BaseResult.ok("获取成功", result);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    // 搜索词集
    //@ApiOperation("搜索词集")
    @GetMapping("/search/{keyword}")
    BaseResult<List<Vocabulary>> searchVocabulary(@PathVariable String keyword) {
        List<Vocabulary> vocabularyList = vocabularyService.searchVocabulary(keyword);
        return BaseResult.ok("搜索成功", vocabularyList);
    }

    // 用户学习词集
    //@ApiOperation("用户学习词集")
    @PostMapping("/learn")
    BaseResult<String> learnVocabulary(@RequestBody UserVocabulary userVocabulary) {
        Integer i = vocabularyService.userRelevanceVoc(userVocabulary);
        if (i > 0) return BaseResult.ok("已加入学习");
        return BaseResult.error("失败");
    }
}
