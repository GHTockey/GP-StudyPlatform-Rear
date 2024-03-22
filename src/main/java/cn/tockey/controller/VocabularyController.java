package cn.tockey.controller;

import cn.tockey.domain.UserVocabulary;
import cn.tockey.domain.Vocabulary;
import cn.tockey.service.VocabularyService;
import cn.tockey.vo.BaseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Operation(summary = "添加词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
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
    @Operation(summary = "修改词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
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
    @Operation(summary = "删除词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
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
    @Operation(summary = "获取词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @GetMapping("/{id}")
    BaseResult<Vocabulary> getVocabulary(@PathVariable Integer id) {
        Vocabulary vocabulary = vocabularyService.getVocabularyById(id);
        if (vocabulary != null) {
            return BaseResult.ok("获取成功", vocabulary);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    @Operation(summary = "获取词集列表", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
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
    @Operation(summary = "获取用户的词集列表", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @GetMapping("/list/{uid}")
    BaseResult<List<Vocabulary>> getUserVocabularyListByUid(@PathVariable String uid) {
        List<Vocabulary> result = vocabularyService.getUserVocabularyListByUid(uid);
        if (result != null) {
            return BaseResult.ok("获取成功", result);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    // 获取用户学习的词集列表
    @Operation(summary = "获取用户学习的词集列表", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @GetMapping("/learn/{uid}")
    BaseResult<List<Vocabulary>> getUserRelevanceVocListByUid(@PathVariable String uid) {
        List<Vocabulary> result = vocabularyService.getUserRelevanceVocListByUid(uid);
        if (result != null) {
            return BaseResult.ok("获取成功", result);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    // 搜索词集
    @Operation(summary = "搜索词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @GetMapping("/search/{keyword}")
    BaseResult<List<Vocabulary>> searchVocabulary(@PathVariable String keyword) {
        List<Vocabulary> vocabularyList = vocabularyService.searchVocabulary(keyword);
        return BaseResult.ok("搜索成功", vocabularyList);
    }

    // 用户学习词集
    @Operation(summary = "用户学习词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @PostMapping("/learn")
    BaseResult<String> learnVocabulary(@RequestBody UserVocabulary userVocabulary) {
        Integer i = vocabularyService.userRelevanceVoc(userVocabulary);
        if (i > 0) return BaseResult.ok("已加入学习");
        return BaseResult.error("失败");
    }

    // 用户取消学习词集
    @Operation(summary = "用户取消学习词集", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @PutMapping("/cancel")
    BaseResult<String> cancelLearnVocabulary(@RequestBody UserVocabulary userVocabulary) {
        Integer i = vocabularyService.userCancelRelevanceVoc(userVocabulary);
        if (i > 0) return BaseResult.ok("操作成功");
        return BaseResult.error("失败");
    }

    // 获取学习数最多的词集列表
    @Operation(summary = "获取学习数最多的词集列表", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @GetMapping("/most")
    BaseResult<List<Vocabulary>> getMostStudyVocList() {
        List<Vocabulary> result = vocabularyService.getMostStudyVocList();
        if (result != null) {
            return BaseResult.ok("获取成功", result);
        } else {
            return BaseResult.error("获取失败");
        }
    }

    // 更新用户的词集学习数
    @Operation(summary = "更新用户的词集学习数", responses = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = BaseResult.class)))})
    @PutMapping("/updateStudyTotal")
    BaseResult<String> updateVocStudyTotal(@RequestBody UserVocabulary userVocabulary) {
        Integer i = vocabularyService.updateVocStudyTotal(userVocabulary.getUid(), userVocabulary.getVid());
        if (i > 0) return BaseResult.ok("更新成功");
        return BaseResult.error("更新失败");
    }

}
