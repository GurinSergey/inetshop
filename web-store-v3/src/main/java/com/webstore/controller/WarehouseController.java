package com.webstore.controller;

import com.webstore.base.stateStepBase.StepService;
import com.webstore.domain.*;
import com.webstore.domain.enums.InvoiceState;
import com.webstore.domain.enums.ResultCode;
import com.webstore.domain.json.BasketJson;
import com.webstore.repository.WarehouseRepo;
import com.webstore.responses.Response;
import com.webstore.security.services.TokenAuthService;
import com.webstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
@RequestMapping(value = "/warehouse")
public class WarehouseController {
    @Autowired
    ContractorService contractorService;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoicePartService invoicePartService;

    @Autowired
    ProductService productService;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private ProductRestService productRestService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailsService orderDetailsService;


    @GetMapping(value = "/getAllContractor")
    @ResponseBody
    public Response getAllContractor() {

        return Response.ok(contractorService.getAll());

    }


    @RequestMapping(value = "/getAutoCompleteContractor", method = RequestMethod.POST)
    @ResponseBody
    public Response getAutoCompleteContractor(@RequestBody Contractor contractor) {

        List<Contractor> contractorList = contractorService.getByMatchers(contractor);

        //   return Response.ok(contractorService.getAll());
        return Response.ok(contractorList);
    }


    @RequestMapping(value = "/getAutoCompleteProduct", method = RequestMethod.POST)
    @ResponseBody
    public Response getAutoCompleteProduct(@RequestBody Product product) {

        List<Product> productList = productService.getByMatchers(product);

        //   return Response.ok(contractorService.getAll());
        return Response.ok(productList);
    }


    @GetMapping(value = "/getAllWarehouse")
    @ResponseBody
    public Response getAllWarehouse() {
        return Response.ok(warehouseService.getAll());
    }


    @GetMapping(value = "/getAllInvoiceByWarehouse")
    @ResponseBody
    public Response getAllInvoiceByWarehouse(@RequestParam(value = "id") long id) {
        Warehouse warehouse = warehouseService.getOne(id);

        return Response.ok(invoiceService.findAllByWarehouse(warehouse));
    }


    @ResponseBody
    @RequestMapping(value = {"/getInvoicePartByArrayId"}, method = RequestMethod.POST)
    public Response getInvoicePartByArrayId(@RequestParam("invoices") Long[] invoices) {
        return Response.ok(invoicePartService.findInvoicePartByArrayId(invoices));
    }

    @ResponseBody
    @RequestMapping(value = {"/getInvoicePartById"}, method = RequestMethod.POST)
    public Response getInvoicePartById(@RequestParam("invoice") Long invoice) {
        return Response.ok(invoicePartService.findInvoicePartById(invoice));
    }

    @ResponseBody
    @RequestMapping(value = {"/calcInitialSumById"}, method = RequestMethod.POST)
    public Response calcInitialSumById(@RequestParam("invoice") Long invoice) {
        return Response.ok(invoicePartService.calcInitialSumById(invoice));
    }

    @ResponseBody
    @GetMapping(value = "/getInvoice")
    public Response getInvoice(@RequestParam(value = "id") Long id) {
        return Response.ok(invoiceService.findById(id));
    }

    @RequestMapping(value = "/updateContractor", method = RequestMethod.POST)
    @ResponseBody
    public Response updateContractor(@RequestBody Contractor contractor) {

        contractorService.update(contractor);


        return Response.ok(contractor);
    }

    @RequestMapping(value = "/updateInvoice", method = RequestMethod.POST)
    @ResponseBody
    public Response updateInvoice(@RequestBody Invoice invoice) {

        User user = tokenAuthService.getCurrentUser();
        invoice.setOper(user);
        Invoice oldInvoice = invoiceService.findById(invoice.getId());

        if (oldInvoice.getState() == InvoiceState.CLOSED){
            oldInvoice.setContractor(invoice.getContractor());
            invoiceService.update(oldInvoice);
            return Response.ok(oldInvoice.getId());
        }
       /* Warehouse warehouse = warehouseService.getOne(invoice.getWarehouse().getId());
        invoice.setWarehouse(warehouse);*/

        invoiceService.update(invoice);

        return Response.ok(invoice.getId());
    }


    @RequestMapping(value = "/insertInvoice", method = RequestMethod.POST)
    @ResponseBody
    public Response insertInvoice(@RequestBody Invoice invoice) {
        User user = tokenAuthService.getCurrentUser();
        invoice.setOper(user);
       /* Warehouse warehouse = warehouseService.getOne(invoice.getWarehouse().getId());
        invoice.setWarehouse(warehouse);*/
        invoice.setState(InvoiceState.NEW);
        invoiceService.update(invoice);
        //  invoice.getContractor();
        return Response.ok(invoice.getId());
    }

    @RequestMapping(value = "/insertInvoicebyOrder", method = RequestMethod.POST)
    @ResponseBody
    public Response insertInvoicebyOrder(@RequestParam("warehouseId") Long orderId) {
        User user = tokenAuthService.getCurrentUser();
        Order order = orderService.find(orderId);
        List<OrderDetails> orderDetails = orderDetailsService.find(order);
        Warehouse warehouse = warehouseService.getOne(1L);

        Invoice invoice = new Invoice();
        invoice.setSum(order.getSum());
        invoice.setOrder(order);
        invoice.setWarehouse(warehouse);
        invoice.setOper(user);
        invoice.setState(InvoiceState.WAIT);

         //add to invoicePart
        for (OrderDetails orderDetail : orderDetails) {
            invoice.setInvoicePart(new InvoicePart(invoice, orderDetail.getProduct(), orderDetail.getPrice(), orderDetail.getQuantity()));
        }

        invoiceService.update(invoice);

        return Response.ok("OK");
    }



    @RequestMapping(value = "/insertInvoicePart", method = RequestMethod.POST)
    @ResponseBody
    public Response insertInvoicePart(@RequestBody InvoicePart invoicePart) {
        invoicePart.setState(InvoiceState.NEW);
        invoicePartService.update(invoicePart);
        return Response.ok("OK");
    }



    @RequestMapping(value = "/updateInvoicePart", method = RequestMethod.POST)
    @ResponseBody
    public Response updateInvoicePart(@RequestBody InvoicePart invoicePart) {

     InvoicePart oldInvoicePart = invoicePartService.findOne(invoicePart.getId());
        if (oldInvoicePart.getState() == InvoiceState.CLOSED){
            return Response.ok(ResultCode.READ_ONLY,(Object)"позиция уже оформлена на склад, изменить нельзя");
        }

        invoicePartService.update(invoicePart);
        return Response.ok("OK");
    }

    @ResponseBody
    @RequestMapping(value = {"/getCurrentProductRestAll"}, method = RequestMethod.POST)
    public Response getCurrentProductRestAll(@RequestParam("restDate")  @DateTimeFormat(pattern="yyyy-MM-dd") Date restDate,
                                             @RequestParam("warehouseId") int warehouseId) {
        return Response.ok(productRestService.getCurrentProductRestAll(restDate, warehouseId));
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = {"/checkInPurchaseInvoiceById"}, method = RequestMethod.POST)
    public Response checkInPurchaseInvoiceById(@RequestParam("invoice") Long invoiceId) {

        Invoice invoice = invoiceService.findById(invoiceId);
        invoice.setSum(invoicePartService.calcInitialSumById(invoiceId));
        for (InvoicePart invoicePart : invoice.getInvoicePart()) {
            productRestService.addPurchase(invoice.getInvoiceDate(),
                    invoice.getWarehouse(),
                    invoicePart);
            invoicePart.setActive(true);
            invoicePart.setState(InvoiceState.CLOSED);
          //  invoicePartService.update(invoicePart);
        }
        invoice.setState(InvoiceState.CLOSED);
        invoiceService.update(invoice);

        return Response.ok("ok");
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = {"/checkInSalesInvoiceById"}, method = RequestMethod.POST)
    public Response checkInSalesInvoiceById(@RequestParam("invoice") Long invoiceId) {

        Invoice invoice = invoiceService.findById(invoiceId);
        for (InvoicePart invoicePart : invoice.getInvoicePart()) {
            productRestService.addSales(invoice.getInvoiceDate(),
                    invoice.getWarehouse(),
                    invoicePart);
            invoicePart.setState(InvoiceState.CLOSED);
           // invoicePartService.update(invoicePart);
        }
        invoice.setState(InvoiceState.CLOSED);
        invoiceService.update(invoice);

        return Response.ok("ok");
    }








}

