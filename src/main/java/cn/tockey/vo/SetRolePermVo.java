package cn.tockey.vo;

import lombok.Data;

import java.util.List;

@Data
public class SetRolePermVo {
    private List<Integer> selectedIds;
    private List<Integer> halfCheckIds;
}
