package com.webstore.controller;

import com.webstore.responses.Response;
import com.webstore.service.AddressGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/checkout")
public class CheckoutController {
    @Autowired
    AddressGeneralService addressGeneralService;

    @GetMapping(value = "/getAllNova", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAllNova() {
        return Response.ok(addressGeneralService.getAllCity());
    }

    @GetMapping(value = "/getAllByCityRef", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAllNova(@RequestParam(value = "cityRef") String city) {
        return Response.ok(addressGeneralService.getAllByCityRef(city));
    }
}
