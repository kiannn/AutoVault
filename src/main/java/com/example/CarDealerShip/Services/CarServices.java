package com.example.CarDealerShip.Services;

import com.example.CarDealerShip.Models.NHTSAResponse;
import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.CarMakeModelDTO;
import com.example.CarDealerShip.Models.CarWithDocsDTO;
import com.example.CarDealerShip.Models.Documents;
import com.example.CarDealerShip.Models.CarSearchDTO;
import com.example.CarDealerShip.Models.CarStatsDTO;
import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Models.Transmissions;
import com.example.CarDealerShip.Repository.CarRepository;
import com.example.CarDealerShip.Repository.OwnerRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CarServices {

    @Autowired
    private CarRepository CarRepository;

    @Autowired
    private OwnerRepository OwnerRepository;

    @Autowired
    DocumentService DocumentService;

    @Autowired
    WebClient client;

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    class CarComparator implements Comparator<CarWithDocsDTO> {

        public String by;

        public CarComparator(String s) {

            by = s;
        }

        @Override
        public int compare(CarWithDocsDTO o1, CarWithDocsDTO o2) {

            return switch (by) {
                case "itemNo" ->
                    o1.getItemNo().compareTo(o2.getItemNo());
                case "make" ->
                    o1.getMake().compareTo(o2.getMake());
                case "model" ->
                    o1.getModel().compareTo(o2.getModel());
                case "year" ->
                    o1.getYear().compareTo(o2.getYear());
                case "datePurchased" ->
                    o1.getDatePurchased().compareTo(o2.getDatePurchased());
                case "price" ->
                    o1.getPrice().compareTo(o2.getPrice());
                case "powerTrain" ->
                    o1.getPowerTrain().compareTo(o2.getPowerTrain());
                case "condn" ->
                    o1.getCondn().compareTo(o2.getCondn());
                default ->
                    o1.getHorsePower().compareTo(o2.getHorsePower());
            };
        }

    }
 
    @Transactional // MUST be present, otherwise at line CarServices.java:112 when we addAll the newly uploaded documents we get: org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.example.CarDealerShip.Models.Car.docs: could not initialize proxy - no Session
    public Integer add_Update(CarWithDocsDTO carDTO, String Owner, MultipartFile[] filee) throws IOException {

        Car car = Car.builder()
                        .make(carDTO.getMake())
                        .model(carDTO.getModel())
                        .year(carDTO.getYear())
                        .datePurchased(carDTO.getDatePurchased())
                        .price(carDTO.getPrice())
                        .powerTrain(carDTO.getPowerTrain())
                        .condn(carDTO.getCondn())
                        .horsePower(carDTO.getHorsePower())
                        .build();    
        
        car.setOwner(OwnerRepository.findById(Owner).get());
        
        List<Documents> addDocument = DocumentService.retreiveDocument(filee, car); 
        
        Integer id = carDTO.getItemNo();
        if (id == null) {
            car.setDocs(addDocument);
        } else {
            car.setItemNo(id); 
            List<Documents> docs = CarRepository.findById(id).get().getDocs();
            docs.addAll(addDocument);
            car.setDocs(docs);
        }
        Car save = CarRepository.save(car);
        
        Runtime runtime = Runtime.getRuntime();
        log.info("Max Memory: {}", (runtime.maxMemory() / (1024 * 1024)) + " MB");
        log.info("Total Memory: {}", (runtime.totalMemory() / (1024 * 1024)) + " MB");
        log.info("Free Memory: {}", (runtime.freeMemory() / (1024 * 1024)) + " MB");

        return save.getItemNo();
    }

    public void deleteSelectedCars(List<Integer> asList) {

       CarRepository.deleteAllById(asList); 
    }
    
    public List<CarWithDocsDTO> findCarById(int id) {
        List<CarWithDocsDTO> findById = CarRepository.findCarById(id);
        return findById;
    }
    
    public CarMakeModelDTO findCar_MakeAndModel_ById(int id) {
        CarMakeModelDTO findById = CarRepository.findCar_MakeAndModel_ById(id);
        return findById;
    }
    
    public CarStatsDTO findCar_Stats_ById(int id) {
        CarStatsDTO findById = CarRepository.findCar_Stats_ById(id);
        return findById;
    }
    
    @Transactional
    public List<CarWithDocsDTO> getAllCars(String authorizedUser) {

        List<CarWithDocsDTO> collect = null;
        try (Stream<CarWithDocsDTO> findByOwnerUsername = CarRepository.findByOwnerUsername(authorizedUser)) {
            collect = findByOwnerUsername.collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("catch -> "+collect);
        }
        return collect;
    }

    public List<CarWithDocsDTO> getCarsSortBy(List<CarWithDocsDTO> car, String by) {

        boolean descending = by.contains("-");
        String suby = by.substring(0, !descending ? by.length() : by.indexOf('-'));

        List<CarWithDocsDTO> nullKeeper = new ArrayList<>();
        List<CarWithDocsDTO> copy = new ArrayList<>(Arrays.asList(new CarWithDocsDTO[car.size()]));

        Collections.copy(copy, car);

            switch (suby) {
                case "itemNo" -> {
                    List<Integer> collect = car.stream().map(c -> c.getItemNo()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                }
                case "make" -> {
                    List<String> collect = car.stream().map(c -> c.getMake()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                }
                case "model" -> {
                    List<String> collect = car.stream().map(c -> c.getModel()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getModel() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getModel() == null);
                }
                case "year" -> {
                    List<Integer> collect = car.stream().map(c -> c.getYear()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getYear() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getYear() == null);
                }
                case "datePurchased" -> {
                    List<LocalDate> collect = car.stream().map(c -> c.getDatePurchased()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getDatePurchased() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getDatePurchased() == null);
                }
                case "price" -> {
                    List<Double> collect = car.stream().map(c -> c.getPrice()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getPrice() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getPrice() == null);
                }
                case "powerTrain" -> {
                    List<Transmissions> collect = car.stream().map(c -> c.getPowerTrain()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getPowerTrain() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getPowerTrain() == null);
                }
                case "condn" -> {
                    List<String> collect = car.stream().map(c -> c.getCondn()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getCondn() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getCondn() == null);
                }
                case "horsePower" -> {
                    List<Double> collect = car.stream().map(c -> c.getHorsePower()).collect(Collectors.toList());
                    boolean equals = alreadySort(collect, descending);
                    if (equals) {
                        return car;
                    }
                    car.stream().forEach(e -> {
                        if (e.getHorsePower() == null) {
                            nullKeeper.add(e);
                        }
                    });
                    copy.removeIf(e -> e.getHorsePower() == null);
                }
            }
   
        CarComparator com = new CarComparator(suby);
        Collections.sort(copy, com);
        nullKeeper.addAll(copy);
        if (by.endsWith("desc")) {

            Collections.reverse(nullKeeper);
        }

        return nullKeeper;
    }

    public boolean alreadySort(List<? extends Comparable> collect, boolean descending) {
        
        List<Comparable> copyElement = new ArrayList<>(Arrays.asList(new Comparable[collect.size()]));
        Collections.copy(copyElement, collect);
        collect.sort(descending ? Comparator.nullsLast(Comparator.reverseOrder()) : Comparator.nullsFirst(Comparator.naturalOrder()));
        boolean equals = collect.equals(copyElement);
        return equals;
    }

    public List<Object> searchForCars(CarSearchDTO carFrom, String name) {

        Double priceFrom = carFrom.getPrice() == null ? 0 : carFrom.getPrice();
        Double priceTo = carFrom.getPriceTo() == null ? Integer.MAX_VALUE : carFrom.getPriceTo();
 
        Integer yearFrom = carFrom.getYear() == null ? LocalDate.MIN.getYear() : carFrom.getYear();
        Integer yearTo = carFrom.getYearTo() == null ? LocalDate.MAX.getYear() : carFrom.getYearTo();

        LocalDate of = LocalDate.of(0, 1, 1);
        LocalDate plus = LocalDate.now().plus(Period.ofYears(1000));
        
        LocalDate dateFrom = carFrom.getDatePurchased() == null || carFrom.getDatePurchased().isBefore(of) ? of : carFrom.getDatePurchased();
        LocalDate dateTo = carFrom.getDatePurchasedTo() == null || carFrom.getDatePurchasedTo().isAfter(plus)? plus : carFrom.getDatePurchasedTo();

        List<CarWithDocsDTO> result = carFrom.getModelList().isEmpty() ? CarRepository.queryBasedOnSearchNoModel(name,carFrom.getMake(),                                                    
                                                                                                priceFrom, priceTo,
                                                                                                yearFrom, yearTo,
                                                                                                dateFrom, dateTo)
                                    
                                                : CarRepository.queryBasedOnSearch(name,carFrom.getMake(),                                                     
                                                                                carFrom.getModelList(),
                                                                                priceFrom, priceTo,
                                                                                yearFrom, yearTo,
                                                                                dateFrom, dateTo);
        
        CarSearchDTO actualSearchInput = CarSearchDTO.builder()
                .make(carFrom.getMake())
                .modelList(carFrom.getModelList())
                .year(yearFrom)
                .yearTo(yearTo)
                .datePurchased(dateFrom)
                .datePurchasedTo(dateTo)
                .price(priceFrom)
                .priceTo(priceTo).build();

        List<Object> resultAndsearchInput = new ArrayList<>();
        resultAndsearchInput.addAll(result);
        resultAndsearchInput.add(actualSearchInput);

        return resultAndsearchInput;

    }

    public List<Boolean> columnEntirelyHasNoValueSort(List<CarWithDocsDTO> findAll, List<String[]> listOfProperties) {

        if (findAll.size() < 2) {
            return Collections.nCopies(listOfProperties.size(), false);
        }
        Stream<CarWithDocsDTO> stream = null;

        List<? extends Comparable> map = null;
        List<Boolean> noValue = new ArrayList<>();
        List<String[]> ptoperties = listOfProperties.subList(1, listOfProperties.size());

        for (String[] suby : ptoperties) {
            stream = findAll.stream();

            switch (suby[0]) {

                case "make" ->
                    map = stream.map(a -> a.getMake()).collect(Collectors.toList());
                case "model" ->
                    map = stream.map(a -> a.getModel()).collect(Collectors.toList());
                case "year" ->
                    map = stream.map(a -> a.getYear()).collect(Collectors.toList());
                case "datePurchased" ->
                    map = stream.map(a -> a.getDatePurchased()).collect(Collectors.toList());
                case "price" ->
                    map = stream.map(a -> a.getPrice()).collect(Collectors.toList());
                case "powerTrain" ->
                    map = stream.map(a -> a.getPowerTrain()).collect(Collectors.toList());
                case "condn" ->
                    map = stream.map(a -> a.getCondn()).collect(Collectors.toList());
                case "horsePower" ->
                    map = stream.map(a -> a.getHorsePower()).collect(Collectors.toList());

            }

            int frequency = Collections.frequency(map, map.getFirst());
            noValue.add(!(frequency == map.size()));

        }
        return noValue;
    }

    public void adjustModelMakeList(List<MakeAndModel> availableMakesAndModels, CarWithDocsDTO carDTO) {

        List<String> keysLowerCaseMake = availableMakesAndModels.stream().map(a -> a.getAvailableMake().toLowerCase()).toList();
        String toLowerCaseMake = carDTO.getMake().trim().toLowerCase();

        List<String> keysLowerCaseModel = availableMakesAndModels.stream().map(a -> a.getAvailableModel().toLowerCase()).toList();
        String toLowerCaseModel = carDTO.getModel().trim().toLowerCase();

        boolean containsModel = keysLowerCaseModel.contains(toLowerCaseModel);
        boolean containsMake = keysLowerCaseMake.contains(toLowerCaseMake);

        if (containsModel && containsMake) {

            carDTO.setMake(availableMakesAndModels.get(keysLowerCaseMake.indexOf(toLowerCaseMake)).getAvailableMake());
            carDTO.setModel(availableMakesAndModels.get(keysLowerCaseModel.indexOf(toLowerCaseModel)).getAvailableModel());

        } else if (containsMake && !containsModel) {

            MakeAndModel c = new MakeAndModel();

            int lastIdx = keysLowerCaseMake.lastIndexOf(toLowerCaseMake);

            c.setAvailableMake(availableMakesAndModels.get(lastIdx).getAvailableMake());
            c.setAvailableModel(carDTO.getModel().trim());

            carDTO.setMake(availableMakesAndModels.get(lastIdx).getAvailableMake());
            carDTO.setModel(carDTO.getModel().trim());

            availableMakesAndModels.add(lastIdx + 1, c);

        } else {

            MakeAndModel c = new MakeAndModel();
            c.setAvailableMake(carDTO.getMake().trim());
            c.setAvailableModel(carDTO.getModel().trim());

            availableMakesAndModels.add(c);
        }
    }

    public boolean isValidCarIds(String name, Integer[] ids) {

        List<Integer> allIds = CarRepository.findCarIdsByOwnerUsername(name);

        boolean containsAll = allIds.containsAll(Arrays.asList(ids));

        return containsAll;
    }

    public long numberOfRecors() {

        return CarRepository.count();
    }

    public List<String> verifyMakeAndModelValidity(String make, String model) {

        ResponseEntity<NHTSAResponse> response;

            response = client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                    .path("api/vehicles/GetModelsForMake/" + make)
                    .queryParam("format", "json")
                    .build())
                    .retrieve().toEntity(NHTSAResponse.class).block();

        List<String> collectModel = response.getBody().data.stream()
                                                            .map(car -> car.getModel().toLowerCase())
                                                               .collect(Collectors.toList());

        String toLowerCase = model.toLowerCase();

        for (String s : collectModel) {

            if (s.contains(toLowerCase)) {

                List<CarMakeModelDTO> cars = response.getBody().data;
                int i = collectModel.indexOf(s);

                return List.of(cars.getFirst().getMake(), cars.get(i).getModel());
            }
        }

        return List.of(make);

    }

    public Collection<List<CarWithDocsDTO>> arrangDataForView(List<CarWithDocsDTO> cars) {
        
        Collection<List<CarWithDocsDTO>> values = cars.stream()
                .collect(Collectors.groupingBy(c -> c.getItemNo(), () -> new LinkedHashMap<>(), Collectors.toList()))
                .values();
        
        return values;
    }


}
