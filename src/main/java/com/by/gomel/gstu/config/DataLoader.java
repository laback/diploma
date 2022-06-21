package com.by.gomel.gstu.config;

import com.by.gomel.gstu.model.Category;
import com.by.gomel.gstu.model.Detail;
import com.by.gomel.gstu.model.Order;
import com.by.gomel.gstu.model.User;
import com.by.gomel.gstu.service.*;
import com.by.gomel.gstu.viewModel.DetailViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Заполняет базу данных, если она пустая
@Component
public class DataLoader implements ApplicationRunner {

    private final UserService userService;

    private final CategoryService categoryService;

    private final RoleService roleService;

    private final DetailService detailService;

    private final OrderService orderService;

    private final OrderDetailService orderDetailService;

    private static final String COMMA = ",";

    @Autowired
    public DataLoader(UserService userService, CategoryService categoryService, RoleService roleService, DetailService detailService, OrderService orderService, OrderDetailService orderDetailService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.roleService = roleService;
        this.detailService = detailService;
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }

    @Override
    public void run(ApplicationArguments args) throws MessagingException, IOException {
        addRolesIfAbsent();
        addAdminIfAbsent();
        addUsersIfAbsent();
        addCategoriesIfAbsent();
        var details = addDetailsIfAbsent();
        var orders = addOrdersIfAbsent();
        addDetailsInOrderIfAbsent(details, orders);
    }

    private void addDetailsInOrderIfAbsent(List<Detail> details, List<Order> orders) {
        if(orderDetailService.getAll().isEmpty() && details != null && orders != null){
            orders.get(0).setOrderCost(orderDetailService.setDetailsInOrder(
                    details.get(0).getId() + COMMA + 10 + COMMA + details.get(1).getId() + COMMA + 20,
                    orders.get(0)
            ));

            orders.get(1).setOrderCost(orderDetailService.setDetailsInOrder(
                    details.get(2).getId() + COMMA + 10 + COMMA + details.get(3).getId() + COMMA + 20,
                    orders.get(1)
            ));

            orders.get(2).setOrderCost(orderDetailService.setDetailsInOrder(
                    details.get(4).getId() + COMMA + 10 + COMMA + details.get(0).getId() + COMMA + 20,
                    orders.get(2)
            ));

            orders.get(3).setOrderCost(orderDetailService.setDetailsInOrder(
                    details.get(1).getId() + COMMA + 10 + COMMA + details.get(2).getId() + COMMA + 20,
                    orders.get(3)
            ));

            orders.get(4).setOrderCost(orderDetailService.setDetailsInOrder(
                    details.get(3).getId() + COMMA + 10 + COMMA + details.get(4).getId() + COMMA + 20,
                    orders.get(4)
            ));

            orderService.save(orders);
        }
    }

    private Map<String, List<User>> getCustomerAndEmployees(){

        List<User> employees = new ArrayList<>();
        List<User> customers = new ArrayList<>();

        for(var user : userService.getAll()){
            if (user.getRole().getId() == roleService.getCustomerRoleId()) {
                customers.add(user);
            } else {
                employees.add(user);
            }
        }

        return Map.of("customers", customers, "employees", employees);
    }

    private List<Order> addOrdersIfAbsent() {
        List<Order> orders = null;

        if(orderService.getAllOrders().isEmpty()){
            var allUsers = getCustomerAndEmployees();

            var customers = allUsers.get("customers");
            var employees = allUsers.get("employees");

            orderService.addOrder(new Order(
                    customers.get(0),
                    employees.get(0),
                    LocalDate.now()
            ));

            orderService.addOrder(new Order(
                    customers.get(1),
                    employees.get(1),
                    LocalDate.now().plusDays(1)
            ));

            orderService.addOrder(new Order(
                    customers.get(2),
                    employees.get(0),
                    LocalDate.now().plusDays(1)
            ));

            orderService.addOrder(new Order(
                    customers.get(1),
                    employees.get(0),
                    LocalDate.now().plusDays(1)
            ));

            orderService.addOrder(new Order(
                    customers.get(0),
                    employees.get(1),
                    LocalDate.now().plusDays(1)
            ));

            orders = orderService.getAllOrders();
        }

        return orders == null ? orderService.getAllOrders().stream().limit(5).collect(Collectors.toList()) : orders;
    }

    private List<Detail> addDetailsIfAbsent() throws IOException {
        List<Detail> details = null;

        if(detailService.getAllDetails().isEmpty()){

            Category accum = categoryService.getCategoryByName("Аккумулятор");
            Category complect = categoryService.getCategoryByName("Комплект ГРМ");
            Category remen = categoryService.getCategoryByName("Ремень ГРМ");

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        accum,
                        "0 092 S30 070",
                        "EXIDE",
                        "Premium аккумулятор 12V 72Ah 720A ETN 0(R+) B13 278x175x175 16,5kg",
                        100,
                        300
                    ),
                    getAccumAttribute(12, 72, 278, 176, 175, 0, "B13", 720, "EN")
                )
            );

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        accum,
                        "EC652",
                        "EXIDE",
                        "Батарея аккумуляторная \"Classic\", 12в 65а/ч",
                        50,
                        240
                    ),
                    getAccumAttribute(12, 65, 278, 175, 175, 0, "B13", 540, "EN")
                )
            );

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        accum,
                        "EA640",
                        "EXIDE",
                        "Аккумулятор",

                        73,
                        310
                    ),
                    getAccumAttribute(12, 64, 242, 175, 190, 0, "B13", 640, "EN")
                )
            );

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        accum,
                        "0092M60180",
                        "BOSCH",
                        "Аккумулятор для мототехники BOSCH MOBA AGM M6 12V 12AH 200A (YTX14-4/YTX14-BS) 152x88x147mm 5.02kg",
                        42,
                        225
                    ),
                    getAccumAttribute(12, 12, 152, 88, 147, 1, "B00", 200, "Y5")
                )
            );

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        accum,
                        "AGM1212F",
                        "EXIDE",
                        "Аккумулятор",
                        56,
                        150
                    ),
                    getAccumAttribute(12, 12, 100, 100, 150, 3, "B0", 150, "Y5")
                )
            );

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        complect,
                        "KTB259",
                        "DAYCO",
                        "Комплект ремня ГРМ",
                        30,
                        120
                    ),
                    ""
                )
            );

            detailService.addDetail(
                new DetailViewModel(
                    new Detail(
                        remen,
                        "5244XS",
                        "GATES",
                        "Ремень ГРМ",
                        40,
                        35
                    ),
                    getRemenAttribute("STT-1", "Стекловолокно", "HNBR (Hydrierter Acryl-Nitril-Butadien-Kautschuk)", "черный", 148, 25, 9, 1410)
                )
            );

            details = detailService.getAllDetails();
        }

        return details == null ? detailService.getAllDetails().stream().limit(7).collect(Collectors.toList()) : details;
    }

    private String getRemenAttribute(String param1, String param2, String param3, String param4, int param5, int param6, int param7, int param8){
        return String.format("\"Номер рекомендуемого специального инструмента\" : \"%s\", " +
                        "\"Материал\" : \"%s\"," +
                        "\"Материал\" : \"%s\"," +
                        "\"Цвет\" : \"%s\"," +
                        "\"Число зубцов\" : \"%d\"," +
                        "\"Ширина [мм]\" : \"%d\"," +
                        "\"Межзубчатое расстояние [мм]\" : \"%d\"," +
                        "\"Длина [мм]\" : \"%d\"",
                param1, param2, param3, param4, param5, param6, param7, param8);
    }

    private String getAccumAttribute(int param1, int param2, int param3, int param4, int param5, int param6, String param7, int param8, String param9){
        return String.format("\"Напряжение [В]\" : \"%d\", " +
                "\"Емкость батареи [А·ч]\" : \"%d\"," +
                "\"Длина [мм]\" : \"%d\"," +
                "\"Ширина [мм]\" : \"%d\"," +
                "\"Высота [мм]\" : \"%d\"," +
                "\"Расположение полюсных выводов\" : \"%d\"," +
                "\"Исполнение днищевой планки\" : \"%s\"," +
                "\"Ток холодного пуска EN [A]\" : \"%d\"," +
                "\"Вид зажима цепи\" : \"%s\"",
                param1, param2, param3, param4, param5, param6, param7, param8, param9);
    }

    private void addUsersIfAbsent() throws MessagingException {
        if(userService.getAll().size() == 1){
            long employee = roleService.getEmployeeRoleId();
            long customer = roleService.getCustomerRoleId();

            List<User> users = List.of(
                    new User("Егоров", "Евгений", "Андреевич", "employee10000@mail.ru", employee),
                    new User("Ларчик", "Кирилл", "Игоревич", "customer10000@mail.ru", customer),
                    new User("Зайцев", "Александр", "Викторович", "customer10001@mail.ru", customer),
                    new User("Леоненко", "Анастасия", "Николаевна", "customer10002@mail.ru", customer)
            );

            userService.addUsers(users);
        }
    }

    private void addRolesIfAbsent() {
        if(roleService.getAll().isEmpty()){
            roleService.addRoleByName("ROLE_ADMIN");
            roleService.addRoleByName("ROLE_EMPLOYEE");
            roleService.addRoleByName("ROLE_CUSTOMER");

        }
    }

    //Добавляет администратора, если его нет
    private void addAdminIfAbsent() throws MessagingException {
        if(userService.getAll().isEmpty()){
            userService.add(new User("Коршиков", "Никита", "Викторович", "labac2001@mail.ru", roleService.getAdminRoleId()));
        }

    }

    private void addCategoriesIfAbsent(){

        if(categoryService.getAllCategories().isEmpty()) {
            addParentCategories();

            List<Category> daughterCategories = getDaughterCategories();

            int beginIndex = 0;

            beginIndex = setParentCategory(daughterCategories, "Расходники", beginIndex, 6);
            beginIndex = setParentCategory(daughterCategories, "Фильтра", beginIndex, 7);
            beginIndex = setParentCategory(daughterCategories, "Тормозные элементы", beginIndex, 11);
            beginIndex = setParentCategory(daughterCategories, "Гидравлика тормозной системы", beginIndex, 15);
            beginIndex = setParentCategory(daughterCategories, "Электронные компоненты", beginIndex, 5);
            beginIndex = setParentCategory(daughterCategories, "Детали двигателя", beginIndex, 26);
            beginIndex = setParentCategory(daughterCategories, "Прокладки двигателя", beginIndex, 6);
            beginIndex = setParentCategory(daughterCategories, "Ремни, цепи и натяжители", beginIndex, 11);
            beginIndex = setParentCategory(daughterCategories, "Система спуска", beginIndex, 6);
            beginIndex = setParentCategory(daughterCategories, "Топливная система и управление двигателем", beginIndex, 9);
            beginIndex = setParentCategory(daughterCategories, "Турбины и компрессоры", beginIndex, 3);
            beginIndex = setParentCategory(daughterCategories, "Подвеска", beginIndex, 15);
            beginIndex = setParentCategory(daughterCategories, "Рулевое управление", beginIndex, 5);
            beginIndex = setParentCategory(daughterCategories, "Запчасти трансмиссии", beginIndex, 8);
            beginIndex = setParentCategory(daughterCategories, "Сцепление", beginIndex, 7);
            beginIndex = setParentCategory(daughterCategories, "Охлаждение", beginIndex, 7);
            beginIndex = setParentCategory(daughterCategories, "Система кондиционирования", beginIndex, 5);
            beginIndex = setParentCategory(daughterCategories, "Система отопления", beginIndex, 2);
            beginIndex = setParentCategory(daughterCategories, "Зажигание", beginIndex, 2);
            beginIndex = setParentCategory(daughterCategories, "Освещение", beginIndex, 6);
            beginIndex = setParentCategory(daughterCategories, "Электрика", beginIndex, 8);
            beginIndex = setParentCategory(daughterCategories, "Внутренние элементы", beginIndex, 3);
            beginIndex = setParentCategory(daughterCategories, "Наружные части", beginIndex, 11);
            setParentCategory(daughterCategories, "Система стеклоочистителя ", beginIndex, 3);

            categoryService.addCategories(daughterCategories);

        }



    }

    private void addParentCategories(){
        categoryService.addCategories(
                List.of(
                        new Category("Расходники"),
                        new Category("Фильтра"),
                        new Category("Тормозные элементы"),
                        new Category("Гидравлика тормозной системы"),
                        new Category("Электронные компоненты"),
                        new Category("Детали двигателя"),
                        new Category("Прокладки двигателя"),
                        new Category("Ремни, цепи и натяжители"),
                        new Category("Система спуска"),
                        new Category("Топливная система и управление двигателем"),
                        new Category("Турбины и компрессоры"),
                        new Category("Подвеска"),
                        new Category("Рулевое управление"),
                        new Category("Запчасти трансмиссии"),
                        new Category("Сцепление"),
                        new Category("Охлаждение"),
                        new Category("Система кондиционирования"),
                        new Category("Система отопления"),
                        new Category("Зажигание"),
                        new Category("Освещение"),
                        new Category("Электрика"),
                        new Category("Внутренние элементы"),
                        new Category("Наружные части"),
                        new Category("Система стеклоочистителя")
                ));
    }

    private List<Category> getDaughterCategories(){

        List<Category> categoryList = new ArrayList<>();

        categoryList.add(new Category("Аккумулятор"));
        categoryList.add(new Category("Комплект ГРМ"));
        categoryList.add(new Category("Ремень ГРМ"));
        categoryList.add(new Category("Свеча зажигания"));
        categoryList.add(new Category("Тормозные диски"));
        categoryList.add(new Category("Тормозные колодки"));

        categoryList.add(new Category("Воздушный фильтр"));
        categoryList.add(new Category("Комплект фильтров"));
        categoryList.add(new Category("Масляный фильтр"));
        categoryList.add(new Category("Топливный фильтр"));
        categoryList.add(new Category("Фильтр салона"));
        categoryList.add(new Category("Фильтр антифриза"));
        categoryList.add(new Category("Фильтр фильтр коробки"));

        categoryList.add(new Category("Комплект дисковых тормозов"));
        categoryList.add(new Category("Комплект направляющей суппорта"));
        categoryList.add(new Category("Ремкомплект барабанных колодок"));
        categoryList.add(new Category("Скобы тормозных колодок"));
        categoryList.add(new Category("Стояночный тормоз"));
        categoryList.add(new Category("Тормозной барабан"));
        categoryList.add(new Category("Тормозной поршень"));
        categoryList.add(new Category("Тормозные диски"));
        categoryList.add(new Category("Тормозные колодки"));
        categoryList.add(new Category("Тормозные колодки барабанные"));
        categoryList.add(new Category("Трос ручного тормоза"));

        categoryList.add(new Category("Вакуумный насос"));
        categoryList.add(new Category("Вакуумный усилитель тормозов"));
        categoryList.add(new Category("Главный тормозной цилиндр"));
        categoryList.add(new Category("Клапан"));
        categoryList.add(new Category("Рабочий тормозной цилиндр"));
        categoryList.add(new Category("Распределитель тормозных усилий"));
        categoryList.add(new Category("Регулятор увеличения силы пружины"));
        categoryList.add(new Category("Ремкомплект главного тормозного цилиндра"));
        categoryList.add(new Category("Ремкомплект тормозного суппорта"));
        categoryList.add(new Category("Ремкомплект тормозного цилиндра"));
        categoryList.add(new Category("Скоба тормозного суппорта"));
        categoryList.add(new Category("Тормозная жидкость"));
        categoryList.add(new Category("Тормозной суппорт"));
        categoryList.add(new Category("Тормозной шланг"));
        categoryList.add(new Category("Тормозные трубопроводы"));

        categoryList.add(new Category("Датчик АБС"));
        categoryList.add(new Category("Датчик износа тормозных колодок"));
        categoryList.add(new Category("Регулировка динамики движения"));
        categoryList.add(new Category("Выключатель фонаря сигнала торможения"));
        categoryList.add(new Category("Гидроаккумулятор, реле давления"));

        categoryList.add(new Category("Cальники клапанов"));
        categoryList.add(new Category("Болт ГБЦ"));
        categoryList.add(new Category("Воздушный патрубок"));
        categoryList.add(new Category("Гидрокомпенсаторы"));
        categoryList.add(new Category("Клапан впускной"));
        categoryList.add(new Category("Клапан выпускной"));
        categoryList.add(new Category("Комплект поршневых колец"));
        categoryList.add(new Category("Коренной вкладыш"));
        categoryList.add(new Category("Коромысло"));
        categoryList.add(new Category("Крышка масло заливной горловины"));
        categoryList.add(new Category("Масляный насос"));
        categoryList.add(new Category("Масляный поддон"));
        categoryList.add(new Category("Направляющая клапана"));
        categoryList.add(new Category("Патрубок вентиляции картера"));
        categoryList.add(new Category("Подушка двигателя"));
        categoryList.add(new Category("Полукольца коленвала"));
        categoryList.add(new Category("Поршень"));
        categoryList.add(new Category("Пробка поддона"));
        categoryList.add(new Category("Распредвал"));
        categoryList.add(new Category("Сальник коленвала"));
        categoryList.add(new Category("Сальник распредвала"));
        categoryList.add(new Category("Цепь масляного насоса"));
        categoryList.add(new Category("Шатунный вкладыш"));
        categoryList.add(new Category("Шестерня коленвала"));
        categoryList.add(new Category("Шестерня распредвала"));
        categoryList.add(new Category("Щуп масляный"));

        categoryList.add(new Category("Комплект прокладок двигателя"));
        categoryList.add(new Category("Прокладка впускного коллектора"));
        categoryList.add(new Category("Прокладка выпускного коллектора"));
        categoryList.add(new Category("Прокладка ГБЦ"));
        categoryList.add(new Category("Прокладка клапанной крышки"));
        categoryList.add(new Category("Прокладка масляного поддона"));

        categoryList.add(new Category("Клиновой ремень"));
        categoryList.add(new Category("Натяжитель ремня генератора"));
        categoryList.add(new Category("Натяжитель ремня ГРМ"));
        categoryList.add(new Category("Натяжитель цепи ГРМ"));
        categoryList.add(new Category("Натяжной ролик ремня ГРМ"));
        categoryList.add(new Category("Ремень генератора"));
        categoryList.add(new Category("Ролик ремня генератора"));
        categoryList.add(new Category("Ролик ремня ГРМ"));
        categoryList.add(new Category("Успокоитель цепи ГРМ"));
        categoryList.add(new Category("Цепь ГРМ"));
        categoryList.add(new Category("Шкив коленвала"));

        categoryList.add(new Category("Глушитель выхлопных газов"));
        categoryList.add(new Category("Гофра глушителя"));
        categoryList.add(new Category("Катализатор"));
        categoryList.add(new Category("Крепление глушителя"));
        categoryList.add(new Category("Прокладка глушителя"));
        categoryList.add(new Category("Хомуты глушителя"));

        categoryList.add(new Category("Датчик давления масла"));
        categoryList.add(new Category("Датчик положения дроссельной заслонки"));
        categoryList.add(new Category("Датчик положения коленвала"));
        categoryList.add(new Category("Лямбда-зонд"));
        categoryList.add(new Category("Насос высокого давления"));
        categoryList.add(new Category("Прокладка под форсунку"));
        categoryList.add(new Category("Расходомер воздуха"));
        categoryList.add(new Category("Топливный насос"));
        categoryList.add(new Category("Форсунка"));

        categoryList.add(new Category("Интеркулер"));
        categoryList.add(new Category("Прокладка турбины"));
        categoryList.add(new Category("Турбина"));

        categoryList.add(new Category("Амортизаторы"));
        categoryList.add(new Category("Болт крепления колеса"));
        categoryList.add(new Category("Втулка стабилизатора"));
        categoryList.add(new Category("Комплект пыльника и отбойника амортизатора"));
        categoryList.add(new Category("Опора амортизатора"));
        categoryList.add(new Category("Отбойник"));
        categoryList.add(new Category("Подшипник ступицы"));
        categoryList.add(new Category("Пружина подвески"));
        categoryList.add(new Category("Пыльник амортизатора"));
        categoryList.add(new Category("Рычаг подвески"));
        categoryList.add(new Category("Сайлентблок задней балки"));
        categoryList.add(new Category("Сайлентблок рычага"));
        categoryList.add(new Category("Стойка стабилизатора"));
        categoryList.add(new Category("Ступица"));
        categoryList.add(new Category("Шаровая опора"));

        categoryList.add(new Category("Наконечник рулевой тяги"));
        categoryList.add(new Category("Насос гидроусилителя руля"));
        categoryList.add(new Category("Пыльник рулевой рейки"));
        categoryList.add(new Category("Рулевая рейка"));
        categoryList.add(new Category("Рулевая тяга"));

        categoryList.add(new Category("Подушка коробки передач (АКПП)"));
        categoryList.add(new Category("Подушка коробки передач (МКПП)"));
        categoryList.add(new Category("Полуось в сборе"));
        categoryList.add(new Category("Прокладка поддона АКПП"));
        categoryList.add(new Category("Пыльник шруса"));
        categoryList.add(new Category("Сальник полуоси"));
        categoryList.add(new Category("Фильтр масляный АКПП"));
        categoryList.add(new Category("ШРУС"));

        categoryList.add(new Category("Выжимной подшипник"));
        categoryList.add(new Category("Главный цилиндр сцепления"));
        categoryList.add(new Category("Диск сцепления"));
        categoryList.add(new Category("Комплект сцепления"));
        categoryList.add(new Category("Корзина сцепления"));
        categoryList.add(new Category("Маховик"));
        categoryList.add(new Category("Рабочий цилиндр сцепления"));

        categoryList.add(new Category("Вентилятор системы охлаждения двигателя"));
        categoryList.add(new Category("Крышка радиатора"));
        categoryList.add(new Category("Крышка расширительного бачка"));
        categoryList.add(new Category("Помпа (водяной насос)"));
        categoryList.add(new Category("Радиатор охлаждения двигателя"));
        categoryList.add(new Category("Расширительный бачок"));
        categoryList.add(new Category("Термостат"));

        categoryList.add(new Category("Испаритель"));
        categoryList.add(new Category("Компрессор кондиционера"));
        categoryList.add(new Category("Осушитель кондиционера"));
        categoryList.add(new Category("Радиатор кондиционера"));
        categoryList.add(new Category("Расширительный клапан кондиционера"));

        categoryList.add(new Category("Вентилятор салона"));
        categoryList.add(new Category("Радиатор печки"));

        categoryList.add(new Category("Катушка зажигания"));
        categoryList.add(new Category("Свеча накаливания"));

        categoryList.add(new Category("Выключатель стоп-сигнала"));
        categoryList.add(new Category("Задний фонарь"));
        categoryList.add(new Category("Лампа ближнего света"));
        categoryList.add(new Category("Основная фара"));
        categoryList.add(new Category("Противотуманная фара"));
        categoryList.add(new Category("Указатель поворотов"));

        categoryList.add(new Category("Генератор"));
        categoryList.add(new Category("Датчик включения вентилятора"));
        categoryList.add(new Category("Датчик скорости"));
        categoryList.add(new Category("Датчик температуры охлаждающей жидкости"));
        categoryList.add(new Category("Кнопка стеклоподьемника"));
        categoryList.add(new Category("Мост (выпрямитель) генератора"));
        categoryList.add(new Category("Муфта генератора"));
        categoryList.add(new Category("Стартер"));

        categoryList.add(new Category("Амортизатор багажника и капота"));
        categoryList.add(new Category("Замок двери"));
        categoryList.add(new Category("Стеклоподъемник"));

        categoryList.add(new Category("Бампер передний / задний"));
        categoryList.add(new Category("Защита двигателя"));
        categoryList.add(new Category("Капот"));
        categoryList.add(new Category("Крыло переднее"));
        categoryList.add(new Category("Наружное зеркало"));
        categoryList.add(new Category("Панель передняя"));
        categoryList.add(new Category("Петля капота"));
        categoryList.add(new Category("Подкрылок"));
        categoryList.add(new Category("Решетка радиатора"));
        categoryList.add(new Category("Стекло лобовое"));
        categoryList.add(new Category("Усилитель бампера"));

        categoryList.add(new Category("Двигатель стеклоочистителя"));
        categoryList.add(new Category("Насос омывателя"));
        categoryList.add(new Category("Щетка стеклоочистителя"));

        return categoryList;
    }

    private int setParentCategory(List<Category> daughterCategories, String parentCategoryName, int beginIndex, int count){

        Category parentCategory = categoryService.getCategoryByName(parentCategoryName);

        daughterCategories.subList(beginIndex, beginIndex + count).forEach(category -> category.setParentCategory(parentCategory));

        return beginIndex + count;
    }
}