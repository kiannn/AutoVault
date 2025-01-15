package com.example.CarDealerShip.Services;

import com.example.CarDealerShip.Models.NHTSAResponse;
import com.example.CarDealerShip.Models.Car;
import com.example.CarDealerShip.Models.Documents;
import com.example.CarDealerShip.Models.Owner;
import com.example.CarDealerShip.Models.CarSearchDTO;
import com.example.CarDealerShip.Models.MakeAndModel;
import com.example.CarDealerShip.Repository.CarRepository;
import com.example.CarDealerShip.Repository.OwnerRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    String[] att = new String[]{"model", "year", "datePurchased",
                                    "price", "powerTrain",
                                    "condn", "horsePower"};
    
    public Car add_Update(Car car, String Owner, MultipartFile[] filee) throws IOException {

        List<Documents> addDocument = DocumentService.retreiveDocument(filee, car);

        if (car.getItemNo() == null) {

            Owner findById = OwnerRepository.findById(Owner).get();
            car.setOwner(findById);
            car.setDocs(addDocument);

        } else {

            car.getDocs().addAll(addDocument);
        }

        Runtime runtime = Runtime.getRuntime();
        System.out.println("Max Memory: " + (runtime.maxMemory() / (1024 * 1024)) + " MB");
        System.out.println("Total Memory: " + (runtime.totalMemory() / (1024 * 1024)) + " MB");
        System.out.println("Free Memory: " + (runtime.freeMemory() / (1024 * 1024)) + " MB");
        
        Car save = CarRepository.save(car);

        return save;
    }

    public Car add_Update_NoFile(Car car, String Owner) throws IOException {

        Owner findById = OwnerRepository.findById(Owner).get();
        car.setOwner(findById);

        Car save = CarRepository.save(car);

        return save;
    }

    public void deleteSelectedCars(List<Integer> asList) {

       CarRepository.deleteAllById(asList); 
    }

    public Optional<Car> findCarById(int id) {
        Optional<Car> findById = CarRepository.findById(id);
        
        return findById;
    }
    
    @Transactional
    public List<Car> getAllCars(String authorizedUser) {

        List<Car> collect = null;
        try (Stream<Car> findByOwnerUsername = CarRepository.findByOwnerUsername(authorizedUser)) {
            collect = findByOwnerUsername.collect(Collectors.toList());
        } catch (Exception e) {
        }
        return collect;
    }
  
    public List<Car> getAllCarsSortBy(String username,String by) {

        String suby = by.substring(0, by.indexOf('-') == -1 ? by.length() : by.indexOf('-'));

        List<Car> findAllOrderby = CarRepository.findAllOrderby(username,Sort.by(suby));

        if (by.endsWith("desc")) {

            Collections.reverse(findAllOrderby);
        }

        return findAllOrderby;
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

        List<Car> result = carFrom.getModelList().isEmpty() ? CarRepository.queryBasedOnSearchNoModel(name,carFrom.getMake(),                                                    
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
    
    public List<Car> searchForCarsOrderBy(CarSearchDTO q, String by, String name) {
  
     String suby = by.substring(0, by.indexOf('-') == -1 ? by.length() : by.indexOf('-'));
         
     List<Car> queryBasedOnSearchOrderBy = q.getModelList()
                                               .isEmpty()?
                                                         CarRepository.queryBasedOnSearchNoModelOrderBy(
                                                        name,q.getMake(),
                                                        q.getPrice(), q.getPriceTo(),
                                                        q.getYear(), q.getYearTo(),
                                                        q.getDatePurchased(),q.getDatePurchasedTo(), 
                                                                               Sort.by(suby))
                                                        : CarRepository.queryBasedOnSearchOrderBy(
                                                        name, q.getMake(),
                                                        q.getModelList(),
                                                        q.getPrice(), q.getPriceTo(),
                                                        q.getYear(), q.getYearTo(),
                                                        q.getDatePurchased(), 
                                                        q.getDatePurchasedTo(),
                                                        Sort.by(suby));

            if (by.endsWith("desc")) {
               
                Collections.reverse(queryBasedOnSearchOrderBy);
            }
            
            return queryBasedOnSearchOrderBy;
    }

    public List<Boolean> columnEntirelyHasNoValueSort(List<Car> findAll) {

        if (findAll.isEmpty()) {
            return Collections.nCopies(att.length, false);
        }
        Stream<Car> stream;

        List<? extends Comparable> map = null;
        List<Boolean> noValue = new ArrayList<>();

        for (String suby : att) {

            stream = findAll.stream();

            switch (suby) {

                case "model" ->
                    map = stream.map(a -> a.getModel()).toList();
                case "year" ->
                    map = stream.map(a -> a.getYear()).toList();
                case "datePurchased" ->
                    map = stream.map(a -> a.getDatePurchased()).toList();
                case "price" ->
                    map = stream.map(a -> a.getPrice()).toList();
                case "powerTrain" ->
                    map = stream.map(a -> a.getPowerTrain()).toList();
                case "condn" ->
                    map = stream.map(a -> a.getCondn()).toList();
                case "horsePower" ->
                    map = stream.map(a -> a.getHorsePower()).toList();

            }

            int frequency = Collections.frequency(map, map.getFirst());
            noValue.add(!(frequency == map.size()));

        }
        return noValue;
    }

    public void adjustModelMakeList(List<MakeAndModel> availableMakesAndModels, Car car) {

        List<String> keysLowerCaseMake = availableMakesAndModels.stream().map(a -> a.getAvailableMake().toLowerCase()).toList();
        String toLowerCaseMake = car.getMake().trim().toLowerCase();

        List<String> keysLowerCaseModel = availableMakesAndModels.stream().map(a -> a.getAvailableModel().toLowerCase()).toList();
        String toLowerCaseModel = car.getModel().trim().toLowerCase();

        boolean containsModel = keysLowerCaseModel.contains(toLowerCaseModel);
        boolean containsMake = keysLowerCaseMake.contains(toLowerCaseMake);

        if (containsModel && containsMake) {

            car.setMake(availableMakesAndModels.get(keysLowerCaseMake.indexOf(toLowerCaseMake)).getAvailableMake());
            car.setModel(availableMakesAndModels.get(keysLowerCaseModel.indexOf(toLowerCaseModel)).getAvailableModel());

        } else if (containsMake && !containsModel) {

            MakeAndModel c = new MakeAndModel();

            int lastIdx = keysLowerCaseMake.lastIndexOf(toLowerCaseMake);

            c.setAvailableMake(availableMakesAndModels.get(lastIdx).getAvailableMake());
            c.setAvailableModel(car.getModel().trim());

            car.setMake(availableMakesAndModels.get(lastIdx).getAvailableMake());
            car.setModel(car.getModel().trim());

            availableMakesAndModels.add(lastIdx + 1, c);

        } else {

            MakeAndModel c = new MakeAndModel();
            c.setAvailableMake(car.getMake().trim());
            c.setAvailableModel(car.getModel().trim());

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
        
        ResponseEntity<NHTSAResponse> response  = client
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("api/vehicles/GetModelsForMake/"+make)
                .queryParam("format", "json")
                .build()
                )
                .retrieve().toEntity(NHTSAResponse.class).block(); 
        
        List<String> collectModel = response.getBody()
                                            .data.stream()
                                            .map(car -> car.getModel().toLowerCase()).collect(Collectors.toList());

        String toLowerCase = model.toLowerCase();

        for (String s : collectModel) {

            if (s.contains(toLowerCase)) {

                List<Car> cars = response.getBody().data;
                int i = collectModel.indexOf(s);

                return List.of(cars.getFirst().getMake(), cars.get(i).getModel());
            }
        }

        return List.of(make);

    }
    

}
