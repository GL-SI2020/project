package tekup.de.eshop.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import tekup.de.eshop.model.ProductCategory;
import tekup.de.eshop.model.ProductInfo;
import tekup.de.eshop.service.CategoryService;
import tekup.de.eshop.service.ProductService;
import tekup.de.eshop.vo.response.CategoryPage;

/**
 * Created By Zhu Lin on 3/10/2018.
 */
@RestController
@CrossOrigin
public class CategoryController {
   
    private CategoryService categoryService;
   
    private ProductService productService;
    @Autowired
    public CategoryController(CategoryService c,ProductService p) {
    	super();
    	this.categoryService=c;
    	this.productService=p;
		
	}

    /**
     * Show products in category
     *
     * @param categoryType
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/category/{type}")
    public CategoryPage showOne(@PathVariable("type") Integer categoryType,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "3") Integer size) {

        ProductCategory cat = categoryService.findByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInCategory = productService.findAllInCategory(categoryType, request);
        CategoryPage tmp = new CategoryPage("", productInCategory);
        tmp.setCategory(cat.getCategoryName());
        return tmp;
    }
    @PostMapping("/category/new")
    public ResponseEntity create(@Valid @RequestBody ProductCategory entity,
            BindingResult bindingResult) {
			ProductCategory categoryIdExists = categoryService.findByCategoryType(entity.getCategoryType());
			if (categoryIdExists != null) {
			bindingResult
			.rejectValue("productId", "error.product",
			   "There is already a product with the code provided");
			}
			if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult);
			}
			return ResponseEntity.ok(categoryService.save(entity));
    }
}
