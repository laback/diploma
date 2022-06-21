package com.by.gomel.gstu.service;

import com.by.gomel.gstu.model.AnalogGroup;
import com.by.gomel.gstu.repository.AnalogGroupRepository;
import com.by.gomel.gstu.viewModel.DetailViewModel;
import com.by.gomel.gstu.viewModel.OrderDetailViewModel;
import com.by.gomel.gstu.model.Detail;
import com.by.gomel.gstu.repository.DetailRepository;
import com.by.gomel.gstu.repository.OrderDetailRepository;
import com.by.gomel.gstu.repository.OrderRepository;
import com.by.gomel.gstu.util.PageUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DetailService {

    private final DetailRepository detailRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    private final AnalogGroupRepository analogGroupRepository;

    @Autowired
    public DetailService(DetailRepository detailRepository, OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, AnalogGroupRepository analogGroupRepository) {
        this.detailRepository = detailRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.analogGroupRepository = analogGroupRepository;
    }

    public List<Detail> getAllDetails(int page, String detailName, Model model){
        var details = detailRepository.findAll();

        if(detailName != null && !detailName.isBlank()){
            details = details.stream().filter(detail -> detail.getDetailName().toLowerCase().contains(detailName.toLowerCase())).collect(Collectors.toList());
        }

        model.addAttribute("maxPages", getMaxPages(details));

        return PageUtils.getAllEntitiesByPage(details, page);
    }

    public List<Detail> getAllDetails(int page, String detailName, List<Detail> details, Model model){

        if(detailName != null && !detailName.isBlank()){
            details = details.stream().filter(detail -> detail.getDetailName().toLowerCase().contains(detailName.toLowerCase())).collect(Collectors.toList());
        }

        model.addAttribute("maxPages", getMaxPages(details));

        return PageUtils.getAllEntitiesByPage(details, page);
    }

    public List<Detail> getAllDetails(){
        return detailRepository.findAll();
    }

    public List<Detail> getAllDetailsInStock(){
        return detailRepository.findAll().stream().filter(detail -> detail.getDetailCount() > 0).collect(Collectors.toList());
    }

    public List<Detail> getAnalogsByVinCodeAndPage(String vinCode, int page, Model model) {

        var detail = getDetailByVendorCode(vinCode);

        List<Detail> analogs = new ArrayList<>();

        if(detail != null){
            var analogGroup = analogGroupRepository.getAnalogGroupByAnalogGroupName(detail.getAnalogGroup().getAnalogGroupName());

            analogs = detailRepository.getDetailsByAnalogGroup(analogGroup);

            model.addAttribute("maxPages", getMaxPages(analogs));
        }

        return PageUtils.getAllEntitiesByPage(analogs, page);
    }

    public List<Detail> getAnalogsByVinCodeAndPage(String vinCode, int page, List<Detail> details, Model model) {

        var detail = getDetailByVendorCode(vinCode);

        List<Detail> analogs = new ArrayList<>();

        if(details != null){
            var analogGroup = analogGroupRepository.getAnalogGroupByAnalogGroupName(detail.getAnalogGroup().getAnalogGroupName());

            analogs = details.stream().filter(x -> x.getAnalogGroup().equals(analogGroup)).collect(Collectors.toList());


            model.addAttribute("maxPages", getMaxPages(analogs));
        }

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

        List<Element> finalElements = new ArrayList<>();

        for(int i = 1; i < elements.size(); i+=3){
            finalElements.add(elements.get(i));
        }
        finalElements.forEach(element -> {
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

    public void addDetail(DetailViewModel detailViewModel) throws IOException {
        var detail = detailViewModel.getDetail();
        if(!detailViewModel.getStringAttributes().isEmpty()){
            detail.setDetailAttributes(Arrays.stream(detailViewModel.getStringAttributes().split(","))
                    .map(entry -> entry.split(":"))
                    .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1])));
        }
        detail.setAnalogGroup(getAnalogGroupByArticleOrNew(detail.getDetailVendorCode()));
        detailRepository.save(detail);
    }

    public List<OrderDetailViewModel> getDetailsByOrderId(Long orderId){
        var ordersDetail = orderDetailRepository.getAllByOrder(orderRepository.getById(orderId));

        return ordersDetail.stream().map(orderDetail -> new OrderDetailViewModel(orderDetail.getDetail(), orderDetail.getCount())).collect(Collectors.toList());
    }

    private AnalogGroup getAnalogGroupByArticleOrNew(String article) throws IOException {
        article = article.replace(" ", "");
        Document doc = Jsoup.connect("https://vincode.by/parts/" + article).get();

        List<Element> elements;

        if (doc.getElementsByClass("tooltip").isEmpty()) {
            doc = Jsoup
                    .connect("https://vincode.by" + doc.getElementsByClass("data-row")
                            .get(0)
                            .childNode(4)
                            .childNode(1)
                            .attr("href"))
                    .get();
        }
        elements = distinctDuplicates(doc.getElementsByClass("tooltip"));

        List<String> codes = elements.stream().map(element -> element.childNode(0).outerHtml()).collect(Collectors.toList());

        var analogs = detailRepository.getDetailsByArticles(codes);

        AnalogGroup result;

        if(analogs.isEmpty()){
            String groupName = UUID.randomUUID().toString();

            analogGroupRepository.save(new AnalogGroup(groupName));

            result = analogGroupRepository.getAnalogGroupByAnalogGroupName(groupName);
        } else{
            result = analogs.get(0).getAnalogGroup();
        }

        return result;
    }
}
