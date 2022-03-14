package com.webstore.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webstore.core.base.ComplexBasketStorage;
import com.webstore.core.base.ComplexCompareStorage;
import com.webstore.core.base.WatchHistoryStorage;
import com.webstore.core.entities.*;
import com.webstore.core.entities.enums.OrderState;
import com.webstore.core.entities.json.*;
import com.webstore.core.service.*;
import com.webstore.core.session.attribute.Basket;
import com.webstore.core.session.attribute.CompareProduct;
import com.webstore.core.session.attribute.WatchHistory;
import com.webstore.core.util.Const;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.webstore.core.util.Utils.*;

/**
 * Created by SGurin on 14.07.2016.
 */
@RestController
public class RestControllers {

    @Autowired
    private ProductGroupService productGroupService;

    @Autowired
    private MarkService markService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModificationService modificationService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private SmsInfoService smsInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @RequestMapping(value = "/deleteItem", method = RequestMethod.POST)
    @ResponseBody
    public boolean deleteItem(@RequestBody ItemJson itemJson) throws IOException {
        switch (itemJson.getType()) {
            case "mark":
                return markService.delete(itemJson.getId());
            case "model":
                return modelService.delete(itemJson.getId());
            case "modification":
                return modificationService.delete(itemJson.getId());
            case "catalog":
                return templateService.findListTemplatesByCatalogId(itemJson.getId()) != null ? false : catalogService.delete(itemJson.getId());
            default:
                return false;
        }
    }

    @RequestMapping(value = "/addMarks", method = RequestMethod.POST)
    @ResponseBody
    public boolean addMarks(@RequestBody List<String> markNames) throws IOException {
        try {
            for (String markName : markNames) {
                markService.add(new Mark(markName));
            }
            return true;
        } catch (Exception e) {
            //TODO Make logging
        }
        return false;
    }

    @RequestMapping(value = "/addModels", method = RequestMethod.POST)
    @ResponseBody
    public boolean addModels(@RequestBody List<String> modelContent) throws IOException {
        try {
            int mark_id = Integer.parseInt(modelContent.get(0));
            Mark mark = markService.find(mark_id);

            for (int i = 1; i < modelContent.size(); i++) {
                Model newModel = new Model();
                newModel.setName(modelContent.get(i));
                newModel.setMark(mark);

                modelService.add(newModel);
            }

            return true;
        } catch (Exception e) {
            //TODO Make logging
        }
        return false;
    }

    @RequestMapping(value = "/addModifications", method = RequestMethod.POST)
    @ResponseBody
    public boolean addModifications(@RequestBody List<String> modificationContent) throws IOException {
        try {
            int model_id = Integer.parseInt(modificationContent.get(0));
            Model model = modelService.find(model_id);

            for (int i = 1; i < modificationContent.size(); i++) {
                Modification newModification = new Modification();
                newModification.setName(modificationContent.get(i));
                newModification.setModel(model);

                modificationService.add(newModification);
            }

            return true;
        } catch (Exception e) {
            //TODO Make logging
        }
        return false;
    }

    @RequestMapping(value = "/addSpareParts", method = RequestMethod.POST)
    @ResponseBody
    public boolean addSpareParts(@RequestBody SparePartJson sparePartJson) throws IOException {
        try {
            Catalog catalog = new Catalog();
            catalog.setTitle(sparePartJson.getTitle());
            catalog.setParent(catalogService.find(sparePartJson.getParentId()));
            catalog.setProductGroup(productGroupService.find(sparePartJson.getGroupId()));

            return (catalogService.add(catalog) instanceof Catalog);
        } catch (Exception e) {
            //TODO Make logging
        }
        return false;
    }

    /*Корзина*/
  /*  @RequestMapping(value = "/addToBasket", method = RequestMethod.POST)
    @ResponseBody
    public ComplexBasketStorage addToBasket(@RequestParam(value = "code") String code, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);


        Basket basket = getCurrentBasket(request);

        if (basket == null) {
            basket = new Basket();
            session.setAttribute("basket", basket);

        }

        Product product = productService.findByCode(code);
        basket.addProduct(product);

        ComplexBasketStorage complexBasketStorage = new ComplexBasketStorage(basket, "test for test");


        return complexBasketStorage;
    }

    @RequestMapping(value = "/deleteFromBasket", method = RequestMethod.POST)
    @ResponseBody
    public ComplexBasketStorage deleteFromBasket(@RequestParam(value = "code") String code, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(true);
        Basket basket = getCurrentBasket(request);
        basket.deleteByCode(code);
        if (!basket.getProductList().isEmpty() ){
            session.setAttribute("basket", basket);
        }
        else{
            session.removeAttribute("basket");
        }
        ComplexBasketStorage complexBasketStorage = new ComplexBasketStorage(basket, "synchronize from Server(remove item)");

        return complexBasketStorage;
    }
*/
    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    @ResponseBody
    public SmsInfoJson getJsonOrders(@RequestBody AndroidAuthData androidAuthData) throws Exception {
        SmsInfoJson smsInfoJson;
        //Проверка аутентификации
        if(userService.findUserByEmail(androidAuthData.getLogin()).getPassword().equals(androidAuthData.getPassword())){
            smsInfoJson = smsInfoService.getSmsInfoJson();

            if(smsInfoJson.getOrdersJson().size() == 0){
                smsInfoJson.setErrorMessage(Const.NODATAFOUNDERROR);
            }
            else {
                //Ставим промежуточный статус, чтобы следующий запрос к серверу не захватил эти же заказы
                for(OrderJson orderJson: smsInfoJson.getOrdersJson()){
                    smsInfoService.updateStatus(orderJson.getIdOrder(), OrderState.IN_PROCESS);
                }
            }
        }
        else {
            smsInfoJson = new SmsInfoJson();
            smsInfoJson.setErrorMessage(Const.AUTHERROR);
        }
        return smsInfoJson;
    }

    @RequestMapping(value = "/smsanswer", method = RequestMethod.POST)
    @ResponseBody
    public Boolean setJson(@RequestBody AndroidRequestJson androidRequestJson) throws Exception {
        if(userService.findUserByEmail(androidRequestJson.getAndroidAuthData().getLogin()).getPassword().
                equals(androidRequestJson.getAndroidAuthData().getPassword())){
            if(smsInfoService.updateStatus(androidRequestJson.getIdOrdersList(), OrderState.CLOSED)){
                return true;
            }
        }
        return false;
    }





    @RequestMapping(value = "/getMarkList", method = RequestMethod.GET)
    @ResponseBody
    public List<Mark> getMarkList() throws IOException {
        List<Mark> markList = new ArrayList<>();
        try {
            return markService.getAllMarks();

        } catch (Exception e) {
            //TODO Make logging
        }
        return markList;
    }

    @RequestMapping(value = "/getModelsList", method = RequestMethod.POST)
    @ResponseBody
    public List<Model> getModelsList(@RequestParam(value = "id") String id) throws IOException {
        List<Model> modelList = new ArrayList<>();
        try {
            Mark mark = markService.find(Integer.parseInt(id));
            modelList = mark.getModel();
            return modelList;
        } catch (Exception e) {
            //TODO Make logging
        }
        return modelList;
    }

    @RequestMapping(value = "/getModificationsList", method = RequestMethod.POST)
    @ResponseBody
    public List<Modification> getModificationsList(@RequestParam(value = "id") String id) throws IOException {
        List<Modification> modificationList = new ArrayList<>();
        try {
            Model model = modelService.find(Integer.parseInt(id));
            modificationList = model.getModification();
            return modificationList;
        } catch (Exception e) {
            //TODO Make logging
        }
        return modificationList;
    }

    @RequestMapping(value = "/mainFilter", method = RequestMethod.POST)
    @ResponseBody
    public boolean mainFilter(@RequestBody FilterJson filterJson) throws IOException {
        try {
            //main logic
            return true;
        } catch (Exception e) {
            //TODO Make logging
        }
        return false;
    }

    /*Комментарии*/


    @RequestMapping(
            value = "/comment/loadComments",
            method = RequestMethod.GET,
            produces = {"application/json; charset=UTF-8", "*/*;charset=UTF-8"})
    @ResponseBody
    public String loadComments(@RequestParam(value = "id") String id,
                               @RequestParam(value = "comment") String comment,
                               HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        return getJsonString(commentService.getComments(Integer.parseInt(comment), Integer.parseInt(id)));
    }

    @RequestMapping(value = "/isExistProduct", method = RequestMethod.POST)
    @ResponseBody
    public boolean isExistProduct(@RequestParam(value = "code") String code) throws IOException {
        return productService.findByCode(code) instanceof Product;
    }

    @RequestMapping(value = {"admin/template/{id}/fields"}, method = RequestMethod.GET)
    @ResponseBody
    public Set<TemplateFields> getFieldsByTemplate(@PathVariable("id") int id) throws IOException {
        return templateService.getFieldsByTemplateId(id);
    }

    /*--------------------PRODUCER---------------------*/
    @RequestMapping(value = {"admin/producer/get"}, method = RequestMethod.GET)
    @ResponseBody
    public List<Producer> getProducers() {
        return producerService.getProducers();
    }

    /*--------------------PRODUCT----------------------*/

    @RequestMapping(value = {"admin/product/{id}/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public boolean removeProduct(@PathVariable("id") int id) {
        return productService.delete(id);
    }

    @RequestMapping(value = {"admin/product/{id}/photo/delete/{photoId}"}, method = RequestMethod.POST)
    public void removePhoto(@PathVariable("id") int id, @PathVariable("photoId") int photoId) {
        Product product = productService.find(id);
        productService.deletePhotoById(product, photoId);
    }

    @RequestMapping(value = {"admin/product/{id}/toggleVisible"}, method = RequestMethod.POST)
    @ResponseBody
    public boolean toggleVisibleProduct(@PathVariable("id") int id) {
        Product product = productService.find(id);
        product.setVisible(!product.isVisible());
        productService.update(product);
        return true;
    }

    @RequestMapping(value = {"admin/product/{id}/get"}, method = RequestMethod.GET)
    @ResponseBody
    public Product getProduct(@PathVariable("id") int id) {
        return productService.find(id);
    }

    @RequestMapping(
            value = {"admin/product/merge"},
            method = RequestMethod.POST,
            consumes = {"multipart/form-data"}
    )
    @ResponseBody
    public String mergeProduct(
            @RequestPart("product") String json,
            @RequestPart(value = "file") MultipartFile[] files) {
        try {
            JsonNode node = new ObjectMapper().readTree(getUTF8(json));

            Producer producer = producerService.find(node.path("producer").asInt());
            if (producer == null)
                throw new NullPointerException("Производитель не найден");

            Template template = templateService.find(node.path("template").asInt());
            if (template == null)
                throw new NullPointerException("Шаблон не найден");

            Product product = new Product();
            if(node.get("id").asInt() != 0)
                product = productService.find(node.get("id").asInt());

            product.setTitle(node.path("name").asText());
            product.setCode(node.path("code").asText());
            product.setPrice(node.path("price").asLong());
            product.setProducer(producer);
            product.setDescription(node.path("description").asText());
            product.setTemplate(template);

            productService.mergeFields(product, node.path("fields"));
            productService.mergeApplicability(product, node.path("applicabilities"));
            productService.mergeFiles(product, files, "jpg");

            product = productService.update(product);
            return String.valueOf(product.getId());
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    /*--------------------ORDER----------------------*/

    @RequestMapping(value = {"admin/order/{id}/get"}, method = RequestMethod.GET)
    @ResponseBody
    public OrderJson getOrder(@PathVariable("id") int id) {

        Order order =  orderService.find(id);
        OrderJson orderJson = new OrderJson(order);

        return orderJson;
    }

    @RequestMapping(
            value = {"admin/order/merge"},
            method = RequestMethod.POST,
            consumes = {"multipart/form-data"}
    )
    @ResponseBody
    public String mergeOrder(
            @RequestPart("order") String json) {
        try {
            JsonNode node = new ObjectMapper().readTree(getUTF8(json));

            Order order = new Order();
            int orderId = node.get("order_id").asInt();
            if(orderId != 0){
                order = orderService.find(orderId);
            }

            order.setSum(node.path("sum").asDouble());

            order = orderService.update(order);
            orderDetailsService.updateInOrder(orderId, node.get("orderDetails"));

            return String.valueOf(order.getOrder_id());
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении заказа");
            return e.getMessage();
        }
    }

/*-------------------------OrderDetails--------------------------*/

    @RequestMapping(value = {"admin/orderDetails/{id}/get"}, method = RequestMethod.GET)
    @ResponseBody
    public List<OrderDetailsJson> getOrderDetails(@PathVariable("id") int id) {

        List<OrderDetails> orderDetails =  orderDetailsService.getOrderDetails(id);
        List<OrderDetailsJson> orderDetailsJson = OrderDetailsJson.createOrderDetailsJsonList(orderDetails);

        return orderDetailsJson;
    }

/*---------------------------------------------------------------*/
    @RequestMapping(value = {"admin/template/update"}, method = RequestMethod.POST)
    public String updateTemplate(HttpServletRequest request) throws IOException, JSONException {
        String json = getStringFromInputStream(request.getInputStream());

        JsonNode node = new ObjectMapper().readTree(json);

        Template template = templateService.find(node.path("id").asInt());
        if (template == null) {
            template = new Template();
        }

        template.setName(node.path("name").asText());
        template.setState(node.path("state").asInt());
        template.setCatalog(catalogService.find(node.path("catalog_id").asInt()));

        JsonNode fields = node.path("templateFields");

        for (final JsonNode field : fields) {

            int field_id = field.path("id").asInt();

            if (field_id < 0) {
                templateService.deleteField(template, Math.abs(field_id));
                continue;
            }

            TemplateFields templateField = new TemplateFields();
            templateField.setId(field_id);
            templateField.setName(field.path("name").asText());
            templateField.setOrder(field.path("order").asInt());
            templateField.setVisible(field.path("visible").asInt());

            JsonNode values = field.path("templateFieldsValues");
            for (final JsonNode value : values) {
                int field_value_id = value.path("id").asInt();

                if (field_value_id < 0) {
                    templateService.deleteFieldValue(template, Math.abs(field_value_id));
                    continue;
                }
                TemplateFieldsValue templateFieldsValue = new TemplateFieldsValue();
                templateFieldsValue.setId(field_value_id);
                templateFieldsValue.setValue(value.path("value").asText());
                templateField.addTemplateFieldsValues(templateFieldsValue);
            }
            template.addTemplateFields(templateField);
        }

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("template", new ObjectMapper().writeValueAsString(templateService.update(template)));
        jsonObject.put("catalog", new ObjectMapper().writeValueAsString(catalogService.getAllCatalogList()));

        return jsonObject.toString();

        //return new ObjectMapper().writeValueAsString(templateService.update(template));

    }

    @RequestMapping(value = {"admin/template/{id}"}, method = RequestMethod.GET)
    public String getTemplateById(@PathVariable("id") int id) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("template", new ObjectMapper().writeValueAsString(templateService.find(id)));
        jsonObject.put("catalog", new ObjectMapper().writeValueAsString(catalogService.getAllCatalogList()));

        return jsonObject.toString();
    }

    @RequestMapping(value = {"admin/template/get"}, method = RequestMethod.GET)
    public List<Template> getTemplates() {
        return templateService.getAllTemplates();
    }

    @RequestMapping(value = {"admin/template/delete/{id}"}, method = RequestMethod.POST)
    public String deleteTemplate(@PathVariable("id") int id) throws IOException {
        if (templateService.delete(id)) {
            return "{'status': 'success'}";
        }
        return "{'status': 'error'}";
    }



}
