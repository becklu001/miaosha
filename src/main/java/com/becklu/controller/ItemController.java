package com.becklu.controller;

import com.becklu.controller.viewobject.ItemVO;
import com.becklu.error.BusinessException;
import com.becklu.response.CommonReturnType;
import com.becklu.service.ItemService;
import com.becklu.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ItemController extends BaseController{
    @Autowired
    private ItemService itemService;

    //创建商品接口
    @RequestMapping(value = "/createItem",method = {RequestMethod.POST})
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
    //调用service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);

        ItemModel itemModelReturn = itemService.createItem(itemModel);
        ItemVO itemVO = convertItemVOFromItemModel(itemModel);
        return  CommonReturnType.create(itemVO);

    }

    //浏览商品详情接口
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getItemById(@RequestParam(name = "id") Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertItemVOFromItemModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    //商品列表浏览接口
    @RequestMapping(value = "/listItems",method = {RequestMethod.GET})
    public CommonReturnType listItems(){
        List<ItemModel> itemModelList = itemService.listItems();
        List<ItemVO> itemVOList = itemModelList.stream().map(
                itemModel -> {
                    ItemVO itemVO = convertItemVOFromItemModel(itemModel);
                    return itemVO;
                }).collect(Collectors.toList());

        return CommonReturnType.create(itemVOList);
    }

    private ItemVO convertItemVOFromItemModel(ItemModel itemModel){
        if(null == itemModel){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if(itemModel.getPromoModel() != null){
            //表明有即将开始或者正在进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        }else{
            //没有秒杀活动，或者秒杀活动已经结束
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
