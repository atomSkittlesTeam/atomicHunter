package net.vniia.skittles.generators;

import jakarta.annotation.PostConstruct;
import net.vniia.skittles.entities.Product;
import net.vniia.skittles.entities.Request;
import net.vniia.skittles.entities.RequestPosition;
import net.vniia.skittles.repositories.ProductRepository;
import net.vniia.skittles.repositories.RequestPositionRepository;
import net.vniia.skittles.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Deprecated
public class DataGenerator {

    @Value("${generators.request}")
    private Boolean generatorRequestEnable;

    @Value("${generators.request-positions}")
    private Boolean generatorRequestPositionsEnable;

    @Value("${generators.product}")
    private Boolean generatorProductEnable;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private RequestPositionRepository requestPositionRepository;

    @Autowired
    private ProductRepository productRepository;

    private static int positionsCount = 15;

    @PostConstruct
    public void generateData() {
        this.generateRequests();
        this.generateRequestPositions();
        this.generateProducts();
    }

    private void generateRequests() {
        if (generatorRequestEnable) {
            List<Request> requestList = requestListGenerate();
            requestRepository.saveAllAndFlush(requestList);
        }
    }

    private void generateRequestPositions() {
        if (generatorRequestPositionsEnable) {
            List<RequestPosition> requestPositionList = requestPositionListGenerate();
            requestPositionRepository.saveAll(requestPositionList);
        }
    }

    private void generateProducts() {
        if (generatorProductEnable) {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                List<Product> productList = productListGenerate();
                productRepository.saveAllAndFlush(productList);
            }
        }
    }

    private List<Product> productListGenerate() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product productOne = new Product(
                    (long) i, //id - автогенерируется, здесь как заглушка для allArgs
                    "Генерированный designation " + String.valueOf(i),
                    "Генерированный name " + String.valueOf(i)
            );
            productList.add(productOne);
        }
        return productList;
    }

    private List<Request> requestListGenerate() { //todo когда продукты перейдут в позиции, переделать генератор
        Random random = new Random();
        List<Request> requestList = new ArrayList<>();
        List<Long> productIds = productRepository.findAll().stream().map(Product::getId).toList();
        for (int i = 0; i < positionsCount; i++) {
            Request requestOne = new Request(
                    (long) i, //id - автогенерируется, здесь как заглушка для allArgs
                    String.valueOf(random.nextInt(0, 10)), //number
                    Date.from(Instant.now()), //requestDate
                    "Генерированный description " + String.valueOf(i), //description
                    Date.from(Instant.now()), //releaseDate
                    (long) i, //priority
//                    productIds.get(random.nextInt(0, productIds.size()-1)), //productId
                    false
            );
            requestList.add(requestOne);
        }
        return requestList;
    }

    private List<RequestPosition> requestPositionListGenerate() {
        Random random = new Random();
        List<RequestPosition> requestPositionList = new ArrayList<>();
        int index = 0;
        List<Long> requestIds = requestRepository.findAll().stream().map(Request::getId).toList();
        for (int i = 0; i < positionsCount; i++) {
            for (int j = 0; j < random.nextInt(4,6); j++) {

                RequestPosition requestPosition = new RequestPosition(
                        (long) index, //id - автогенерируется, здесь как заглушка для allArgs
                        requestIds.get(random.nextInt(0, requestIds.size()-1)), //requestId
                        "Генерированный note " + i + " " + index, 1l, false); //note
                requestPositionList.add(requestPosition);
                index++;
            }
        }
        return requestPositionList;
    }
}
