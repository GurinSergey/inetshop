package com.webstore.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webstore.session.attribute.Basket;
import com.webstore.session.attribute.CompareProduct;
import com.webstore.session.attribute.WatchHistory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * Created by DVaschenko on 20.07.2016.
 */
public final class Utils {
    private Utils() {
    }

    public static Basket getCurrentBasket(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (Basket) session.getAttribute("basket");
    }

    public static CompareProduct getCurrentCompare(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        return (CompareProduct) session.getAttribute("compare");
    }

    public static WatchHistory getCurrentWatchHistory(HttpServletRequest request,HttpSession session) {
      //LAO двойной гет не нужен  HttpSession session = request.getSession(true);
        return (WatchHistory) session.getAttribute("watchHistory");
    }

    public static String getDateByFormat(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }

    public static String getJsonString(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ObjectMapper().createObjectNode().toString();
    }

    public static String getJsonString(Object o, Class<?> serializationView) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        try {
            return mapper.writerWithView(serializationView).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ObjectMapper().createObjectNode().toString();
    }

    public static String getStringFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        return reader.readLine();
    }

    public static boolean saveImageJpg(String path, MultipartFile file, String title) throws IOException {
        File productDir = new File(path);
        if (!productDir.exists())
            productDir.mkdir();

        FileCopyUtils.copy(file.getBytes(), new File(productDir + "\\" + title + ".jpg"));
        return true;
    }

    public static String getUTF8(String str) throws UnsupportedEncodingException {
        return URLDecoder.decode(str, "UTF-8");
    }

    public static void checkExistsCommonCatalog(String path) {
        File catalogDir = new File(path);
        if (!catalogDir.exists())
            catalogDir.mkdir();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "_");
    }

    public static Date dateTimeFormat(String datetime) {

        SimpleDateFormat date_format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String dateInString = datetime;
        Date date = null;
        try {
            date = date_format.parse(dateInString);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    public static String formatDateToString(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    public static Boolean isAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return true;
        }
        return false;
    }
}
