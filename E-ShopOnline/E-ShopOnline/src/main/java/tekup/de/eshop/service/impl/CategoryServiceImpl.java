package tekup.de.eshop.service.impl;


import tekup.de.eshop.model.ProductCategory;
import tekup.de.eshop.enums.ResultEnum;
import tekup.de.eshop.exception.MyException;
import tekup.de.eshop.repository.ProductCategoryRepository;
import tekup.de.eshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created By Zhu Lin on 3/10/2018.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
     public CategoryServiceImpl( ProductCategoryRepository p) {
    	super();
    	this.productCategoryRepository=p;
	}


    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategory> res = productCategoryRepository.findAllByOrderByCategoryType();
      //  res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    @Override
    public ProductCategory findByCategoryType(Integer categoryType) {
        ProductCategory res = productCategoryRepository.findByCategoryType(categoryType);
        if(res == null) throw new MyException(ResultEnum.CATEGORY_NOT_FOUND);
        return res;
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        List<ProductCategory> res = productCategoryRepository.findByCategoryTypeInOrderByCategoryTypeAsc(categoryTypeList);
       //res.sort(Comparator.comparing(ProductCategory::getCategoryType));
        return res;
    }

    @Override
    @Transactional
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }



}
