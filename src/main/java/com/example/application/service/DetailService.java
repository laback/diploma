package com.example.application.service;

import com.example.application.model.Detail;
import com.example.application.repository.CategoryRepository;
import com.example.application.repository.DetailRepository;
import com.example.application.repository.OrderDetailRepository;
import com.example.application.repository.OrderRepository;
import com.example.application.util.PageUtils;
import com.example.application.viewModel.OrderDetailViewModel;
import org.hibernate.mapping.Collection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetailService {

    private final DetailRepository detailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public DetailService(DetailRepository detailRepository, OrderDetailRepository orderDetailRepository, OrderRepository orderRepository) {
        this.detailRepository = detailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
    }

    public List<Detail> getAllDetails(int page, String detailName){
        var details = detailRepository.findAll();

        if(detailName != null && !detailName.isBlank()){
            details = details.stream().filter(detail -> detail.getDetailName().toLowerCase().contains(detailName.toLowerCase())).collect(Collectors.toList());
        }

        return PageUtils.getAllEntitiesByPage(details, page);
    }

    public List<Detail> getAllDetails(){
        return detailRepository.findAll();
    }

    public List<Detail> getAllDetailsInStock(){
        return detailRepository.findAll().stream().filter(detail -> detail.getDetailCount() > 0).collect(Collectors.toList());
    }

    public List<Detail> getAnalogsByVinCodeAndPage(String vinCode, int page) throws IOException {

        Comparator<Detail> comparator = Comparator.comparing(Detail::getDetailVendorCode);

        Document doc = Jsoup.connect("https://www.todx.ru/search.html?article=" + vinCode).get();

        List<Element> elements = null;

        if (!doc.getElementsByClass("alt-step-table__cell alt-step-table__cell_type_action_alt").isEmpty()) {
            var test = doc.getElementsByClass("alt-step-table__cell alt-step-table__cell_type_action_alt").get(0).child(0);
            doc = Jsoup.connect("https://www.todx.ru" + doc.getElementsByClass("alt-step-table__cell alt-step-table__cell_type_action_alt").get(0).child(0).attr("href")).get();
        }
        elements = distinctDuplicates(doc.getElementsByClass("search-results__article_main"));

        List<Detail> analogs = new ArrayList<>();

        elements.forEach(element -> {

            var detail = getDetailByVendorCode(element.childNode(0).outerHtml());

            if(detail != null && detail.getDetailCount() > 0){
                analogs.add(detail);
            }
        });

        return PageUtils.getAllEntitiesByPage(analogs, page);
    }

    private Detail getDetailByVendorCode(String vendorCode){
        return detailRepository.getDetailByDetailVendorCode(vendorCode);
    }

    public List<Detail> getAllDetailByCategory(String categoryName, int page){
        return PageUtils.getAllEntitiesByPage(detailRepository.findAll().stream().filter(detail -> detail.getCategory().getCategoryName().equals(categoryName)).collect(Collectors.toList()), page);
    }

    public void increaseDetailsCountById(int id){
        var detail = detailRepository.getById((long) id);

        detail.setDetailCount(detail.getDetailCount() + 1);

        detailRepository.save(detail);
    }

    public void decreaseDetailsCountById(int id){
        var detail = detailRepository.getById((long) id);

        detail.setDetailCount(detail.getDetailCount() - 1);

        detailRepository.save(detail);
    }

    public Detail getDetailById(long id){
        return detailRepository.getById(id);
    }

    public long getMaxPages(List<Detail> details){
        return PageUtils.getMaxPages(details);
    }

    private List<Element> distinctDuplicates(List<Element> elements){
        List<Element> result = new ArrayList<>();

        elements.forEach(element -> {
            boolean isEntered = false;

            for(var resultElement : result){
                if(resultElement.childNode(0).outerHtml().equals(element.childNode(0).outerHtml())){
                    isEntered = true;
                    break;
                }
            }

            if(!isEntered){
                result.add(element);
            }
        });

        return result;
    }

    public void addDetail(Detail detail){
        if(!detail.getStringAttributes().isEmpty()){
            detail.setDetailAttributes(Arrays.stream(detail.getStringAttributes().split(","))
                    .map(entry -> entry.split(":"))
                    .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1])));
        }
        detailRepository.save(detail);
    }

    public List<OrderDetailViewModel> getDetailsByOrderId(Long orderId){
        var ordersDetail = orderDetailRepository.getAllByOrder(orderRepository.getById(orderId));

        return ordersDetail.stream().map(orderDetail -> new OrderDetailViewModel(orderDetail.getDetail(), orderDetail.getCount())).collect(Collectors.toList());
    }
}
