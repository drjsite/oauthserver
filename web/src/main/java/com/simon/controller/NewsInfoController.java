package com.simon.controller;

import com.simon.common.controller.BaseController;
import com.simon.common.domain.EasyUIDataGridResult;
import com.simon.common.domain.ResultMsg;
import com.simon.common.domain.UserEntity;
import com.simon.model.NewsInfo;
import com.simon.service.DictTypeService;
import com.simon.service.NewsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 新闻
 *
 * @author SimonSun
 * @date 2019-01-20
 **/
@Slf4j
@Api(description = "新闻")
@Controller
@RequestMapping("/api/newsInfos")
public class NewsInfoController extends BaseController {

    @Autowired
    private NewsInfoService newsInfoService;

    @Autowired
    private DictTypeService dictTypeService;

    @ApiIgnore
    @ApiOperation(value = "列表页面")
    @GetMapping("list")
    public String list(Model model, Locale locale) {
        model.addAttribute("newsStatusList", dictTypeService.getTypeByGroupCode("news_status", locale.toString()));
        model.addAttribute("newsTypeList", dictTypeService.getTypeByGroupCode("news_type", locale.toString()));
        return "vue/newsInfo/list";
    }

    @ApiIgnore
    @ApiOperation(value = "新增页面")
    @GetMapping("add")
    public String add(Model model, Locale locale) {
        model.addAttribute("newsStatusList", dictTypeService.getTypeByGroupCode("news_status", locale.toString()));
        model.addAttribute("newsTypeList", dictTypeService.getTypeByGroupCode("news_type", locale.toString()));
        return "vue/newsInfo/add";
    }

    @ApiIgnore
    @ApiOperation(value = "编辑页面")
    @GetMapping("edit")
    public String edit(@RequestParam Long id, Model model, Locale locale) {
        model.addAttribute("newsStatusList", dictTypeService.getTypeByGroupCode("news_status", locale.toString()));
        model.addAttribute("newsTypeList", dictTypeService.getTypeByGroupCode("news_type", locale.toString()));
        model.addAttribute("entity", entityToMap(newsInfoService.findById(id)));
        return "vue/newsInfo/edit";
    }

    @ApiIgnore
    @ApiOperation(value = "列表数据")
    @GetMapping("data")
    @ResponseBody
    public EasyUIDataGridResult<NewsInfo> data(
            @ApiParam(value = "标题") @RequestParam(required = false) String title,
            @ApiParam(value = "状态") @RequestParam(required = false) Integer status,
            @ApiParam(value = "新闻类型") @RequestParam(required = false) Integer newsType,
            @ApiParam(value = "发布时间") @RequestParam(required = false) String[] publishDate,
            @ApiParam(value = "页码", defaultValue = "1", required = true) @RequestParam Integer pageNo,
            @ApiParam(value = "每页条数", defaultValue = "10", required = true) @RequestParam Integer pageSize,
            @ApiParam(value = "排序") @RequestParam(required = false, defaultValue = "") String orderBy) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("title", title);
        params.put("status", status);
        params.put("newsType", newsType);
        log.info(StringUtils.join(publishDate, ","));
        if (null != publishDate && publishDate.length == 2) {
            params.put("publishDateStart", publishDate[0]);
            params.put("publishDateEnd", publishDate[1]);
        }
        return new EasyUIDataGridResult<>(newsInfoService.getList(params, pageNo, pageSize, orderBy));
    }

    @ApiOperation(value = "新增")
    @PostMapping("add")
    @ResponseBody
    public ResultMsg add(@RequestBody NewsInfo body, Authentication authentication) {
        UserEntity userEntity = getCurrentUser(authentication);
        body.setCreateDate(LocalDateTime.now());
        body.setCreateBy(userEntity.getId());
        body.setUserId(userEntity.getId());
        newsInfoService.insertSelective(body);
        return ResultMsg.success();
    }

    @ApiOperation(value = "修改")
    @PatchMapping("edit")
    @ResponseBody
    public ResultMsg edit(@RequestBody NewsInfo body, Authentication authentication) {
        UserEntity userEntity = getCurrentUser(authentication);
        body.setUpdateDate(LocalDateTime.now());
        body.setUpdateBy(userEntity.getId());
        newsInfoService.updateByPrimaryKeySelective(body);
        return ResultMsg.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/ids/{ids}")
    @ResponseBody
    public ResultMsg delete(@PathVariable String ids) {
        newsInfoService.deleteByIds(ids);
        return ResultMsg.success();
    }
}