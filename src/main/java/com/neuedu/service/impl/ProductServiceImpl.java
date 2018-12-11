package com.neuedu.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.neuedu.common.Const;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.CategoryMapper;
import com.neuedu.dao.ProductMapper;
import com.neuedu.pojo.Category;
import com.neuedu.pojo.Product;
import com.neuedu.service.ICategoryService;
import com.neuedu.service.IProductService;
import com.neuedu.utils.DateUtils;
import com.neuedu.utils.PropertiesUtils;
import com.neuedu.vo.ProductDetailVO;
import com.neuedu.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;

    //添加商品或者更新商品
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        //step1:参数校验
        if (product==null){
            return ServerResponse.createServerResponseByError(ResponseCode.PARAM_EMPTY.getStatus(),ResponseCode.PARAM_EMPTY.getMsg());
        }
        //step2:设置商品主图 sub_images --> 1.jpg,2.jpg,3.png
        String subImages=product.getSubImages();
        if(subImages!=null&&!subImages.equals("")){
            String[] subImageArr=subImages.split(",");
            if (subImageArr.length>0){
                //设置商品的主图
                product.setMainImage(subImageArr[0]);
            }
        }
        //step3:商品save or update
        if (product.getId()==null){
            //添加
            int result=productMapper.insert(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess();
            }else {
                return ServerResponse.createServerResponseByError("添加失败");
            }
        }else {
             //更新
            int result=productMapper.updateByPrimaryKey(product);
            if (result>0){
                return ServerResponse.createServerResponseBySucess();
            }else {
                return ServerResponse.createServerResponseByError("更新失败");
            }

        }

    }
    //商品上下架
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        //step1:参数非空校验
        if (productId==null){
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        if (status==null){
            return ServerResponse.createServerResponseByError("商品状态不能为空");
        }
        //step2:更新商品状态
        Product product=new Product();
        product.setId(productId);
        product.setStatus(status);
       int result = productMapper.updateProductKeySelective(product);
        //step3:返回结果
        if (result>0){
            return ServerResponse.createServerResponseBySucess();
        }else {
            return ServerResponse.createServerResponseByError("更新失败");
        }
    }

    //查看商品详情
    @Override
    public ServerResponse detail(Integer productId) {
        //step1:参数非空校验
        if (productId==null) {
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        //step2:根据productid查询商品信息
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //step3:product-->productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        //step4:返回结果
        return ServerResponse.createServerResponseBySucess(null,productDetailVO);
    }



    private ProductDetailVO assembleProductDetailVO(Product product){

        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(product.getSubImages());
        //父类id
        Category category=categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            //默认根节点
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }
    //分页查看产品列表
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum,pageSize);
        //step1:查询商品数据
        List<Product> productList=productMapper.selectAll();
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product : productList) {
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());

        return productListVO;
    }
    //商品搜索根据productId和productName
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        //select * from product where productId=? and productName like %name%
        PageHelper.startPage(pageNum,pageSize);

        if (productName!=null&&!productName.equals("")){
                productName="%"+productName+"%";
        }
        List<Product> productList=productMapper.findProductByProductIdAndProductName(productId,productName);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if (productList!=null&&productList.size()>0){
            for (Product product : productList) {
                ProductListVO productListVO=assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo=new PageInfo(productListVOList);
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }
//图片上传
    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if (file==null){
            return ServerResponse.createServerResponseByError();
        }
        //获取图片名称
        String orignalFileName=file.getOriginalFilename();
        //获取图片的扩展名
        String exName=orignalFileName.substring(orignalFileName.lastIndexOf("."));
        //为图片生成新的唯一的名字
        String newFileName=UUID.randomUUID().toString()+exName;

        File pathFile=new File(path);

        if (!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }

        File file1=new File(path,newFileName);
        try {
            file.transferTo(file1);
            //上传到图片服务器

            //...
            Map<String,String> map=Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            return ServerResponse.createServerResponseBySucess(null,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //前台-商品详情
    @Override
    public ServerResponse detail_portal(Integer productId) {

        //step1:参数校验
        if (productId==null) {
            return ServerResponse.createServerResponseByError("商品id不能为空");
        }
        //step2:根据productid查询商品信息
        Product product=productMapper.selectByPrimaryKey(productId);
        if (product==null){
            return ServerResponse.createServerResponseByError("商品不存在");
        }
        //step3:校验商品状态
        if (product.getStatus()==Const.ProductStatusEnum.PRODUCT_ONLINE.getStatus()){
            return ServerResponse.createServerResponseByError("商品已下架或删除");
        }
        //step4:获取productDetailVO
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        //step5:返回结果
        return ServerResponse.createServerResponseBySucess(null,productDetailVO);
    }

    //前台-商品搜索
    @Override
    public ServerResponse list_portal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //step1:参数校验 categoryId和keyword不能同时为空
        if(categoryId==null&&(keyword==null||keyword.equals(""))){
            return ServerResponse.createServerResponseByError("参数错误");
        }
        //step2:categoryId
        Set<Integer> integerSet= Sets.newHashSet();
        if(categoryId!=null){
            Category category=  categoryMapper.selectByPrimaryKey(categoryId);
            if(category==null&&(keyword==null||keyword.equals(""))){
                //说明没有商品数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList=Lists.newArrayList();
                PageInfo pageInfo=new PageInfo(productListVOList);
                return ServerResponse.createServerResponseBySucess(null,pageInfo);
            }

            ServerResponse serverResponse= categoryService.get_deep_category(categoryId);

            if(serverResponse.isSuccess()){
                integerSet=(Set<Integer>) serverResponse.getData();
            }
        }
        //step3: keyword
        if(keyword!=null&&!keyword.equals("")){
            keyword="%"+keyword+"%";
        }

        if(orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize);
        }else{
            String[] orderByArr=   orderBy.split("_");
            if(orderByArr.length>1){
                PageHelper.startPage(pageNum,pageSize,orderByArr[0]+" "+orderByArr[1]);
            }else{
                PageHelper.startPage(pageNum,pageSize);
            }
        }
        //step4: List<Product>-->List<ProductListVO>
        List<Product> productList=productMapper.searchProduct(integerSet,keyword);
        List<ProductListVO> productListVOList=Lists.newArrayList();
        if(productList!=null&&productList.size()>0){
            for(Product product:productList){
                ProductListVO productListVO=  assembleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }

        //step5:分页

        PageInfo pageInfo=new PageInfo();
        pageInfo.setList(productListVOList);
        //step6:返回
        return ServerResponse.createServerResponseBySucess(null,pageInfo);
    }

}
